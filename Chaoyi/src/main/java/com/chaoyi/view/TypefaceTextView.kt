package com.chaoyi.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by yupenglei on 2018/4/19.
 */
class TypefaceTextView(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {

    init {
        typeface = Typeface.createFromAsset(context.assets, "MFKeSong-Regular.ttf")
    }

    fun setTypeface(name: String) {
        typeface = Typeface.createFromAsset(context.assets, name)
    }

}