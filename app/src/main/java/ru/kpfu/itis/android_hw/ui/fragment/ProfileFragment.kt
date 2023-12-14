package ru.kpfu.itis.android_hw.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
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
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.data.di.ServiceLocator
import ru.kpfu.itis.android_hw.data.entity.DeletedUserEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity
import ru.kpfu.itis.android_hw.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var binding: FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding?.run {
            val userId = ServiceLocator.getSharedPref().getString("USER_ID", "")!!
            var userEntity: UserEntity? = null

            val job = lifecycleScope.launch(Dispatchers.IO) {
                userEntity = ServiceLocator.getDbInstance().userDao.getUserInfoById(userId)
            }
            lifecycleScope.launch {
                job.join()
                name.text = userEntity!!.name
                email.text = userEntity!!.email
                phone.text = userEntity!!.phone
            }

            editPhone.setOnClickListener {
                showDialogPhone("Edit your phone", userId)
            }
            editPassword.setOnClickListener {
                showDialogPassword("Edit your password", userId)
            }
            exit.setOnClickListener {
                ServiceLocator.getSharedPref().edit().remove("USER_ID").apply()
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, LoginFragment())
                    .commit()
            }
            delete.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    val userEntity = ServiceLocator.getDbInstance().userDao.getUserInfoById(userId)

                    userEntity!!.let {
                        ServiceLocator.getDbInstance().deletedDao.addUser(DeletedUserEntity(userId, it.name, it.phone, it.email, it.password, System.currentTimeMillis()))
                    }
                    ServiceLocator.getDbInstance().userDao.deleteUserById(userId)

                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LoginFragment())
                        .commit()
                    ServiceLocator.getSharedPref().edit().remove("USER_ID").apply()
                }
            }
        }
    }

    private fun showDialogPhone(title: String, userId: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(title)

        var enteredText = ""

        val inputEditText = EditText(requireContext())
        builder
            .setView(inputEditText)
            .setPositiveButton("Edit") { dialog, which ->
                enteredText = inputEditText.text.toString()
                if(enteredText.isNotEmpty()) {
                    var flag = lifecycleScope.async(Dispatchers.IO) {
                        val users = ServiceLocator.getDbInstance().userDao.getAllUser()
                        users?.forEach {
                            if (it.phone == enteredText) {
                                return@async false
                            }
                        }
                        ServiceLocator.getDbInstance().userDao.updateUserPhone(userId, enteredText)
                        return@async true
                    }
                    lifecycleScope.launch {
                        if(flag.await()) {
                            binding?.phone?.text = enteredText
                        } else {
                            makeToast("This phone number is already taken")
                        }
                    }
                }else { makeToast()}

            }.setNegativeButton("Cansel") { dialog, which ->
                dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun showDialogPassword(title: String, userId: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(title)

        val inputEditText = EditText(requireContext())
        builder
            .setView(inputEditText)
            .setPositiveButton("Edit") { dialog, which ->
                val enteredText = inputEditText.text.toString()
                if(enteredText.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        ServiceLocator.getDbInstance().userDao.updateUserPassword(userId, enteredText)
                    }
                }else { makeToast()}

            }.setNegativeButton("Cansel") { dialog, which ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun makeToast() {
        Toast.makeText(requireContext(), "Empty field", Toast.LENGTH_SHORT).show()
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text , Toast.LENGTH_SHORT).show()
    }



    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.main_bottom_navigation).visibility = View.VISIBLE
    }

}