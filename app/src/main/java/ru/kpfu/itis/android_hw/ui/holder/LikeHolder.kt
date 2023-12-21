package ru.kpfu.itis.android_hw.ui.holder

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.SeriesEntity
import ru.kpfu.itis.android_hw.databinding.ItemLikedBinding
import ru.kpfu.itis.android_hw.databinding.ItemSeriesBinding
import ru.kpfu.itis.android_hw.model.Model
import ru.kpfu.itis.android_hw.model.SeriesModel
import ru.kpfu.itis.android_hw.ui.adapter.SeriesAdapter

class LikeHolder (
    val viewBinding: ItemLikedBinding,
    private val onLikeClicked: ((Int, Model.SeriesModel) -> Unit),
    private val onDeleteClicked: ((Int, Model.SeriesModel) -> Unit),
    private val ctx: Context,
    private val isLiked: ((Model.SeriesModel) -> Boolean),
    private val countRating: ((Model.SeriesModel) -> Float),
    private val listener: ((Model.SeriesModel) -> Unit),
    private val lifecycleScope: LifecycleCoroutineScope,
    private val getRating: ((Model.SeriesModel) -> Float),
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var likeAdapter: SeriesAdapter? = null

    fun bindItem() {
        var likes: List<Model.SeriesModel>
        var likesEntity: List<String>?

        lifecycleScope.launch (Dispatchers.IO) {

            val userId = ServiceLocator.getSharedPref().getString("USER_ID", "")

            likesEntity = ServiceLocator.getDbInstance().likeDao.getLikeByUserId(userId!!)

            if (likesEntity?.isNotEmpty() == true) {
                likes = likesEntity!!.map { likesEntity ->
                    entityToModel(
                        ServiceLocator.getDbInstance().seriesDao.getSeriesInfoById(likesEntity)!!
                    )
                }

                likeAdapter = SeriesAdapter(
                    dataSet = likes,
                    ctx = ctx,
                    onLikeClicked = onLikeClicked,
                    onDeleteClicked = onDeleteClicked,
                    isLiked = isLiked,
                    countRating = countRating,
                    listener = listener,
                    getRating = getRating,
                    lifecycleScope = lifecycleScope
                )

                viewBinding.likesRv.adapter = likeAdapter

                viewBinding.likesRv.layoutManager =
                    LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    fun entityToModel(entity: SeriesEntity) : Model.SeriesModel {
        return Model.SeriesModel(
            entity.id,
            entity.name,
            entity.year,
            entity.image,
            entity.summary
        )
    }

}