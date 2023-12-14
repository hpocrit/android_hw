package ru.kpfu.itis.android_hw.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.dialogFragmentViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.SeriesEntity
import ru.kpfu.itis.android_hw.databinding.FragmentNewSeriesBinding

class NewSeriesFragment : Fragment(R.layout.fragment_new_series) {
    private var binding : FragmentNewSeriesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewSeriesBinding.bind(view)

        binding?.run {
            add.setOnClickListener {
                val title = name.text.toString()
                val year = year.text.toString()
                val image = image.text.toString()
                val summary = summary.text.toString()

                var cnt = ServiceLocator.getSharedPref().getInt("SERIES_ID", 10)

                if(title.isNotEmpty() && year.isNotEmpty() && image.isNotEmpty()){
                    var flag = false
                    var job = lifecycleScope.launch (Dispatchers.IO) {
                        val series = getAllSeries()
                        series?.forEach {
                            if(it.name == title && it.year == year.toInt()) {
                                flag = true
                            }
                        }
                    }
                    lifecycleScope.launch {
                        job.join()
                        if(!flag) {
                            withContext(Dispatchers.IO) {ServiceLocator.getDbInstance().seriesDao.addSeries(SeriesEntity("id_${++cnt}", title, year.toInt(), image, summary))}
                            ServiceLocator.getSharedPref().edit().putInt("SERIES_ID", cnt).apply()
                        } else {
                            makeToast("Such element already exists")
                        }
                    }
                } else {
                    makeToast("Some fields are empty")
                }
            }
        }
    }

    fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bottom_navigation).visibility = View.VISIBLE
    }

    suspend fun getAllSeries() : List<SeriesEntity>? {
        return lifecycleScope.async(Dispatchers.IO) {
            return@async ServiceLocator.getDbInstance().seriesDao.getAllSeries()
        }.await()
    }

}