package ru.kpfu.itis.android_hw.ui.fragment

import android.media.Rating
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.LikeEntity
import ru.kpfu.itis.android_hw.data.entity.RatingEntity
import ru.kpfu.itis.android_hw.data.entity.SeriesEntity
import ru.kpfu.itis.android_hw.databinding.FragmentDetailsBinding
import ru.kpfu.itis.android_hw.model.SeriesModel
import ru.kpfu.itis.android_hw.ui.adapter.SeriesAdapter
import java.io.Serializable

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var binding: FragmentDetailsBinding? = null
    private var seriesId: String? = null
    private var userId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        binding?.run {
            seriesId = arguments?.getString(SERIES_ID)!!
            userId = arguments?.getString(USER_ID)!!

            var series = lifecycleScope.async(Dispatchers.IO) {
                return@async entityToModel(ServiceLocator.getDbInstance().seriesDao.getSeriesInfoById(seriesId!!)!!)
            }

            lifecycleScope.launch {
                var seriesModel = series.await()
                if(seriesModel != null) {
                    seriesModel.let {
                        Glide
                            .with(requireContext())
                            .load(it.image)
                            .into(image)
                        title.text = it.name + " (" + it.year + ")"
                        summary.text = it.summary
                        val rat =  findRating(userId!!)
                        like.setImageResource(changeLikeBtnStatus(isLiked(it)))
                        ratingBar.rating = rat
                        valueRating.text = rat.toString().subSequence(0, 3)
                    }

                }
            }

            ratingBar.setOnRatingBarChangeListener { ratingBr, rat, fromUser ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val seriesModel = series.await()
                    updateRating(seriesModel, rat)
                }
                ratingBar.rating = rat
                valueRating.text = rat.toString().subSequence(0, 3)
            }

        }
    }

    private fun isLiked(series: SeriesModel) : Boolean {
        var likeEntity: LikeEntity? = null

        runBlocking {
            withContext(Dispatchers.IO) {
                likeEntity = ServiceLocator.getDbInstance().likeDao.getLikeById(userId!!, series.id)
            }
        }

        return likeEntity != null
    }

    fun changeLikeBtnStatus(isChecked: Boolean) : Int {
        if (isChecked) return R.drawable.baseline_favorite_24 else return R.drawable.baseline_favorite_border_24
    }
    companion object{

        const val SERIES_ID = "id"
        const val USER_ID = "userId"

        fun newInstance(seriesId: String, userId: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(SERIES_ID, seriesId)
                putString(USER_ID, userId)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun findRating(userId: String) : Float{
        val rating= 0.toFloat()
        lifecycleScope.launch(Dispatchers.IO) {
            ServiceLocator.getDbInstance().ratingDao.getRatingById(userId, seriesId!!)
        }
        return rating
    }

    private fun updateRating(series: SeriesModel, newRating: Float) {
        var ratingId = ServiceLocator.getSharedPref().getInt("RATING_ID", 10)
        var rating: RatingEntity? = null
        lifecycleScope.launch(Dispatchers.IO) {
            rating = ServiceLocator.getDbInstance().ratingDao.getRatingById(userId!!, series.id)
            if(rating == null) {
                ServiceLocator.getDbInstance().ratingDao.addRating(RatingEntity("id_${++ratingId}", userId!!, series.id, newRating))
                ServiceLocator.getSharedPref().edit().putInt("RATING_ID", ratingId).apply()
            } else {
                if(newRating == 0.toFloat()) {
                    ServiceLocator.getDbInstance().ratingDao.deleteRatingById(rating!!.id)
                } else {
                    ServiceLocator.getDbInstance().ratingDao.updateRaring(rating!!.id, newRating)
                }
            }
        }
//        seriesAdapter?.notifyItemChanged(position)
    }

    fun entityToModel(entity: SeriesEntity) : SeriesModel {
        return SeriesModel(
            entity.id,
            entity.name,
            entity.year,
            entity.image,
            entity.summary
        )
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bottom_navigation).visibility = View.VISIBLE
    }
}