package ru.kpfu.itis.android_hw.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android_hw.BuildConfig
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.databinding.FragmentDebugBinding

class DebugFragment : Fragment(R.layout.fragment_debug) {
    private var binding : FragmentDebugBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDebugBinding.bind(view)

        binding?.run {
            val packageInfo = context?.packageManager?.getPackageInfo(requireContext().packageName, 0)
            val versionName = packageInfo?.versionName
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo?.longVersionCode
            } else {
                packageInfo?.versionCode?.toLong()
            }

            appNameTv.text = getString(R.string.app_name)
            baseUrlTv.text = BuildConfig.OPEN_WEATHER_BASE_URL
            versionNameTv.text = versionName
            versionCodeTv.text = versionCode.toString()
            manufacturerTv.text = Build.MANUFACTURER
            modelTv.text = Build.MODEL
            sdkVersionTv.text = Build.VERSION.SDK_INT.toString()
            androidVersionTv.text = Build.VERSION.RELEASE

        }
    }
}