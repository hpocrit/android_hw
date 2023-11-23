package ru.kpfu.itis.android_hw

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android_hw.databinding.FragmentCoroutineSettingsBinding

class CoroutineSettingsFragment : Fragment(R.layout.fragment_coroutine_settings) {
    private var binding: FragmentCoroutineSettingsBinding? = null
    private var coroutineScope: CoroutineScope? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoroutineSettingsBinding.bind(view)

        binding?.run {

            button.setOnClickListener {
                val cnt = seekbar.progress
                val isAsync = async.isChecked
                val isInBack = background.isChecked

                CoroutineScope(Dispatchers.IO).launch {
                    val coroutines = mutableListOf<Deferred<Unit>>()

                    for (i in 1..cnt) {
                        val coroutine = if (isAsync) {
                            async { performTask(i) }
                        } else {
                            async(start = CoroutineStart.LAZY) { performTask(i) }
                        }
                        coroutines.add(coroutine)
                    }

                    if (isInBack) {
                        // Обрабатываем случай сворачивания приложения
                        while (isActive) {
                            if (!isOnBackground()) {
                                for (coroutine in coroutines) {
                                    if (!coroutine.isCompleted) {
                                        coroutine.start()
                                    }
                                }
                            } else {
                                // Завершаем все активные корутины
                                coroutines.forEach { it.cancel() }
                                break
                            }
                            delay(1000)
                        }
                    } else {
                        // Если не требуется остановка на сворачивании приложения,
                        // запускаем все корутины сразу
                        coroutines.forEach { it.start() }
                    }

                    // Ожидаем завершения всех корутин
                    coroutines.awaitAll()

                    withContext(Dispatchers.Main) {
                        showNotification()
                    }
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
        return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCoroutineSettingsBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onPause() {
        super.onPause()
        coroutineScope?.cancel() // Отменяем корутины при сворачивании активити
    }

    private suspend fun performTask(taskId: Int) {
        // Имитация работы в течение некоторого времени
        delay(1000)
        withContext(Dispatchers.Main) {
            Log.d("Coroutine", "Task $taskId completed")
        }
    }


    private fun isOnBackground(): Boolean {
        // Проверка, находится ли приложение на заднем плане
        val appProcessInfo =  ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
    }

    private fun showNotification() {
        (requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { manager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "first_notification1",
                    "Канал Уведомлений",
                    NotificationManager.IMPORTANCE_LOW
                )
                manager.createNotificationChannel(channel)

                val notification = NotificationCompat.Builder(requireContext(), "first_notification1")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Notification")
                    .setContentText("My job here is done")
                manager.notify(1, notification.build())
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}