package ru.kpfu.itis.android_hw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android_hw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var cnt = 0
    private lateinit var flightModeWarningView: View
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val airplaneOnView: RelativeLayout = findViewById(R.id.networkErrorLayout)
        val airplaneModeHandler = AirplaneMode(this)
        airplaneModeHandler.handle {
            airplaneOnView.isGone = !it
        }

        val action = intent.getIntExtra("action", ACTION_NO)
        onIntentAction(action)

        requestPermission()

        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        NavigationUI.setupWithNavController(viewBinding.mainBottomNavigation, navHost.navController)

    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_REQUEST_CODE
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    if (cnt < 2) {
                        requestPermission()
                        cnt++
                    } else {
                        val dialog = MyDialog(this)
                        val transaction = supportFragmentManager.beginTransaction()
                        dialog.show(transaction, "dialog")
                        cnt = 0
                    }

                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.getIntExtra("action", ACTION_NO) ?: ACTION_NO
        onIntentAction(action)
    }

    private fun onIntentAction(action: Int) {
        when (action) {
            ACTION_MAIN -> {
                Snackbar
                    .make(
                        viewBinding.root,
                        "Вы прибыли.",
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }

            ACTION_SETTINGS -> {
                (supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment)
                    .navController
                    .apply {
                        if (currentDestination?.id != R.id.notificationSettingsFragment) {
                            popBackStack(R.id.mainFragment, false)
                            navigate(R.id.action_mainFragment_to_notificationSettingsFragment)
                        }
                    }
            }
        }
    }


    companion object {
        private const val NOTIFICATION_REQUEST_CODE = 12101
        const val ACTION_NO = 0
        const val ACTION_MAIN = 1
        const val ACTION_SETTINGS = 2
    }
}
