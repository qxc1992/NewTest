package com.chaoyi

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.ADJUST_SAME
import android.media.AudioManager.FLAG_SHOW_UI
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.util.Log
import android.view.View
import com.chaoyi.login.LoginActivity
import kotlinx.android.synthetic.main.act_setting.*
import org.jetbrains.anko.startActivity


/**
 * Created by yupenglei on 2018/4/28.
 */
class SettingActivity : PreferenceActivity() {
    private val titleText: String by lazy { intent.getStringExtra("title") ?: "设置" }
    private val resourceId: Int by lazy { intent.getIntExtra("id", R.xml.settings) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_setting)
        setStatusBarColor(this, true)
        initToolBar()
        Log.e("Mian", "SettingActivity$resourceId")
        fragmentManager.beginTransaction().replace(R.id.fl_setting_act_content, MyFragment().apply {
            arguments = Bundle()
            arguments.putInt("id", resourceId)
        }).commit()
    }

    private fun initToolBar() {
        toolbar.setNavigationIcon(R.mipmap.ic_close_black)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = titleText
    }


    override fun isValidFragment(fragmentName: String?): Boolean {
        return true
    }


    class MyFragment : PreferenceFragment() {
        private val resourceId: Int by lazy { arguments.getInt("id") }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.e("Mian", "MyFragment$resourceId")
            addPreferencesFromResource(resourceId)
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            when (resourceId) {
                R.xml.settings -> {
                    findPreference("pref_key_account").setOnPreferenceClickListener {
                        startActivity<LoginActivity>()
                        true
                    }

                    findPreference("pref_key_focus_time").setOnPreferenceChangeListener { preference, newValue ->
                        preference.summary = "${newValue}分钟"
                        true
                    }

                    findPreference("pref_key_rest").setOnPreferenceClickListener {
                        startActivity<SettingActivity>("id" to R.xml.rest, "title" to "休息设置")
                        true
                    }

                    findPreference("pref_key_auto_rest").setOnPreferenceClickListener {
                        startActivity<SettingActivity>("id" to R.xml.auto_rest, "title" to "自动专注")
                        true
                    }

                    findPreference("pref_key_volume").setOnPreferenceClickListener {
                        val audioManager = App.instance.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        audioManager.adjustStreamVolume(
                                AudioManager.STREAM_MUSIC
                                , ADJUST_SAME
                                , FLAG_SHOW_UI)
                        true
                    }

                }

                R.xml.auto_rest -> {

                }

                R.xml.rest -> {

                }
            }


        }

    }

}