package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.android_hw.databinding.FragmentPagerViewBinding

class ViewPagerFragment : Fragment(R.layout.fragment_pager_view) {
    private val viewBinding: FragmentPagerViewBinding by viewBinding(FragmentPagerViewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cnt = arguments?.getInt(ARG_CNT).toString()
        viewBinding.viewPager.adapter = QuestionPagerAdapter(manager = parentFragmentManager, lifecycle, cnt.toInt())

        val circularViewPagerListener = ViewPagerListener(viewBinding.viewPager)

        viewBinding.viewPager.registerOnPageChangeCallback(circularViewPagerListener)

    }

    companion object{

        private const val ARG_CNT = "cnt_arg"

        fun newInstance(text: Int) = ViewPagerFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CNT, text)
            }
        }
    }

}