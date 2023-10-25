package ru.kpfu.itis.android_hw


import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class ViewPagerListener(private val viewPager: ViewPager2) : ViewPager2.OnPageChangeCallback() {
    private var previousState: Int = ViewPager2.SCROLL_STATE_IDLE
    private var scrollState: Int = ViewPager2.SCROLL_STATE_IDLE

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {
        previousState = scrollState
        scrollState = state


        if (previousState == ViewPager2.SCROLL_STATE_DRAGGING && scrollState == ViewPager2.SCROLL_STATE_IDLE) {
            val currentItem = viewPager.currentItem
            val itemCount = viewPager.adapter?.itemCount ?: 0

            if (currentItem == itemCount - 1) {
                viewPager.setCurrentItem(0, true)
            }
            else if (currentItem == 0) {
                viewPager.setCurrentItem(itemCount - 1, true)
            }
        }
    }
}