package ru.kpfu.itis.android_hw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.databinding.ItemOptionBinding


class OptionsAdapter(
    val dataset: List<Option>,
    private val onItemChecked: (Int) -> Unit,
    private var onRootClicked: (Int) -> Unit)
    :  RecyclerView.Adapter<OptionItem>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionItem {
        return OptionItem(
            ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemChecked,
            onRootClicked)
    }

    override fun onBindViewHolder(holder: OptionItem, position: Int) {
        holder.onBind(dataset[position])
    }

    override fun getItemCount() = dataset.size



}