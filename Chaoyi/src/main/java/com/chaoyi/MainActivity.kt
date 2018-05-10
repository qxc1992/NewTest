package com.chaoyi

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chaoyi.model.*
import com.chaoyi.server.Api
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.viewpager_item.view.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var isMusicPlaying = false
    private var isPrepared = false
    private var disposable: Disposable? = null
    private var residueTime: Long = 0
    private val titles = arrayOf(getMainTimeTag(), "雨天", "森林", "冥想", "咖啡")
    private val colors = intArrayOf(R.color.rimuli
            , R.color.rain
            , R.color.senlin
            , R.color.mingxiang
            , R.color.coffee)
    private val music = arrayOf("ocean.ogg", "rain.ogg", "forest.ogg", "meditation.ogg", "coffee.ogg")
    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer().apply {
            isLooping = true
            setOnPreparedListener {
                if (isMusicPlaying) it.start() else isPrepared = true
            }
        }
    }
    val items: List<String> by lazy { resources.getStringArray(R.array.entry_values_focus_time).toList() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT

        initToolBar()


        viewPager.adapter = myAdapter
        viewPager.addOnPageChangeListener(listener)
        viewPager.offscreenPageLimit = 5

        start.setOnClickListener(this)
        pause.setOnClickListener(this)
        giveUp.setOnClickListener(this)
        keepOn.setOnClickListener(this)

        val pics = App.gson.fromJson(SP_PIC, Pics::class.java)
        if (pics != null && pics.tS == TS) {
            Log.e("Main", "不需要更新数据")
            callback(pics)
        } else {
            Api.rxInstance.getPics().rawRequest(callback)
        }

    }

    private fun initToolBar() {
        toolbar.inflateMenu(R.menu.menu_tz)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.setting -> startActivity<SettingActivity>()
                R.id.absorbed -> startActivity<CalculateActivity>()
                R.id.share -> startActivity<DailyPicActivity>()
                R.id.suggest -> {
                    sendMail()
                }
                R.id.about -> startActivity<AboutActivity>()
            }
            false
        }
    }


    private val callback: (Pics) -> Unit =
            {
                text.text = it.quotes.zh_Hans!!.split("。")[0]
                Glide.with(this).load(it.pic_url).into(imageView)
                        .onLoadFailed(resources.getDrawable(R.mipmap.back, null))
            }

    private val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            imageView.translationX = -positionOffsetPixels.toFloat().plus(position * getDeviceWidth()).div(12)
        }

        override fun onPageSelected(position: Int) {
            Thread {
                mediaPlayer.reset()
                val afd = assets.openFd(music[position])
                mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.declaredLength)
                mediaPlayer.prepareAsync()
            }.start()

        }

    }

    /**
     * SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss 'CST' yyyy", Locale.ENGLISH);
    Date date = dateFormat.parse("Fri Aug 28 18:08:30 CST 2015");
     */
    private val myAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = 5

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(R.layout.viewpager_item, container, false)
            with(view) {
                background = resources.getDrawable(colors[position], null)
                title.text = titles[position]
                val points = arrayListOf<TextView>(point1, point2, point3, point4, point5)
                points[position].isEnabled = false
                if (position == 0) {
                    time.visibility = VISIBLE
                    ts.text = SimpleDateFormat("MMM.dEEE     yyyy", Locale.ENGLISH).format(Date()).toUpperCase()
                } else title.textSize = 50f


                wheel_view.setItems(items, SP_TIME_MINUTES)
                wheel_view.setOnItemSelectedListener { selectedIndex, _ ->
                    SP_TIME_MINUTES = selectedIndex
                }
                var showWheelView = false
                text_container.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if (!showWheelView) {
                            showWheelView = true
                            text_container.visibility = GONE
                            wheel_view.setItems(items, SP_TIME_MINUTES)
                            wheel_view.visibility = VISIBLE
                            Observable.timer(3, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        showWheelView = false
                                        text_container.visibility = VISIBLE
                                        wheel_view.visibility = GONE
                                    }
                        }
                    }
                    true
                }
            }
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

    }

    private fun startTimer(time: Long) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time)
                .map {
                    time.minus(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                        changeModel(false)
                        stopTimer()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        residueTime = t
                        var second = t.rem(60).toString()
                        var minutes = t.div(60).toString()
                        if (second.length == 1) second = "0$second"
                        if (minutes.length == 1) minutes = "0$minutes"
                        for (index: Int in 0 until viewPager.adapter!!.count) {
                            val view = viewPager.getChildAt(index)
                            view.timer_Scheduler.text = "$minutes : $second"
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                })

    }

    private fun restartTimer() = startTimer(residueTime)

    private fun stopTimer() = disposable?.dispose()


    override fun onClick(v: View) {
        when (v.id) {
            R.id.start -> {
                changeModel(true)
                startTimer(items[SP_TIME_MINUTES].toLong().times(60))
                isMusicPlaying = true
                start.visibility = GONE
                pause.visibility = VISIBLE
                if (isPrepared) {
                    mediaPlayer.start()
                    isPrepared = false
                } else {
                    val afd = assets.openFd(music[viewPager.currentItem])
                    mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.declaredLength)
                    mediaPlayer.prepareAsync()
                }
            }
            R.id.pause -> {
                for (index: Int in 0 until viewPager.adapter!!.count) {
                    val view = viewPager.getChildAt(index)
                    view.diffuseview.stop()
                }
                stopTimer()
                isMusicPlaying = false
                pause.visibility = GONE
                buttonContainer.visibility = VISIBLE
                if (mediaPlayer.isPlaying) mediaPlayer.pause()

            }
            R.id.keepOn -> {
                for (index: Int in 0 until viewPager.adapter!!.count) {
                    val view = viewPager.getChildAt(index)
                    view.diffuseview.start()
                }
                restartTimer()
                isMusicPlaying = true
                buttonContainer.visibility = GONE
                pause.visibility = VISIBLE
                if (!mediaPlayer.isPlaying) mediaPlayer.start()

            }
            R.id.giveUp -> {
                changeModel(false)
                stopTimer()
                isMusicPlaying = false
                buttonContainer.visibility = GONE
                start.visibility = VISIBLE
                mediaPlayer.reset()
            }
        }

    }

    fun changeModel(start: Boolean) {
        for (index: Int in 0 until viewPager.adapter!!.count) {
            val view = viewPager.getChildAt(index)
            when (start) {
                true -> {
                    view.diffuseview.start()
                    view.timer_Scheduler.visibility = VISIBLE
                    view.text_container.visibility = GONE
                    view.wheel_view.visibility = GONE
                }
                false -> {
                    view.diffuseview.stop()
                    view.timer_Scheduler.visibility = GONE
                    view.text_container.visibility = VISIBLE
                    view.wheel_view.visibility = GONE
                }
            }

        }
    }
}


