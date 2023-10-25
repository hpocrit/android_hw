package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android_hw.databinding.FragmentQuestionBinding


class QuestionFragment : Fragment(R.layout.fragment_question) {
    private var binding: FragmentQuestionBinding? = null
    private var questions = QuestionsRepository.list

    private var optionsAdapter: OptionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater)
        return binding!!.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionBinding.bind(view)

        binding?.run {

            val position = arguments?.getInt("position")
            val cnt = arguments?.getInt("cnt")

            titleTv.text = position.toString()
            questionTv.text = questions[position!!-1].text



            questionTv.text = questions[position-1].text
            optionsAdapter = OptionsAdapter(
                questions[position-1].options,
                {position ->
                    updateChecked(position)
                    updateButton(button, cnt!!)
                },
                { position ->
                    updateChecked(position)
                    updateButton(button, cnt!!)
                }
            )

            optionsRv.adapter = optionsAdapter


            if(isAllChecked(cnt!!) && position >= cnt - 1) {
                button.visibility = View.VISIBLE
            }
            button.setOnClickListener {
                parentFragmentManager.
                beginTransaction().
                replace(R.id.container, StartFragment()).
                commit()
                Snackbar.make(view,getString(R.string.snackbar) , Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
    private fun updateChecked(position: Int) {
        optionsAdapter?.dataset?.let {
            it.forEach { option ->
                option.isChecked = false
            }
            it[position].isChecked = true
        }

        optionsAdapter!!.notifyDataSetChanged()
    }

    private fun isAllChecked(n: Int) : Boolean {
        var vis = true
        for (i in 1 until n){
            val question = questions[i]
            var loc_vis = false
            question.options.forEach {
                if(it.isChecked) {
                    loc_vis = true
                    return@forEach
                }
            }
            if(!loc_vis) {
                vis = false

            }

        }
        return vis
    }

    fun updateButton(button : Button, cnt : Int) {
        if(isAllChecked(cnt)) {
            button.visibility = View.VISIBLE
        }
    }


}