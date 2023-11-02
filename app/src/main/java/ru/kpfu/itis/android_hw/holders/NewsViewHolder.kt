package ru.kpfu.itis.android_hw.holders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.NewsModel
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.databinding.ItemNewsBinding

class NewsViewHolder (
    val viewBinding: ItemNewsBinding,
    private val onLikeClicked: ((Int, NewsModel.News) -> Unit),
    private val onDeleteClicked: ((NewsModel.News) -> Unit)
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: NewsModel.News? = null

    init {
        viewBinding.likeBtnIv.setOnClickListener {
            this.item?.let {
                val data = it.copy(isLiked = !it.isLiked)
                onLikeClicked(adapterPosition , data)
            }
        }
        viewBinding.root.setOnLongClickListener {
            viewBinding.deleteBtnIv.isVisible = !viewBinding.deleteBtnIv.isVisible
            true
        }
        viewBinding.deleteBtnIv.setOnClickListener {
            this.item?.let(onDeleteClicked)

        }

    }

    fun bindItem(item: NewsModel.News) {
        this.item = item
        with(viewBinding) {
            newsTitleTv.text = item.newsTitle
            item.newsDetails?.let { newsDetailsTv.text = it }
            item.newsImage?.let { res ->
                newsImageIv.setImageResource(res)
            }
            deleteBtnIv.isVisible = false
            changeLikeBtnStatus(isChecked = item.isLiked)

        }
    }


    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable =
            if (isChecked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        viewBinding.likeBtnIv.setImageResource(likeDrawable)
    }
}
