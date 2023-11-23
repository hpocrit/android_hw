package ru.kpfu.itis.android_hw

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import ru.kpfu.itis.android_hw.databinding.FragmentMainBinding

// TODO: String в ресурсы

class MainFragment : Fragment(R.layout.fragment_main) {
    private var binding: FragmentMainBinding? = null
    private var notificationHandler = NotificationHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding?.run {
            button.setOnClickListener {
                val title = notificationTitleEt.text.toString()
                val text = notificationTextEt.text.toString()


                (requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { manager ->

                    var importance_ = 1
                    var visibility_ = NotificationCompat.VISIBILITY_PUBLIC
                    var hide_ = false
                    var show_ = false

                    setFragmentResultListener("request_key") { key, bundle ->
                        val importance = bundle.getInt("importance")
                        val visibility = bundle.getInt("visibility")
                        val hide = bundle.getBoolean("hide")
                        val show = bundle.getBoolean("show")

                        visibility_ = when (visibility) {
                            1 -> NotificationCompat.VISIBILITY_PUBLIC
                            2 -> NotificationCompat.VISIBILITY_SECRET
                            3 -> NotificationCompat.VISIBILITY_PRIVATE
                            else -> NotificationCompat.VISIBILITY_PUBLIC
                        }
                        importance_ = importance
                        hide_ = hide
                        show_ = show

                    }

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    val intent2 = intent.putExtra(intentName, intentValue)
                    val pendingIntent = PendingIntent.getActivity(requireContext(), 102, intent,PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
                    val pendingIntent2 = PendingIntent.getActivity(requireContext(), 103, intent2, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

                    notificationHandler.sendNotification(title, text, importance_, visibility_, hide_, show_, requireContext(), "first_notification", "Канал уведомлений")

                }
            }
            val airplaneModeHandler = AirplaneMode(requireContext())
            airplaneModeHandler.handle {
                button.isEnabled = !it
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    companion object {
        const val intentName = "fragment"
        const val intentValue = "notificationSettings"
    }
}