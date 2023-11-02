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
            editText.addTextChangedListener {
                if(editText.text.toString().isEmpty()) {
                    editText.error = "Поле не может быть пустым"
                }else {
                    if(editText.text.toString().toInt() > 45) {
                        editText.error = "Число должно быть меньше 45"
                    } else {
                        editText.error = null
                    }
                }
            }
            button.setOnClickListener {
                val cnt = editText.text.toString().toInt()
                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, NewsFragment.newInstance( cnt))
                    .addToBackStack("start")
                    .commit()
            }
        }

    }
}