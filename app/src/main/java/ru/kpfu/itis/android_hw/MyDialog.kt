package ru.kpfu.itis.android_hw

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment

class MyDialog(private val ctx: Context) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Включите уведомления")
                .setMessage("Для полноценной работы приложения необходимо включить уведомления. Нажмите кнопку ниже, чтобы перейти в настройки приложения и включить уведомления.")
                .setPositiveButton("Настройки") { _, _ ->
                    openAppSettings(ctx)
                }
                .setCancelable(false)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

}
