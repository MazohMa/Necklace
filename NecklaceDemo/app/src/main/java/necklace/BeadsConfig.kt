package necklace

import android.util.Log
import com.google.gson.annotations.SerializedName
import io.reactivex.Flowable

/**
 * Created by wangfeihang on 2019/1/2.
 */

object BeadsConfigRepository {

    private const val TAG = "BeadsConfigRepository"

    private var beadsConfigList: List<BeadsConfig>? = null

    init {
        //提前请求配置，缓存起来
        getBeadsConfigs()
            .subscribe({
                Log.d(TAG, "getBeadsConfigs suc")
            }, {
                Log.e(TAG, "getBeadsConfigs failed:${it.message}")
            })
    }

    fun getBeadsConfigs(): Flowable<List<BeadsConfig>?> {
        beadsConfigList = ArrayList()
        var bead = BeadsConfig(7, "image", "yyMobile", 1, 1)
        var bead1 = BeadsConfig(1, "image", "yyMobile", 1, 2)
        Log.d(TAG, "getBeadsConfigs, beadsConfigList is null?${beadsConfigList == null}")
        return Flowable.just(beadsConfigList?.plus(bead)?.plus(bead1))
    }
}

data class BeadsConfig(

    val beadsId: Int,
    val img: String?,
    val schemeURL: String?,
    val priority: Int,
    val position: Int
) {

    override fun equals(other: Any?): Boolean {
        return (other is BeadsConfig)
            && beadsId == other.beadsId
            && position == other.position
    }

    override fun hashCode() = beadsId.hashCode() + position.hashCode()

    override fun toString(): String {
        return "BeadsConfig(" +
            "beadsId=${BeadsId.valueOf(beadsId)}, " +
            "img=$img, " +
            "schemeURL=$schemeURL, " +
            "priority=$priority, " +
            "position=$position)"
    }
}

enum class BeadsId(val value: Int) {
    MENU_ICON(1),
    ROTATION_ICON(2),
    DANMU_ICON(3),
    SPECIAL_GIFT_ICON(9),
    TURN_TABLE_ICON(10),
    SCENE_GIFT_ICON(4),
    SHENYOU_ICON(5),
    PK_CROWN_ICON(6),
    GIFT_ICON(7),
    DEFAULT_ICON(8);

    companion object {
        fun valueOf(value: Int): BeadsId? {
            return BeadsId.values().firstOrNull { it.value == value }
        }
    }
}

