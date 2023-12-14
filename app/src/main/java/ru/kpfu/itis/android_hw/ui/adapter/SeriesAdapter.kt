package ru.kpfu.itis.android_hw.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android_hw.databinding.ItemSeriesBinding
import ru.kpfu.itis.android_hw.model.SeriesModel
import ru.kpfu.itis.android_hw.ui.holder.SeriesHolder

class SeriesAdapter(
    private var dataSet: List<SeriesModel>,
    private val ctx: Context,
    private val onLikeClicked: ((Int, SeriesModel) -> Unit),
    private val onDeleteClicked: ((Int, SeriesModel) -> Unit),
    private val isLiked: ((SeriesModel) -> Boolean),
    private val countRating: ((SeriesModel) -> Float),
    private val listener: ((SeriesModel) -> Unit),
    private val getRating: ((SeriesModel) -> Float)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SeriesHolder {
        return SeriesHolder(
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
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SeriesHolder -> {
                holder.bindItem(dataSet[position])
                holder.viewBinding.seriesImage.setOnClickListener {
                    listener(dataSet[position])
                }
            }
        }
    }

    fun updateDataSet(int: Int) {
        when(int) {
            0 -> dataSet = dataSet.sortedBy { it.year }
            1 -> dataSet = dataSet.sortedBy { it.year }.reversed()
            2 -> dataSet = dataSet.sortedBy { getRating(it) }
            else -> dataSet = dataSet.sortedBy { getRating(it) }.reversed()
        }
        notifyDataSetChanged()
    }

    fun deleteById(id: String) {
        for(i in dataSet.indices) {
            if(dataSet[i].id == id) {
                var localData = dataSet.toMutableList()
                localData.removeAt(i)
                dataSet = localData.toList()
                notifyDataSetChanged()
                return
            }
        }
    }

    fun updateById(id: String) {
        for(i in dataSet.indices) {
            if(dataSet[i].id == id) {
                notifyItemChanged(i)
                return
            }
        }
    }

    fun updateLikeById(series: SeriesModel) {
        for(i in dataSet.indices) {
            if(dataSet[i].id == series.id) {
                var localData = dataSet.toMutableList()
                localData.removeAt(i)
                dataSet = localData.toList()
                notifyDataSetChanged()
                return
            }
        }
        var localData = dataSet.toMutableList()
        localData.add(series)
        dataSet = localData.toList()
        notifyDataSetChanged()

    }


    override fun getItemCount() = dataSet.size
}