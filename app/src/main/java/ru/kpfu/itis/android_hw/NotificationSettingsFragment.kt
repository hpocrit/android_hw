package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android_hw.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {
    private var binding : FragmentNotificationSettingsBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationSettingsBinding.bind(view)

        binding?.run {
            var importance = 1
            var visibility = 1
            var show = false
            var hide = false

            // importance
            group1.setOnCheckedChangeListener { group, checkedId ->
                importance = when(checkedId) {
                    radioMedium.id -> 1
                    radioHigh.id -> 2
                    radioUrgent.id -> 3
                    else -> 1
                }
                sendResult(importance, visibility, show, hide)
            }

            //visibility
            group2.setOnCheckedChangeListener { group, checkedId ->
                visibility = when(checkedId) {
                    radioPublic.id -> 1
                    radioSecret.id -> 2
                    radioPrivate.id -> 3
                    else -> 1
                }
                sendResult(importance, visibility, show, hide)
            }

            checkboxHide.setOnCheckedChangeListener { buttonView, isChecked ->
                hide = isChecked
                sendResult(importance, visibility, show, hide)
            }

            checkboxButton.setOnCheckedChangeListener { buttonView, isChecked ->
                show = isChecked
                sendResult(importance, visibility, show, hide)
            }

        }
    }

    fun sendResult(importance : Int, visibility : Int, show : Boolean, hide : Boolean) {
        parentFragmentManager.setFragmentResult(
            "request_key",
            bundleOf(
                "importance" to importance,
                "visibility" to visibility,
                "hide" to hide,
                "show" to show
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    
    
}