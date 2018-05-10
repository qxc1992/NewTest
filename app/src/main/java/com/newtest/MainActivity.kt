package com.newtest

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var a = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.adapter = MyAdapter()

        app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListenerd() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State, verticalOffset: Int) {
                when (state) {
                    State.COLLAPSED -> {
                        //关闭
                        Log.e("Main", "关闭")
                        image_bg.alpha = 0f
                    }
                    State.EXPANDED -> {
                        //展开
                        Log.e("Main", "展开")
                        setStatusBarColor(this@MainActivity, false)
                        title_about.setTextColor(Color.WHITE)
                        back.setImageDrawable(getDrawable(R.drawable.ic_action_arrow_left_white))
                        image_bg.alpha = 1f
                        a = true
                    }
                    State.IDLE -> {
                        //滑动
                        if (a) {
                            setStatusBarColor(this@MainActivity, true)
                            title_about.setTextColor(Color.BLACK)
                            back.setImageDrawable(getDrawable(R.drawable.ic_action_arrow_left_black))
                            a = false
                        }
                        val totalScrollRange = appBarLayout.totalScrollRange
                        val alpha = (totalScrollRange - Math.abs(verticalOffset)) / (totalScrollRange * 1.0f)
                        Log.e("Main", "$alpha")
                        image_bg.alpha = alpha
                    }
                }
            }

        })

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

}
//    private val textWatcher = object : MyTextWatcher() {
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////            show.text = edit.text
//        }
//    }

