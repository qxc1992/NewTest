package com.chaoyi.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chaoyi.R
import com.chaoyi.setStatusBarColor

/**
 * Created by yupenglei on 2018/4/28.
 */
class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(this, true)
        setContentView(R.layout.act_login)

    }


}