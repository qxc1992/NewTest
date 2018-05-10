package com.chaoyi.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

import com.chaoyi.R

/**
 * Created by yupenglei on 2018/5/8.
 */

class TiltView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var imageWidth: Int = 0//图片宽度
    private var imageHeight: Int = 0//图片高度
    private val angle = 10 * Math.PI / 180//三角形角度
    private var triangleHeight: Int = 0//三角形高度
    private var paint: Paint? = null//画笔
    private var path: Path? = null//绘制路径
    private val type: Int//倾斜图片的类型

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TiltView)
        type = array.getInteger(R.styleable.TiltView_type, 1)
        array.recycle()
    }


    //重测大小
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        imageWidth = measureSpec(widthMeasureSpec)
        imageHeight = measureSpec(heightMeasureSpec)
        setMeasuredDimension(imageWidth, imageHeight) //设置View的大小
        triangleHeight = Math.abs(Math.tan(angle) * imageHeight).toInt()
    }

    //测量长度
    private fun measureSpec(measureSpec: Int): Int {
        val minLength = 200
        val mode = MeasureSpec.getMode(measureSpec)
        var length = MeasureSpec.getSize(measureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            length = Math.min(length, minLength)
        }
        return length
    }

    override fun onDraw(canvas: Canvas) {
        initPaint()

        val mBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888) //初始化Bitmap
        val mCanvas = Canvas(mBitmap)//创建画布，并绘制mBitmap
        if (drawable == null) return
        val mBackBitmap =
                (drawable as BitmapDrawable).bitmap
        mCanvas.drawBitmap(resizeBitmap(mBackBitmap), 0f, 0f, null)//绘制Bitmap

        setTriangle()
        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        mCanvas.drawPath(path!!, paint!!)

        canvas.drawBitmap(mBitmap, 0f, 0f, null)
    }

    //初始化画笔
    private fun initPaint() {
        paint = Paint()
        paint!!.isDither = true//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint!!.isAntiAlias = true//设置抗锯齿
        paint!!.strokeWidth = 5f
        paint!!.style = Paint.Style.FILL
        paint!!.strokeCap = Paint.Cap.ROUND
        paint!!.strokeJoin = Paint.Join.ROUND//圆角
    }


    //设置三角形区域
    private fun setTriangle() {
        path = Path()
        when (type) {
            1//右下角
            -> {
                path!!.moveTo(0f, imageHeight.toFloat())
                path!!.lineTo(imageWidth.toFloat(), imageHeight.toFloat())
                path!!.lineTo(imageWidth.toFloat(), (imageHeight - triangleHeight).toFloat())
                path!!.lineTo(0f, imageHeight.toFloat())
            }
            2//左上角+左下角
            -> {
                path!!.moveTo(0f, triangleHeight.toFloat())
                path!!.lineTo(imageWidth.toFloat(), 0f)
                path!!.lineTo(0f, 0f)
                path!!.lineTo(0f, imageHeight.toFloat())
                path!!.lineTo(imageWidth.toFloat(), imageHeight.toFloat())
                path!!.lineTo(0f, (imageHeight - triangleHeight).toFloat())
            }
            3//右上角+右下角
            -> {
                path!!.moveTo(imageWidth.toFloat(), triangleHeight.toFloat())
                path!!.lineTo(0f, 0f)
                path!!.lineTo(imageWidth.toFloat(), 0f)
                path!!.lineTo(imageWidth.toFloat(), imageHeight.toFloat())
                path!!.lineTo(0f, imageHeight.toFloat())
                path!!.lineTo(imageWidth.toFloat(), (imageHeight - triangleHeight).toFloat())
            }
            4//右上角
            -> {
                path!!.moveTo(0f, 0f)
                path!!.lineTo(imageWidth.toFloat(), 0f)
                path!!.lineTo(imageWidth.toFloat(), triangleHeight.toFloat())
                path!!.lineTo(0f, 0f)
            }
            5//左上角
            -> {
                path!!.moveTo(0f, 0f)
                path!!.lineTo(imageWidth.toFloat(), 0f)
                path!!.lineTo(0f, triangleHeight.toFloat())
                path!!.lineTo(0f, 0f)
            }
        }
    }

    //重新调节图片大小
    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        // 设置想要的大小
        val newWidth = imageWidth
        val newHeight = imageHeight
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

}

