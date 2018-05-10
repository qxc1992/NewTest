package com.newtest

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by yupenglei on 2018/4/19.
 */
abstract class MyTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}