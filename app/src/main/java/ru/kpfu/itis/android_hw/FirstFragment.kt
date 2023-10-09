package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android_hw.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {

    private var binding: FragmentFirstBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        binding?.run {


            enter.setOnClickListener {
                val editText = inputEt.text.toString()

                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, SecondFragment.newInstance(editText))
                    .addToBackStack("first")
                    .commit()

                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, ThirdFragment.newInstance(editText))
                    .addToBackStack("first")
                    .commit()
            }

            save.setOnClickListener {

                val editText = inputEt.text.toString()

                if(editText != "") {
                    setFragmentResult("requestKey", bundleOf("bundleKey" to editText))
                } else {
                    Snackbar.make(view, "Text field is empty", Snackbar.LENGTH_SHORT).show()

                }
                inputEt.text?.clear()

            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}