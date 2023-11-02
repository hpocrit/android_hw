package ru.kpfu.itis.android_hw

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.databinding.ItemButtonBinding
import ru.kpfu.itis.android_hw.databinding.ItemDateBinding
import ru.kpfu.itis.android_hw.databinding.ItemNewsBinding
import ru.kpfu.itis.android_hw.holders.ButtonViewHolder
import ru.kpfu.itis.android_hw.holders.DateViewHolder
import ru.kpfu.itis.android_hw.holders.NewsViewHolder


class NewsAdapter(
    private val onLikeClicked: ((Int, NewsModel.News) -> Unit),
    private var cnt: Int,
    private val fragmentManager: FragmentManager,
    private val onDeleteClicked: ((NewsModel.News) -> Unit),
    private val activity : Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var newsList = NewsRepository.getNews(cnt)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == R.layout.item_news) {
            return NewsViewHolder(
                viewBinding = ItemNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onLikeClicked = onLikeClicked,
                onDeleteClicked = onDeleteClicked
            )
        } else if (viewType == R.layout.item_button) {
            return ButtonViewHolder(
                viewBinding = ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                this,
                fragmentManager
            )
        } else {
            return DateViewHolder(
                viewBinding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                holder.bindItem(newsList[position] as NewsModel.News)
                val ivImage = holder.viewBinding.newsImageIv
                ivImage.setOnClickListener {
                    (activity as MainActivity).showDetail(ivImage, (newsList[position] as NewsModel.News).newsId)
                }

                ivImage.transitionName = "kitten_$position"
            }
            is ButtonViewHolder -> holder.bindItem()
            is DateViewHolder -> holder.bindItem()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? NewsViewHolder)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = newsList.size


    fun updateItem(position: Int, item: NewsModel.News) {
        newsList[position] = item
        notifyItemChanged(position, item.isLiked)
    }

    override fun getItemViewType(position: Int): Int {
        return when (newsList[position]) {
            is NewsModel.NewsButton -> R.layout.item_button
            is NewsModel.News -> R.layout.item_news
            is NewsModel.NewsDate -> R.layout.item_date
        }
    }

    fun setCnt(n : Int) {
        cnt += n
    }
}
