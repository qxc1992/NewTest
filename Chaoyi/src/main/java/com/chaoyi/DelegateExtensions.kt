package com.chaoyi

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 委托扩展
 * Created by yupenglei on 17/8/29.
 */

object DelegateExtension {
    fun <T> preference(context: Context, name: String, default: T) = Preference(context, name, default)
}


class Preference<T>(val context: Context, val name: String, val default: T) : ReadWriteProperty<Any?, T> {
    companion object {
        const val SP_DEFAULT_NAME = "default"
    }

    private val prefs by lazy { context.getSharedPreferences(SP_DEFAULT_NAME, Context.MODE_PRIVATE)!! }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name, default)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = putPreference(name, value)

    private fun <T> findPreference(name: String, default: T): T = with(prefs) {
        val result: Any = when (default) {
            is Int -> getInt(name, default)
            is Long -> getLong(name, default)
            is Float -> getFloat(name, default)
            is String -> getString(name, default)
            is Boolean -> getBoolean(name, default)
            else -> throw IllegalArgumentException()
        }
        @Suppress("unchecked_cast")
        return result as T
    }

    private fun <T> putPreference(name: String, value: T) = prefs.edit().apply {
        when (value) {
            is Int -> putInt(name, value)
            is Long -> putLong(name, value)
            is Float -> putFloat(name, value)
            is String -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            else -> throw IllegalArgumentException()
        }
    }.apply()
}
