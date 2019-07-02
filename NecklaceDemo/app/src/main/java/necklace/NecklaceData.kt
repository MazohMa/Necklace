package necklace

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by wangfeihang on 2019/1/7.
 */
class IOCurNecklace(private val observableList: MediatorLiveData<List<NecklaceViewModel.ShadowBead>>) :
    ArrayList<NecklaceViewModel.ShadowBead>() {

    override fun remove(element: NecklaceViewModel.ShadowBead): Boolean {
        val index = this.indexOf(element)
        val result = super.remove(element)
        val copyList = ArrayList(this)

        fun onRemoveBead(shadowBead: NecklaceViewModel.ShadowBead) {
            AndroidSchedulers.mainThread().scheduleDirect {
                shadowBead.realBead.onVisibleChange(false, index)
                copyList.forEach { it.realBead.onLayoutChanged() }
            }
        }

        onRemoveBead(element)
        observableList.postValue(copyList)
        return result
    }

    override fun add(element: NecklaceViewModel.ShadowBead): Boolean {
        val returnValue = super.add(element)
        reSort()
        val index = this.indexOf(element)
        Log.i("NecklaceView", "index:" + index + "; beadid:" + BeadsId.valueOf(element.beadsConfig.beadsId))
        element.isChangeToVisible = true
        observableList.postValue(ArrayList(this))
        return returnValue
    }

    private fun reSort() {
        this.sortWith(kotlin.Comparator { o1, o2 -> o1.beadsConfig.position - o2.beadsConfig.position })
    }
}

class IOBackUpList(private val observableList: MutableLiveData<List<NecklaceViewModel.ShadowBead>>) :
    ArrayList<NecklaceViewModel.ShadowBead>() {

    override fun add(element: NecklaceViewModel.ShadowBead): Boolean {
        val returnValue = super.add(element)
        reSort()
        observableList.postValue(ArrayList(this))
        return returnValue
    }

    override fun remove(element: NecklaceViewModel.ShadowBead): Boolean {
        val value = super.remove(element)
        observableList.postValue(ArrayList(this))
        return value
    }

    override fun removeAt(index: Int): NecklaceViewModel.ShadowBead {
        val value = super.removeAt(index)
        observableList.postValue(ArrayList(this))
        return value
    }

    fun reSort() {
        this.sortWith(kotlin.Comparator { o1, o2 ->
            if (o1.expectedVisible.value == o2.expectedVisible.value) {
                return@Comparator o2.beadsConfig.priority - o1.beadsConfig.priority
            } else {
                val o1priority: Int = if (o1.expectedVisible.value == true) 1 else 0
                val o2priority: Int = if (o2.expectedVisible.value == true) 1 else 0
                return@Comparator o2priority - o1priority
            }
        })
    }
}