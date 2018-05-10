package com.newtest

import android.support.design.widget.AppBarLayout

/**
 * Created by yupenglei on 2017/9/6.
 */

abstract class AppBarStateChangeListenerd : AppBarLayout.OnOffsetChangedListener {

    private var mCurrentState = State.IDLE

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, verticalOffset)
            }
            mCurrentState = State.EXPANDED
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, verticalOffset)
            }
            mCurrentState = State.COLLAPSED
        } else {
            onStateChanged(appBarLayout, State.IDLE, verticalOffset)
            mCurrentState = State.IDLE
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State, verticalOffset: Int)
}
