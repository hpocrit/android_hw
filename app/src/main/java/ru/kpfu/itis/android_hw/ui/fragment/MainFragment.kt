package ru.kpfu.itis.android_hw.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.LikeEntity
import ru.kpfu.itis.android_hw.data.entity.RatingEntity
import ru.kpfu.itis.android_hw.data.entity.SeriesEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity
import ru.kpfu.itis.android_hw.databinding.ActivityMainBinding
import ru.kpfu.itis.android_hw.databinding.FragmentMainBinding
import ru.kpfu.itis.android_hw.model.Model
import ru.kpfu.itis.android_hw.model.SeriesModel
import ru.kpfu.itis.android_hw.ui.adapter.SeriesAdapter
import java.io.Serializable
import java.util.concurrent.CompletableFuture

class MainFragment : Fragment(R.layout.fragment_main) {
    private var binding: FragmentMainBinding? = null
    private var seriesAdapter: SeriesAdapter? = null
    private var likeAdapter: SeriesAdapter? = null
    var userId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding?.run {
            val adapter: ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listOf("In ascending order of year", "In descending order of year", "In ascending order of rating", "In descending order of rating")
            )//ascending - возрастание

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            var order = 0

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    order = position
                    seriesAdapter?.updateDataSet(order)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    order = 0
                }
            }

            userId = ServiceLocator.getSharedPref().getString("USER_ID", "")

            if(userId != null) {
                var series: List<Model>
                var seriesEntity: List<SeriesEntity>?


                var flag =  lifecycleScope.async (Dispatchers.IO) {
                    seriesEntity =  ServiceLocator.getDbInstance().seriesDao.getAllSeries()


                    if(seriesEntity != null) {
                        series = seriesEntity!!.map { seriesEntity -> entityToModel(seriesEntity) }

                        val likesEntity = ServiceLocator.getDbInstance().likeDao.getLikeByUserId(userId!!)

                        when(order) {
                            0 -> series = series.sortedBy { (it as Model.SeriesModel).year }
                            1 -> series = series.sortedBy { (it as Model.SeriesModel).year }.reversed()
                            2 -> series = series.sortedBy { getRating((it as Model.SeriesModel)) }
                            else -> series = series.sortedBy { getRating((it as Model.SeriesModel)) }.reversed()
                        }

                        if (likesEntity?.isNotEmpty() == true && series[0] !is Model.FavoritesContainer) {
                            var localData = series.toMutableList()
                            localData.add(0, Model.FavoritesContainer)
                            series = localData.toList()
                        }

                        seriesAdapter = SeriesAdapter(
                            dataSet = series,
                            ctx = requireContext(),
                            onLikeClicked = ::onLikeClicked,
                            onDeleteClicked = ::onDeleteClicked,
                            isLiked = ::isLiked,
                            countRating = ::countRating,
                            listener = ::setListener,
                            getRating = ::getRating,
                            lifecycleScope = lifecycleScope
                        )

                        seriesRv.adapter = seriesAdapter

                        seriesRv.layoutManager = LinearLayoutManager(requireContext())
                        return@async true
                    }
                    return@async false
                }
                lifecycleScope.launch {
                    if(!flag.await()) {
                        makeToast("No series yet")
                    }
                }

            }


        }


    }

    private fun getRating(series: Model.SeriesModel) : Float{
        var result: Float = 0.toFloat()

        runBlocking {
            withContext(Dispatchers.IO) {
                result = ServiceLocator.getDbInstance().ratingDao.getRatingById(userId!!, series.id)?.rating
                    ?: 0.toFloat()
            }
        }

        return result
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val USER_ID = "id"
        fun newInstance(userId: String) = MainFragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId)
            }
        }
    }

    private fun onLikeClicked(position: Int, series: Model.SeriesModel ) {
        var likeId = ServiceLocator.getSharedPref().getInt("LIKE_ID", 10)
        var likeEntity: LikeEntity?
        lifecycleScope.launch(Dispatchers.IO) {
            likeEntity = ServiceLocator.getDbInstance().likeDao.getLikeById(userId!!, series.id)
            val likes = ServiceLocator.getDbInstance().likeDao.getLikeByUserId(userId!!)
            if(likeEntity == null) {
                ServiceLocator.getDbInstance().likeDao.addLikes(LikeEntity("id_${++likeId}", userId!!, series.id))
                ServiceLocator.getSharedPref().edit().putInt("LIKE_ID", likeId).apply()
            } else {
                ServiceLocator.getDbInstance().likeDao.deleteLikeById(userId!!, series.id)
            }

            if(likes == null) {
                var likesEntity = ServiceLocator.getDbInstance().likeDao.getLikeByUserId(userId!!)

//                if(likesEntity?.isNotEmpty() == true) {
//                    var likes = likesEntity!!.map { likesEntity -> entityToModel(ServiceLocator.getDbInstance().seriesDao.getSeriesInfoById(likesEntity)!!) }
//
//                    likeAdapter = SeriesAdapter(
//                        dataSet = likes,
//                        ctx = requireContext(),
//                        onLikeClicked = ::onLikeClicked,
//                        onDeleteClicked = ::onDeleteClicked,
//                        isLiked = ::isLiked,
//                        countRating = ::countRating,
//                        listener = ::setListener,
//                        getRating = ::getRating
//                    )
//
//                    binding?.likesRv?.adapter = likeAdapter
//
//                    binding?.likesRv?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//                }
            }
        }
        seriesAdapter?.updateById(series.id)
        seriesAdapter?.notifyItemChanged(0)
    }

    private fun onDeleteClicked(position: Int, item: Model.SeriesModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            ServiceLocator.getDbInstance().seriesDao.deleteSeriesById(item.id)
            ServiceLocator.getDbInstance().ratingDao.deleteRatingBySeriesId(item.id)
            ServiceLocator.getDbInstance().likeDao.deleteLikeBySeriesId(item.id)
        }
        seriesAdapter?.deleteById(item.id)
        seriesAdapter?.notifyItemChanged(0)
    }

    private fun isLiked(series: Model.SeriesModel) : Boolean {
        var likeEntity: LikeEntity? = null

        runBlocking {
            withContext(Dispatchers.IO) {
                likeEntity = ServiceLocator.getDbInstance().likeDao.getLikeById(userId!!, series.id)
            }
        }

        return likeEntity != null
    }

    private fun countRating(series: Model.SeriesModel) : Float {
        var ratings: List<RatingEntity>? = null
        runBlocking {
            withContext(Dispatchers.IO) {
                ratings = ServiceLocator.getDbInstance().ratingDao.getAllRatingBySeriesId(series.id)
            }
        }
        if(ratings == null) {
            return 0.toFloat()
        } else {
            val ratingList = ratings!!.map { rating -> rating.rating }
            return ratingList.average().toFloat()
        }
    }

    private fun setListener(series: Model.SeriesModel) {
        parentFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, DetailsFragment.newInstance(series.id, userId!!))
            .addToBackStack("details")
            .commit()
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bottom_navigation).visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}