package ru.kpfu.itis.android_hw

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class QuestionPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle, val cnt : Int) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int {
        return cnt
    }

    override fun createFragment(position: Int): Fragment {
        val questionFragment = QuestionFragment()
        val arg = Bundle()
        arg.putInt("position", position + 1)
        arg.putInt("cnt", cnt)
        questionFragment.arguments = arg
        return questionFragment
    }
}