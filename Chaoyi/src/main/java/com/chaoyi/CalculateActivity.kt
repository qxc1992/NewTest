package com.chaoyi

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chaoyi.login.LoginActivity
import com.db.chart.animation.Animation
import com.db.chart.model.BarSet
import com.db.chart.renderer.AxisRenderer
import kotlinx.android.synthetic.main.calculate.*
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * Created by yupenglei on 2018/5/9.
 */
class CalculateActivity : AppCompatActivity() {
    private val mValuesOne = floatArrayOf(0f, 0f, 0f, 0f, 2f, 10f, 2f)
    private val mValuesTwo = FloatArray(7)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT

        setStatusBarColor(this, true)
        setContentView(R.layout.calculate)

        fit_chart.minValue = 0f
        fit_chart.maxValue = 60f
        fit_chart.setValue(Integer.parseInt(tv_today_percent.text.toString()).toFloat())





        tb_calculate_toolbar.title = "今日关注"
        tb_calculate_toolbar.setTitleTextColor(android.graphics.Color.BLACK)
        tb_calculate_toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left_big)
        tb_calculate_toolbar.setNavigationOnClickListener { finish() }
        tb_calculate_toolbar.inflateMenu(R.menu.menu_calculate)
        tb_calculate_toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.login) {
                startActivity<LoginActivity>()
            }
            true
        }

        Arrays.fill(mValuesTwo, 0.6f)
        val stackBarSetTwo = BarSet(getLastSevenday().toTypedArray(), mValuesTwo)
//        stackBarSetTwo.color = resources.getColor(R.color.colorAccent)
        mValuesOne.forEachIndexed { index, fl ->
            if (fl == 0f) stackBarSetTwo.getEntry(index).color = resources.getColor(R.color.color_gray_chart)
            else stackBarSetTwo.getEntry(index).color = resources.getColor(R.color.colorAccent)
        }

        val stackBarSetOne = BarSet(getLastSevenday().toTypedArray(), mValuesOne)
        stackBarSetOne.color = resources.getColor(R.color.colorAccent)

        calculate_chart.addData(stackBarSetTwo)
        calculate_chart.addData(stackBarSetOne)
        calculate_chart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setYLabels(AxisRenderer.LabelPosition.NONE)
                .setYAxis(false)
                .setLabelsColor(Color.parseColor("#ff8C8C8C"))
                .setAxisColor(Color.parseColor("#1e000000"))
                .show(Animation().inSequence(.5f))


    }

}