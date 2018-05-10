package com.chaoyi

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.chaoyi.listener.AppBarStateChangeListenerd
import kotlinx.android.synthetic.main.act_about.*

/**
 * Created by yupenglei on 2018/5/7.
 */
class AboutActivity : AppCompatActivity() {
    private var isExpanded = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_about)

        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT

        setStatusBarColor(this, true)
        initView()
        initToolBar()
    }

    private fun initView() {
        back.setOnClickListener { finish() }
        suggest.setOnClickListener {
            sendMail()
        }
    }

    private fun initToolBar() {
        app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListenerd() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State, verticalOffset: Int) {
                when (state) {
                    State.COLLAPSED -> {
                        //关闭
                        image_bg.alpha = 0f
                        description.alpha = 0f
                        suggest.isClickable = false
                    }
                    State.EXPANDED -> {
                        //展开
                        setStatusBarColor(this@AboutActivity, false)
                        title_about.setTextColor(Color.WHITE)
                        back.setImageDrawable(getDrawable(R.mipmap.ic_action_arrow_left_white))
                        image_bg.alpha = 1f
                        description.alpha = 1f
                        suggest.isClickable = true
                        isExpanded = true
                    }
                    State.IDLE -> {
                        //滑动
                        if (isExpanded) {
                            setStatusBarColor(this@AboutActivity, true)
                            title_about.setTextColor(Color.BLACK)
                            back.setImageDrawable(getDrawable(R.mipmap.ic_action_arrow_left_black))
                            suggest.isClickable = true
                            isExpanded = false
                        }
                        val totalScrollRange = appBarLayout.totalScrollRange
                        val alpha = (totalScrollRange - Math.abs(verticalOffset)) / (totalScrollRange * 1.0f)
                        image_bg.alpha = alpha
                        description.alpha = alpha
                    }
                }
            }

        })
    }
}