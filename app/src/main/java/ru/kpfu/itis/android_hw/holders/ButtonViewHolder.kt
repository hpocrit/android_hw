package ru.kpfu.itis.android_hw.holders

import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.BottomSheetFragment
import ru.kpfu.itis.android_hw.NewsAdapter
import ru.kpfu.itis.android_hw.databinding.ItemButtonBinding

class ButtonViewHolder(
    private val viewBinding: ItemButtonBinding,
    private val adapter: NewsAdapter,
    private val fragmentManager: FragmentManager,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: AppCompatButton? = null

    init {
        viewBinding.button.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment(adapter)
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }
    }
    fun bindItem() {
        this.item = viewBinding.button
    }
}
