package ru.kpfu.itis.android_hw.ui.holder

import android.content.Context
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.databinding.ItemSeriesBinding
import ru.kpfu.itis.android_hw.model.SeriesModel

class SeriesHolder (
    val viewBinding: ItemSeriesBinding,
    private val onLikeClicked: ((Int, SeriesModel) -> Unit),
    private val onDeleteClicked: ((Int, SeriesModel) -> Unit),
    private val ctx: Context,
    private val isLiked: ((SeriesModel) -> Boolean),
    private val countRating: ((SeriesModel) -> Float),
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: SeriesModel? = null

    init {
        viewBinding.likeBtnIv.setOnClickListener {
            this.item?.let {
//                val data = it.copy(isLiked = !it.isLiked)
                onLikeClicked(adapterPosition , it)
            }
        }
        viewBinding.root.setOnLongClickListener {
            viewBinding.deleteBtnIv.isVisible = !viewBinding.deleteBtnIv.isVisible
            true
        }
        viewBinding.deleteBtnIv.setOnClickListener {
            this.item?.let{onDeleteClicked(adapterPosition, it)}
        }
    }

    fun bindItem(item: SeriesModel) {
        this.item = item
        with(viewBinding) {
            title.text = item.name + " (" + item.year + ")"
            item.summary.let { summary.text = it }
            item.image.let {
                Glide
                    .with(ctx)
                    .load(it)
                    .into(seriesImage)
            }
            deleteBtnIv.isVisible = false
            val fl = isLiked(item)
            changeLikeBtnStatus(isChecked = fl)
            val rat = countRating(item)
            rating.rating = rat
            cntRating.text = if ( rat == 0.toFloat()) "0.0" else rat.toString().subSequence(0, 3)
        }
    }


    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable =
            if (isChecked) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        viewBinding.likeBtnIv.setImageResource(likeDrawable)
    }

}