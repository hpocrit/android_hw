package ru.kpfu.itis.android_hw.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android_hw.BuildConfig
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.handlers.runCatching
import ru.kpfu.itis.android_hw.databinding.FragmentMainBinding
import ru.kpfu.itis.android_hw.di.Container

class MainFragment : Fragment(R.layout.fragment_main) {
    private var binding : FragmentMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding?.run{
            enterButton.setOnClickListener {
                val city = cityEt.text.toString()
                var temp = ""
                var id = ""

                if(city == BuildConfig.debugMenuKey && BuildConfig.DEBUG) {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, DebugFragment())
                        .commit()
                } else{
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching(Container.exceptionHandlerDelegate) {
                            withContext(Dispatchers.Main) {
                                loading.visibility = View.VISIBLE
                            }
                            temp = Container.weatherTemperatureUseCase(city)
                            id = Container.weatherRepository.getIconIdByCity(city)
                        }.onFailure{
                            temp = it.message.toString()
                        }.also {
                            withContext(Dispatchers.Main) {
                                loading.visibility = View.INVISIBLE
                                Toast.makeText(requireContext(), temp, Toast.LENGTH_SHORT).show()

                                Glide
                                    .with(requireContext())
                                    .load("https://openweathermap.org/img/w/$id.png")
                                    .into(iconIv)
                            }
                        }
                    }
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}