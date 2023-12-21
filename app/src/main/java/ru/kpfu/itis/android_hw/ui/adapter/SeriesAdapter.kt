package ru.kpfu.itis.android_hw.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.databinding.ItemLikedBinding
import ru.kpfu.itis.android_hw.databinding.ItemSeriesBinding
import ru.kpfu.itis.android_hw.model.Model
import ru.kpfu.itis.android_hw.model.SeriesModel
import ru.kpfu.itis.android_hw.ui.holder.LikeHolder
import ru.kpfu.itis.android_hw.ui.holder.SeriesHolder

class SeriesAdapter(
    private var dataSet: List<Model>,
    private val ctx: Context,
    private val onLikeClicked: ((Int, Model.SeriesModel) -> Unit),
    private val onDeleteClicked: ((Int, Model.SeriesModel) -> Unit),
    private val isLiked: ((Model.SeriesModel) -> Boolean),
    private val countRating: ((Model.SeriesModel) -> Float),
    private val listener: ((Model.SeriesModel) -> Unit),
    private val getRating: ((Model.SeriesModel) -> Float),
    private val lifecycleScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_series -> SeriesHolder(
            viewBinding = ItemSeriesBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            ),
            onLikeClicked = onLikeClicked,
            onDeleteClicked = onDeleteClicked,
            ctx = ctx,
            isLiked = isLiked,
            countRating = countRating
        )
        else -> LikeHolder(
            viewBinding = ItemLikedBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            ),
            onLikeClicked = onLikeClicked,
            onDeleteClicked = onDeleteClicked,
            ctx = ctx,
            isLiked = isLiked,
            countRating = countRating,
            listener = listener,
            lifecycleScope = lifecycleScope,
            getRating = getRating
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SeriesHolder -> {
                holder.bindItem(dataSet[position] as Model.SeriesModel)
                holder.viewBinding.seriesImage.setOnClickListener {
                    listener(dataSet[position] as Model.SeriesModel)
                }
            }
            is LikeHolder -> {
                holder.bindItem()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position]) {
            is Model.SeriesModel -> R.layout.item_series
            is Model.FavoritesContainer -> R.layout.item_liked
        }
    }

    fun updateDataSet(int: Int) {
        var start = 0
        if(dataSet[0] is Model.FavoritesContainer) { start = 1 }
        when(int) {
            0 -> dataSet = dataSet.subList(start, dataSet.size).sortedBy { (it as Model.SeriesModel).year }
            1 -> dataSet = dataSet.subList(start, dataSet.size).sortedBy { (it as Model.SeriesModel).year }.reversed()
            2 -> dataSet = dataSet.subList(start, dataSet.size).sortedBy { getRating((it as Model.SeriesModel)) }
            else -> dataSet = dataSet.subList(start, dataSet.size).sortedBy { getRating((it as Model.SeriesModel)) }.reversed()
        }
        notifyDataSetChanged()
    }

    fun deleteById(id: String) {
        for(i in dataSet.indices) {
            if((dataSet[i] as Model.SeriesModel).id == id) {
                var localData = dataSet.toMutableList()
                localData.removeAt(i)
                dataSet = localData.toList()
                notifyItemRemoved(i)
                return
            }
        }
    }

    fun updateById(id: String) {
        for(i in dataSet.indices) {
            if((dataSet[i] as Model.SeriesModel).id == id) {
                notifyItemChanged(i)
                return
            }
        }
    }

    fun updateLikeById(series: Model.SeriesModel) {
        for(i in dataSet.indices) {
            if((dataSet[i] as Model.SeriesModel).id == (series).id) {
                var localData = dataSet.toMutableList()
                localData.removeAt(i)
                dataSet = localData.toList()
                notifyItemRemoved(i)
                return
            }
        }
        var localData = dataSet.toMutableList()
        localData.add(series)
        dataSet = localData.toList()
        notifyItemInserted(dataSet.size-1)

    }


    override fun getItemCount() = dataSet.size
}