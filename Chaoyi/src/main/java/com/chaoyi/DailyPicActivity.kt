package com.chaoyi

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chaoyi.model.Pics
import com.chaoyi.model.SP_ADD_QRCODE
import com.chaoyi.model.SP_PIC
import kotlinx.android.synthetic.main.act_dailypic.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.support.design.widget.Snackbar
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by yupenglei on 2018/5/8.
 */
class DailyPicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_dailypic)
        setStatusBarColor(this, true)

        tb_dailypic_toolbar.title = "日贴分享"
        tb_dailypic_toolbar.setTitleTextColor(Color.BLACK)
        tb_dailypic_toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left_big)
        tb_dailypic_toolbar.setNavigationOnClickListener { finish() }

        val pics = App.gson.fromJson(SP_PIC, Pics::class.java)
        Glide.with(this).load(pics.pic_url).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                daily_pic.setImageDrawable(resource)
                return true
            }

        }).submit()
        val mingyans = pics.quotes.zh_Hans!!.split(" ")
        val times = SimpleDateFormat("yyyy#MMM#dd", Locale.ENGLISH).format(Date()).split("#")

        with(date_daily_day) {
            setTypeface("Didot.ttf")
            textSize = 20f
            text = times[2]
        }

        with(date_daily_M) {
            setTypeface("Roboto-Bold.ttf")
            textSize = 6f
            val a = StringBuilder(times[1].toUpperCase()).apply {
                insert(1, " ")
                insert(3, " ")
            }.toString()
            text = a
        }

        with(date_daily_Y) {
            setTypeface("Roboto-Bold.ttf")
            textSize = 6f
            val a = StringBuilder(times[0]).apply {
                insert(1, " ")
                insert(3, " ")
                insert(5, " ")
            }.toString()
            text = a
        }


        with(text_1) {
            setTypeface("NotoSerifCJKsc.ttf")
            text = mingyans[0]
            textSize = 11f

        }
        with(text_2) {
            setTypeface("NotoSerifCJKsc.ttf")
            text = "—"
            textSize = 13f

        }
        with(text_3) {
            setTypeface("NotoSerifCJKsc.ttf")
            text = mingyans[2]
            textSize = 8f
        }


        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SP_ADD_QRCODE = true
                card_qrcode.visibility = VISIBLE
            } else {
                SP_ADD_QRCODE = false
                card_qrcode.visibility = GONE
            }
        }

        checkbox.isChecked = SP_ADD_QRCODE

        daily_download_rl.setOnClickListener {
            val bitmap = loadBitmapFromView(container_daily)
            try {
                val filePic = File("sdcard/", "aaaa.jpg")
                if (!filePic.exists()) {
                    filePic.parentFile.mkdirs()
                    filePic.createNewFile()
                }
                val fos = FileOutputStream(filePic)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Snackbar.make(it, "图片已保存", Snackbar.LENGTH_SHORT).show()
        }

        daily_share_rl.setOnClickListener {
            sendPhoto()
        }

    }


}