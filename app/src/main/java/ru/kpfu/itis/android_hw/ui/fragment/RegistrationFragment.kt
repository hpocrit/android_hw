package ru.kpfu.itis.android_hw.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.UserEntity
import ru.kpfu.itis.android_hw.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var binding: FragmentRegistrationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)

        val id = ServiceLocator.getSharedPref().getString("USER_ID", "")
        if(id?.isNotEmpty() == true) {
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, MainFragment.newInstance(id))
                .addToBackStack("log")
                .commit()
        }

        binding?.run {
            registration.setOnClickListener {
                val name = name.text.toString()
                val phone = phone.text.toString()
                val mail = email.text.toString()
                val password = password.text.toString()

                if(name.isEmpty() || phone.isEmpty() || mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Some fields are empty", Toast.LENGTH_LONG).show()
                } else {
                    var users : List<UserEntity>? = null

                    lifecycleScope.launch(Dispatchers.IO) { // нельзя обращаться к бд в главном потоке
                        users = ServiceLocator.getDbInstance().userDao.getAllUser()
                    }

                    var cnt = ServiceLocator.getSharedPref().getInt("USER_ID_REG", 10)

                    val userEntity = UserEntity(
                        id = "id_${++cnt}",
                        name = name,
                        phone = phone,
                        email = mail,
                        password = password
                    )

                    ServiceLocator.getSharedPref().edit().putInt("USER_ID_REG", cnt).apply()

                    var flag = true
                    if(users != null) {
                        users!!.forEach {
                            if (it.email == userEntity.email || it.phone == userEntity.phone) {
                                Toast.makeText(
                                    requireContext(),
                                    "Such user already exists",
                                    Toast.LENGTH_SHORT
                                )
                                flag = false
                            }
                        }
                    }
                    if(flag) {
                        lifecycleScope.launch(Dispatchers.IO) { // нельзя обращаться к бд в главном потоке
                            ServiceLocator.getDbInstance().userDao.addUser(userEntity)
                        }
                        parentFragmentManager
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.container, LoginFragment())
                            .addToBackStack("reg")
                            .commit()
                    }
                }
            }
            login.setOnClickListener {
                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, LoginFragment())
                    .addToBackStack("reg")
                    .commit()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bottom_navigation).visibility = View.GONE
    }
}