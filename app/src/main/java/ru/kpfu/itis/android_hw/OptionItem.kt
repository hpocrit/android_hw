package ru.kpfu.itis.android_hw

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.databinding.ItemOptionBinding

class OptionItem(
    private val viewBinding: ItemOptionBinding,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit,)
    : RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.radioButton.setOnClickListener{
                onItemChecked.invoke(adapterPosition)
            }
            viewBinding.root.setOnClickListener {
                onRootClicked.invoke(adapterPosition)
            }
        }

    fun onBind(option: Option) {
        with(viewBinding) {
            textView.text = option.text
            radioButton.isChecked = option.isChecked
            radioButton.isEnabled = !option.isChecked
            if(option.isChecked) {
                root.foreground = null
            }
        }
    }

}