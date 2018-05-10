package com.chaoyi

import android.app.Application
import com.google.gson.Gson
import kotlin.properties.Delegates

/**
 * Created by yupenglei on 2018/4/27.
 */
class App : Application() {
    companion object {
        var instance: App by Delegates.notNull()
        var gson:Gson by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        gson = Gson()
        instance = this
    }
}