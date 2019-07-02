package necklace

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import necklace.scene.VisibleInterceptor
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangfeihang on 2019/1/2.
 */
class NecklaceViewModel : ViewModel() {

    val observableCurNecklace: MediatorLiveData<List<ShadowBead>> = MediatorLiveData()
    private val observableBackUpList: MediatorLiveData<List<ShadowBead>> = MediatorLiveData()

    private val beadsSortController = BeadsSortController(observableCurNecklace, observableBackUpList)
    private var registerEmitter: FlowableEmitter<FakeShadowBead>? = null
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private var registeredBeadsConfigs = ArrayList<BeadsConfig>()

    companion object {
        private const val TAG = "NecklaceViewModel"
    }

    init {
        mCompositeDisposable.add(
            Flowable.create(FlowableOnSubscribe<FakeShadowBead> {
                registerEmitter = it
            }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap { getFindBeadsConfigFlowable(it) }
                .subscribe(Consumer {
                    onBeadsRegister(it)
                }, NeckLaceRxUtils.errorConsumer(TAG))
        )
    }

    private fun getFindBeadsConfigFlowable(fakeShadowBead: FakeShadowBead): Flowable<ShadowBead> {
        return BeadsConfigRepository.getBeadsConfigs()
            .flatMap {
                Flowable.fromIterable(it)
            }
            .filter { beadsConfig ->
                BeadsId.valueOf(beadsConfig.beadsId) == fakeShadowBead.beadsId
                    && !registeredBeadsConfigs.contains(beadsConfig)
            }.map { ShadowBead(fakeShadowBead.factory, it, fakeShadowBead.lifecycle) }
    }

    fun register(beadFactory: BeadFactory, beadsId: BeadsId, lifecycle: Lifecycle) {
        Log.i(TAG, "register, beadid:$beadsId")
        registerEmitter?.onNext(FakeShadowBead(beadFactory, beadsId, lifecycle))
    }

    fun getBead(beadsId: BeadsId): Bead? {
        return (observableCurNecklace.value?.firstOrNull {
            it.beadsConfig.beadsId == beadsId.value
        } ?: observableBackUpList.value?.firstOrNull {
            it.beadsConfig.beadsId == beadsId.value
        })?.realBead
    }

    private fun onBeadsRegister(shadowBead: ShadowBead) {
        Log.i(TAG, "onBeadsRegister:${shadowBead.beadsConfig}")
        registeredBeadsConfigs.add(shadowBead.beadsConfig)
        beadsSortController.onBeadsRegister(shadowBead)
    }

    inner class FakeShadowBead(
        val factory: BeadFactory,
        val beadsId: BeadsId,
        val lifecycle: Lifecycle
    )

    inner class ShadowBead(
        private val factory: BeadFactory,
        val beadsConfig: BeadsConfig,
        lifecycle: Lifecycle
    ) {

        var isChangeToVisible = false
        val expectedVisible: MutableLiveData<Boolean> by lazy {
            MutableLiveData<Boolean>().apply {
                this.postValue(false)
            }
        }
        val realBead by lazy { factory.create(WrapperContext(), lifecycle, beadsConfig) }

        var isWishVisible: Boolean = true

        private inner class WrapperContext : NecklaceContext {
            override fun setVisibleInterceptor(visibleInterceptor: VisibleInterceptor) {
                this.visibleInterceptor = visibleInterceptor
                visibleInterceptor.setOnStateChange {
                    val intercept = visibleInterceptor.intercept()
                    //如果状态变化，但是被拦截，并且当前可见的话
                    if (intercept && expectedVisible.value == true) {
                        expectedVisible.value = false
                    }

                    if (!intercept) {
                        expectedVisible.value = isWishVisible
                    }
                }
            }

            private var visibleInterceptor: VisibleInterceptor? = null

            override fun isVisible(beadsId: BeadsId?): Boolean {
                return observableCurNecklace.value?.any { it.beadsConfig.beadsId == beadsId?.value ?: beadsConfig.beadsId }
                    ?: false
            }

            override fun requestShowSelf() {
                isWishVisible = true
                if (visibleInterceptor?.intercept() != true) {
                    Log.i(TAG, "${BeadsId.valueOf(beadsConfig.beadsId)?.name} requestShowSelf")
                    //防止用户在别的线程调用
                    expectedVisible.postValue(true)
                } else {
                    Log.i(
                        TAG,
                        "${BeadsId.valueOf(beadsConfig.beadsId)?.name} requestShowSelf, but been intercepted"
                    )
                }
            }

            override fun requestHideSelf() {
                isWishVisible = false
                Log.d(TAG, "${BeadsId.valueOf(beadsConfig.beadsId)?.name} requestHideSelf")
                //防止用户在别的线程调用
                expectedVisible.postValue(false)
            }

            override fun clearVisibleInterceptor() {
                visibleInterceptor = null
            }
        }

        override fun toString(): String {
            return "beadsConfig:$beadsConfig, expectedVisible:${expectedVisible.value}"
        }
    }

    internal fun onOrientationChanged(isLandscape: Boolean) {
        observableCurNecklace.value?.forEach { it.realBead.onOrientationChange(isLandscape) }
        observableBackUpList.value?.forEach { it.realBead.onOrientationChange(isLandscape) }
    }

    fun destroyBead() {
        registeredBeadsConfigs.clear()
        observableCurNecklace.value?.forEach { it.realBead.onDestroy() }
        observableCurNecklace.value = listOf()
        observableBackUpList.value?.forEach { it.realBead.onDestroy() }
        observableBackUpList.value = listOf()
        beadsSortController.clear()
    }

    override fun onCleared() {
        registerEmitter = null
        mCompositeDisposable.clear()
        beadsSortController.onClear()
        observableCurNecklace.value?.forEach { it.realBead.onDestroy() }
        observableBackUpList.value?.forEach { it.realBead.onDestroy() }
        super.onCleared()
    }
}