package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.ui.fragment.LoginFragment
import ru.kpfu.itis.android_hw.ui.fragment.MainFragment
import ru.kpfu.itis.android_hw.ui.fragment.NewSeriesFragment
import ru.kpfu.itis.android_hw.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000

        val sevenDaysAgo = System.currentTimeMillis() - sevenDaysInMillis

        lifecycleScope.launch(Dispatchers.IO) {
            ServiceLocator.getDbInstance().deletedDao.deleteOldItems(sevenDaysAgo)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment())
            .commit()

        findViewById<BottomNavigationView>(R.id.main_bottom_navigation).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    loadFragment(MainFragment())
                    true
                }
                R.id.profileFragment -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.newSeriesFragment -> {
                    loadFragment(NewSeriesFragment())
                    true
                }

                else -> { true }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }




}