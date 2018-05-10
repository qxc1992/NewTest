package com.chaoyi

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by yupenglei on 2018/4/27.
 */
fun getDeviceWidth(): Int {
    val windowManager = App.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

@TargetApi(Build.VERSION_CODES.M)
fun setStatusBarColor(activity: Activity, dark: Boolean) {
    val decorView = activity.window.decorView
    var vis = decorView.systemUiVisibility
    vis = when (dark) {
        true -> vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        false -> vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    decorView.systemUiVisibility = vis
}

fun sendMail() {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:talk@moreless.io"))
    intent.putExtra(Intent.EXTRA_SUBJECT, "潮汐 for Android - 意见反馈") // 主题
    val createChooser = Intent.createChooser(intent, "潮汐 for Android - 意见反馈")
    App.instance.startActivity(createChooser)
}

fun sendPhoto() {
    val intent = Intent()
    intent.type = "image/*"
    val createChooser = Intent.createChooser(intent, "分享到")
    App.instance.startActivity(createChooser)
}

fun loadBitmapFromView(v: View): Bitmap? {
    v.isDrawingCacheEnabled = true
    v.buildDrawingCache()
    return v.drawingCache
}

fun getLastSevenday(): MutableList<String> {
    val list = mutableListOf<String>()
    val instance = Calendar.getInstance()
    for (item in 2 until 7) {
        val i = if (item == 2) 2 else 1
        instance.set(Calendar.DAY_OF_YEAR, instance.get(Calendar.DAY_OF_YEAR) - i)
        SimpleDateFormat("MM/dd", Locale.getDefault()).format(instance.time).let {
            Log.e("Main", it)
            list.add(it)
        }
    }
    list.reverse().let {
        list.apply {
            add("昨天")
            add("今天")
        }
    }
    return list

}

fun getMainTimeTag(): String {
    val time = Integer.parseInt(SimpleDateFormat("HH", Locale.getDefault()).format(Date()))
    val timeTags = App.instance.resources.getStringArray(R.array.time_tags)
    return when (time) {
        in 4..5 -> timeTags[0]
        in 6..8 -> timeTags[1]
        in 9..12 -> timeTags[2]
        in 12..14 -> timeTags[3]
        in 15..16 -> timeTags[4]
        in 17..18 -> timeTags[5]
        in 19..21 -> timeTags[6]
        in 22..24 -> timeTags[7]
        else -> timeTags[7]
    }
}