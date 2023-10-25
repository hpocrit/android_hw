package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android_hw.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private var binding: FragmentStartBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        binding?.run {
            button.isEnabled = false

            val phoneMaskTextWatcher = PhoneMaskTextWatcher(phoneEt)

            var op_1 = false
            var op_2 = false

            phoneEt.addTextChangedListener {
                if (phoneEt.text!!.length >= 17) {
                    op_1 = true
                }
                button.isEnabled = op_1 && op_2
            }

            cntEt.addTextChangedListener {
                if (cntEt.text!!.isNotEmpty()) {
                    op_2 = true
                }
                button.isEnabled = op_1 && op_2
            }
            if(op_1 && op_2) {
                button.isEnabled = true
            }

            button.setOnClickListener {
                val cnt = cntEt.text.toString().toInt()
                if(cnt in 1..14) {
                    cntEt.error = null
                    parentFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.container, ViewPagerFragment.newInstance(cnt))
                        .addToBackStack("start")
                        .commit()
                } else {
                    cntEt.error = getString(R.string.error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}