package com.chaoyi.model

import android.bluetooth.BluetoothGattCallback
import android.os.Bundle
import android.preference.PreferenceFragment
import android.util.Log
import com.chaoyi.App
import com.chaoyi.DelegateExtension
import com.chaoyi.R
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by yupenglei on 2018/4/27.
 */
data class Pics(val pic_url: String, val quotes: Quotes) {
    var tS: String? = null

    class Quotes {

        @SerializedName("zh-Hans")
        var zh_Hans: String? = null

        val en: String? = null

        @SerializedName("zh-Hant")
        val zh_Hant: String? = null
    }
}

class picsReuestData {
    val lang = "en"
    val ver = "1.3.6"
    val size = Size()
    val dates = arrayOf("${SimpleDateFormat("YYYYMMdd").format(Date())}")

    class Size {
        val h = 1920
        val w = 2160
    }
}

var SP_PIC: String by DelegateExtension.preference(App.instance, "SP_PIC", "")
var SP_TIME_MINUTES: Int by DelegateExtension.preference(App.instance, "SP_TIME_MINUTES", 0)
var SP_ADD_QRCODE: Boolean by DelegateExtension.preference(App.instance, "SP_ADD_QRCODE", false)
val TS = SimpleDateFormat("YYYYMMdd", Locale.getDefault()).format(Date())

fun Observable<String>.rawRequest(callback: (Pics) -> Unit) {
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val jsonObject = JSONObject(it)
                val string = jsonObject.getString(TS)
                val pics = App.gson.fromJson(string, Pics::class.java)
                pics.tS = TS
                SP_PIC = App.gson.toJson(pics)
                pics.let(callback)
            }, {
                Log.e("Main", it.message, it)
            })


}



