package com.chaoyi.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View


import com.chaoyi.R

import java.util.ArrayList

/**
 * Created by zhouyou on 2016/9/27.
 * Class desc:
 *
 *
 * This is a circle on the proliferation of Android custom view.
 * GitHub：https://github.com/Airsaid/DiffuseView
 */
class DiffuseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : View(context, attrs, defStyleAttr) {

    /**
     * 扩散圆圈颜色
     */
    private var mColor: Int = 0
    /**
     * 圆圈中心颜色
     */
    private var mCoreColor: Int = 0
    /**
     * 圆圈中心图片
     */
    private var mBitmap: Bitmap? = null
    /**
     * 中心圆半径
     */
    private var mCoreRadius = 100f
    /**
     * 扩散圆宽度
     */
    private var mDiffuseWidth = 3
    /**
     * 最大宽度
     */
    private var mMaxWidth: Int = 255
    /**
     * 扩散速度
     */
    private var mDiffuseSpeed = 5
    /**
     * 是否正在扩散中
     */
    /**
     * 是否扩散中
     */
    var isDiffuse = false
        private set
    // 透明度集合
    private val mAlphas = ArrayList<Int>()
    // 扩散圆半径集合
    private val mWidths = ArrayList<Int>()
    private var mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mAlphas.add(255)
        mWidths.add(0)

        val a = context.obtainStyledAttributes(attrs, R.styleable.DiffuseView, defStyleAttr, 0)
        mColor = a.getColor(R.styleable.DiffuseView_diffuse_color, mColor)
        mCoreColor = a.getColor(R.styleable.DiffuseView_diffuse_coreColor, mCoreColor)
        mCoreRadius = a.getFloat(R.styleable.DiffuseView_diffuse_coreRadius, mCoreRadius)
        mDiffuseWidth = a.getInt(R.styleable.DiffuseView_diffuse_width, mDiffuseWidth)
        mMaxWidth = a.getInt(R.styleable.DiffuseView_diffuse_maxWidth, mMaxWidth)
        mDiffuseSpeed = a.getInt(R.styleable.DiffuseView_diffuse_speed, mDiffuseSpeed)
        a.recycle()
    }


    override fun invalidate() {
        if (hasWindowFocus()) {
            super.invalidate()
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            invalidate()
        }
    }

    public override fun onDraw(canvas: Canvas) {

        // 绘制中心圆及图片
         mPaint.alpha = 255
         mPaint.strokeWidth = 8f
         mPaint.color = mCoreColor
        canvas.drawCircle((width / 2).toFloat(), 750f, mCoreRadius,  mPaint)

        if (isDiffuse) {
            // 绘制扩散圆
             mPaint.color = mColor
             mPaint.strokeWidth = 3f
            for (i in mAlphas.indices) {
                // 设置透明度
                val alpha = mAlphas[i]
                 mPaint.alpha = alpha
                // 绘制扩散圆
                val width = mWidths[i]
                canvas.drawCircle((getWidth() / 2).toFloat(), 750f, mCoreRadius + width,  mPaint)

                if (alpha > 0 && width < mMaxWidth) {
                    mAlphas[i] = if (alpha - mDiffuseSpeed > 0) alpha - mDiffuseSpeed else 1
                    mWidths[i] = width + mDiffuseSpeed
                }
            }
            // 判断当扩散圆扩散到指定宽度时添加新扩散圆
            if (mWidths[mWidths.size - 1] >= mMaxWidth / mDiffuseWidth) {
                mAlphas.add(255)
                mWidths.add(0)
            }
            // 超过10个扩散圆，删除最外层
            if (mWidths.size >= 4) {
                mWidths.removeAt(0)
                mAlphas.removeAt(0)
            }

            invalidate()
        }
    }

    /**
     * 开始扩散
     */
    fun start() {
        isDiffuse = true
        invalidate()
    }

    /**
     * 停止扩散
     */
    fun stop() {
        isDiffuse = false
        mWidths.clear()
        mAlphas.clear()
        mAlphas.add(255)
        mWidths.add(0)
        invalidate()
    }

    /**
     * 设置扩散圆颜色
     */
    fun setColor(colorId: Int) {
        mColor = colorId
    }

    /**
     * 设置中心圆颜色
     */
    fun setCoreColor(colorId: Int) {
        mCoreColor = colorId
    }

    /**
     * 设置中心圆图片
     */
    fun setCoreImage(imageId: Int) {
        mBitmap = BitmapFactory.decodeResource(resources, imageId)
    }

    /**
     * 设置中心圆半径
     */
    fun setCoreRadius(radius: Int) {
        mCoreRadius = radius.toFloat()
    }

    /**
     * 设置扩散圆宽度(值越小宽度越大)
     */
    fun setDiffuseWidth(width: Int) {
        mDiffuseWidth = width
    }

    /**
     * 设置最大宽度
     */
    fun setMaxWidth(maxWidth: Int) {
        mMaxWidth = maxWidth
    }

    /**
     * 设置扩散速度，值越大速度越快
     */
    fun setDiffuseSpeed(speed: Int) {
        mDiffuseSpeed = speed
    }
}
