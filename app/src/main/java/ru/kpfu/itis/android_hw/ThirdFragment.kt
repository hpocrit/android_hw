package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android_hw.databinding.FragmentThirdBinding

class ThirdFragment : Fragment(R.layout.fragment_third) {
    private var binding: FragmentThirdBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdBinding.bind(view)


        binding?.run {
            if(arguments?.getString(ARG_TEXT) != ""){
                titleTv.text = arguments?.getString(ARG_TEXT)
            } else {
                titleTv.text = "Third Fragment"
            }
        }

    }

    companion object{

        private const val ARG_TEXT = "name_arg"

        fun newInstance(text: String) = ThirdFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TEXT, text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}