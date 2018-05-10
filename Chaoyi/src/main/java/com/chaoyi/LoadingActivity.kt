package com.chaoyi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import kotlinx.android.synthetic.main.act_loading.*
import org.jetbrains.anko.startActivity

/**
 * Created by yupenglei on 2018/4/26.
 */
class LoadingActivity : AppCompatActivity() {
    private val a = emptyArray<String>()
    private val b = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_loading)

        val translateAnimation = TranslateAnimation(0f, 0f, 0f, -300f)
        translateAnimation.duration = 1000
        translateAnimation.fillAfter = true
        translateAnimation.setAnimationListener(listener)
        imageView.startAnimation(translateAnimation)

    }

    private val listener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            startActivity<MainActivity>()
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            finish()
        }

        override fun onAnimationStart(animation: Animation?) {
        }

    }

}


