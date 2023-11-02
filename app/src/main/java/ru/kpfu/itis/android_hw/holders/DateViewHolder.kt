package ru.kpfu.itis.android_hw.holders

import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.databinding.ItemDateBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateViewHolder(
    private val viewBinding: ItemDateBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: TextView? = null
    private var cnt = 1


    fun bindItem() {
        viewBinding.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Date())
        this.item = viewBinding.date
        cnt ++
    }
}