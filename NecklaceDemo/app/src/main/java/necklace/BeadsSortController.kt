package necklace

import android.arch.lifecycle.MediatorLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangfeihang on 2019/1/17.
 */
class BeadsSortController(
    private val observableCurNecklace: MediatorLiveData<List<NecklaceViewModel.ShadowBead>>,
    private val observableBackUpList: MediatorLiveData<List<NecklaceViewModel.ShadowBead>>
) {

    companion object {
        private const val MAX_BEADS = 5
        private const val TAG = "BeadsSortController"
    }

    //将项链的变化和需要add、remove的view联系起来的数据类型
    private var mIOCurNecklace = IOCurNecklace(observableCurNecklace)

    //按照优先级和可见性排序，第一个永远是最需要被填进去的珠子
    private val mIOBackUpList = IOBackUpList(observableBackUpList)

    private var mBeadChangedEmitter: FlowableEmitter<Pair<NecklaceViewModel.ShadowBead, Boolean>>? = null

    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        mCompositeDisposable.add(
            Flowable.create(FlowableOnSubscribe<Pair<NecklaceViewModel.ShadowBead, Boolean>> {
                mBeadChangedEmitter = it
            }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(Consumer {
                    onBeadsChanged(it.first, it.second)
                }, NeckLaceRxUtils.errorConsumer(TAG))
        )
    }

    //todo 这个函数异步的。think
    internal fun onBeadsRegister(shadowBead: NecklaceViewModel.ShadowBead) {
        //由多个珠子的状态变化来决定要不要add View
        (observableCurNecklace).addSource(shadowBead.expectedVisible) { curBeadExpectedVisible ->
            //io线程处理，减少卡顿
            mBeadChangedEmitter!!.onNext(Pair(shadowBead, curBeadExpectedVisible!!))
        }
        shadowBead.expectedVisible.postValue(true)
        shadowBead.realBead.onCreate()
    }

    //在io线程同步操作
    private fun onBeadsChanged(curShadowBead: NecklaceViewModel.ShadowBead, curBeadExpectedVisible: Boolean) {

//        MLog.info(TAG, "onBeadsChanged, bead:${curShadowBead.beadsConfig}, curBeadExpectedVisible:$curBeadExpectedVisible")

        val beadsConfig = curShadowBead.beadsConfig

        val sBead = mIOCurNecklace.firstOrNull { it.beadsConfig == beadsConfig }

//        MLog.info(TAG, "${BeadsId.valueOf(beadsConfig.beadsId)} is bead in cur necklace? ${sBead != null}")

        //珠子就在当前项链里
        if (sBead != null) {
            //珠子被设置为不可见
            if (!curBeadExpectedVisible) {
                moveToBackUpTeam(sBead)
                plugHole()
            } else {
//                MLog.info(TAG, "curBeads is visible already")
            }
        } else {//珠子不在当前项链里

            //先加到备选队伍里
            val backupBead = mIOBackUpList.firstOrNull { it.beadsConfig == beadsConfig }
//            MLog.info(TAG, "${BeadsId.valueOf(beadsConfig.beadsId)} isInTheBackUpTeam: ${backupBead != null}")
            if (backupBead == null) {
                mIOBackUpList.add(curShadowBead)
            } else {
                mIOBackUpList.reSort()
            }

            //珠子期望可见
            if (curBeadExpectedVisible) {
                plugHole()
            }
        }
    }

    //在io线程同步操作
    //降级到备选队伍里
    private fun moveToBackUpTeam(shadowBead: NecklaceViewModel.ShadowBead) {
//        MLog.debug(TAG, "moveToBackUpTeam:${shadowBead.beadsConfig}")
        mIOCurNecklace.remove(shadowBead)
        mIOBackUpList.add(shadowBead)
    }

    //在io线程同步操作
    //项链中缺了空，从备选队伍中，根据优先级和可见性补空
    private fun plugHole() {

        fun hasBackUp(): Boolean = mIOBackUpList.size > 0 &&
            (mIOBackUpList[0].expectedVisible.value ?: false)

//        MLog.info(TAG, "plugHole-------\n" +
//            mIOBackUpList.joinToString(separator = "\n") { it.toString() })

        while (hasBackUp() && mIOCurNecklace.size < MAX_BEADS) {
//            MLog.info(TAG, "plugHole:${mIOBackUpList[0].beadsConfig}")
            mIOCurNecklace.add(mIOBackUpList[0])
            mIOBackUpList.removeAt(0)
        }
    }

    fun clear() {
        mIOCurNecklace.clear()
        mIOBackUpList.clear()
    }

    internal fun onClear() {
        mCompositeDisposable.clear()
    }
}