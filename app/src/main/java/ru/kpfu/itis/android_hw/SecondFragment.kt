package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import org.junit.Test.None
import ru.kpfu.itis.android_hw.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {
    private var binding: FragmentSecondBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)

        binding?.run {
            if(arguments?.getString(SecondFragment.ARG_TEXT) != ""){
                titleTv.text = arguments?.getString(SecondFragment.ARG_TEXT)
            } else {
                titleTv.text = "Second Fragment"
            }

            toFirst.setOnClickListener{
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, FirstFragment())
                    .commit()
            }

            toThird.setOnClickListener {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, FirstFragment())
                    .commit()

                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ThirdFragment.newInstance(arguments?.getString(SecondFragment.ARG_TEXT).toString()))
                    .addToBackStack("first")
                    .commit()

            }

        }

    }

    companion object{

        private const val ARG_TEXT = "name_arg"

        fun newInstance(text: String) = SecondFragment().apply {
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