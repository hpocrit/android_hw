package ru.kpfu.itis.android_hw

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo.PRIORITY_DEFAULT
import android.app.job.JobInfo.PRIORITY_HIGH
import android.app.job.JobInfo.PRIORITY_LOW
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHandler {
    private var channelId: String? = null
    private var notificationId = 0

    fun sendNotification(title: String, text: String, importance: Int, visibility : Int, hide: Boolean, show: Boolean, ctx : Context, notificationChannelId : String, notificationChannelName: String){
        (ctx.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { manager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (channelId == null) {
                    channelId = notificationChannelId

                    val channel = NotificationChannel(
                        channelId,
                        notificationChannelName,
                        when (importance) {
                            1 -> NotificationManager.IMPORTANCE_LOW
                            2 -> NotificationManager.IMPORTANCE_DEFAULT
                            3 -> NotificationManager.IMPORTANCE_HIGH
                            else -> NotificationManager.IMPORTANCE_LOW
                        }
                    )
                    manager.createNotificationChannel(channel)
                } else {
                    val channel = manager.getNotificationChannel(channelId)
                    channel.importance = when (importance) {
                        1 -> NotificationManager.IMPORTANCE_LOW
                        2 -> NotificationManager.IMPORTANCE_DEFAULT
                        3 -> NotificationManager.IMPORTANCE_HIGH
                        else -> NotificationManager.IMPORTANCE_LOW
                    }
                    manager.createNotificationChannel(channel)
                }
            }

            val intent = Intent(ctx, MainActivity::class.java)

            val pendingIntent = PendingIntent.getActivity(
                ctx,
                100,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(ctx, channelId!!).apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle(title)
                setContentText(text)
                setContentIntent(pendingIntent)
                setVisibility(visibility)
                setPublicVersion(
                    NotificationCompat.Builder(
                        ctx,
                        channelId!!
                    )
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .build()
                )
                setAutoCancel(true)
                priority = when(importance) {
                    1 -> PRIORITY_LOW
                    2 -> PRIORITY_DEFAULT
                    3 -> PRIORITY_HIGH
                    else -> PRIORITY_DEFAULT
                }
                setVisibility(visibility)
                if (hide) {
                    setStyle(NotificationCompat.BigTextStyle().bigText(text))
                }
                if (show) {

                    val intentMain = Intent(ctx, MainActivity::class.java)
                    intentMain.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intentMain.putExtra("action", MainActivity.ACTION_MAIN)
                    val intentSettings = intent.putExtra("action", MainActivity.ACTION_SETTINGS)
                    val pendingIntentMain = PendingIntent.getActivity(ctx, 102, intentMain,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
                    val pendingIntentSettings = PendingIntent.getActivity(ctx, 103, intentSettings, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)


                    val actionMain = NotificationCompat.Action.Builder(
                        R.drawable.ic_launcher_foreground,
                        "Главная страница",
                        pendingIntentMain
                    ).build()
                    val actionSettings = NotificationCompat.Action.Builder(
                        R.drawable.ic_launcher_foreground,
                        "Настройки",
                        pendingIntentSettings
                    ).build()
                    addAction(actionMain)
                    addAction(actionSettings)
                }
            }
            manager.notify(notificationId++, notification.build())
        }
    }
}