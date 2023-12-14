package ru.kpfu.itis.android_hw.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.DeletedUserEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity
import ru.kpfu.itis.android_hw.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding?.run {

            val id = ServiceLocator.getSharedPref().getString("USER_ID", "")
            if(id?.isNotEmpty() == true) {
                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, MainFragment.newInstance(id))
                    .addToBackStack("log")
                    .commit()
            }

            login.setOnClickListener {
                var users: List<UserEntity>?

                val email = email.text.toString()
                val password = password.text.toString()

                var flag1 = true

                runBlocking {
                    val deletedUsers = withContext(Dispatchers.IO) {
                        ServiceLocator.getDbInstance().deletedDao.getAllUser()
                    }
                    deletedUsers?.forEach {
                        if(it.email == email && it.password == password) {
                            showDialog(it)
                            flag1 = false
                        }
                    }
                }

                val flag = lifecycleScope.async(Dispatchers.IO) {
                    users = ServiceLocator.getDbInstance().userDao.getAllUser()
                    if(users != null){
                        users!!.forEach {
                            if(it.email == email && it.password == password){
                                parentFragmentManager
                                    .beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.container, MainFragment())
                                    .addToBackStack("log")
                                    .commit()
                                ServiceLocator.getSharedPref().edit().putString("USER_ID", it.id).apply()
                                return@async true
                            }
                        }
                    }
                    return@async false
                }
                lifecycleScope.launch {
                    if(!flag.await() && flag1) {
                        makeToast()
                    }
                }
            }
            registration.setOnClickListener {
                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, RegistrationFragment())
                    .addToBackStack("reg")
                    .commit()
            }
        }
    }

    private fun showDialog(deletedUser: DeletedUserEntity) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("Account has been deleted")

        builder
            .setPositiveButton("Restore") { dialog, which ->
                lifecycleScope.launch(Dispatchers.IO) {
                    ServiceLocator.getDbInstance().userDao.addUser(UserEntity(deletedUser.id, deletedUser.name, deletedUser.phone, deletedUser.email, deletedUser.password))
                    ServiceLocator.getDbInstance().deletedDao.deleteUserById(deletedUser.id)
                }
                parentFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, MainFragment.newInstance(deletedUser.id))
                    .addToBackStack("restore")
                    .commit()
                ServiceLocator.getSharedPref().edit().putString("USER_ID", deletedUser.id).apply()

            }.setNegativeButton("Delete permanently") { dialog, which ->
                lifecycleScope.launch(Dispatchers.IO) {
                    ServiceLocator.getDbInstance().deletedDao.deleteUserById(deletedUser.id)
                }
//
            }

        val dialog = builder.create()
        dialog.show()
    }

    fun makeToast() {
        Toast.makeText(requireContext(), "Such user does not exist", Toast.LENGTH_SHORT).show()
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