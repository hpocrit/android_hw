package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import ru.kpfu.itis.android_hw.databinding.FragmentFourthBinding

class FourthFragment : Fragment(R.layout.fragment_fourth) {

    private var binding: FragmentFourthBinding? = null
    private var cnt = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFourthBinding.bind(view)

        binding?.run {

            setFragmentResultListener("requestKey") { _, bundle ->
                val result = bundle.getString("bundleKey")
                if(cnt % 3 == 0) {
                    title1Tv.text = result
                    cnt += 1
                } else if(cnt % 3 == 1) {
                    title2Tv.text = result
                    cnt += 1
                } else if(cnt % 3 == 2) {
                    title3Tv.text = result
                    cnt += 1
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}