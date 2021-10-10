package com.evgeny_m.messenger3.fragments.main.settings

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.fragments.main.settings.settings_fragments.ChangeFullNameFragment
import com.evgeny_m.messenger3.fragments.main.settings.settings_fragments.ChangeUserNameFragment
import com.evgeny_m.messenger3.utils.*


fun SettingsFragment.readUserData() {
    /**
     * Читаем номер телефона пользователя и передаем в поле номера телефона во фрагменте настроек
     */
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_PHONE)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userPhone = binding.settingsUserPhone
            userPhone.text = it.value as CharSequence?

        }.addOnFailureListener {
            Log.d("firebase", "Error getting data", it)
        }
    /**
     * Читаем имя пользователя и передаем в Поле имени во фрагменте настроек
     */
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USERFULLNAME)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userFullName = binding.settingsUserFullName
            userFullName.text = it.value as CharSequence?

        }.addOnFailureListener {
            Log.d("firebase", "Error getting data", it)
        }

    /**
     * Читаем ник пользователя и передаем в Поле Username во фрагменте настроек
     */
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USERNAME)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userName = binding.settingsUserName
            userName.text = "@${it.value as CharSequence?}"

        }.addOnFailureListener {
            Log.d("firebase", "Error getting data", it)
        }

    /**
     * Читаем Bio пользователя и передаем в Поле Bio во фрагменте настроек
     */
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_BIO)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userBio = binding.settingsUserBio
            userBio.text = it.value as CharSequence?

        }.addOnFailureListener {
            Log.d("firebase", "Error getting data", it)
        }
}

fun ChangeFullNameFragment.saveNewFullName(fullName: String) {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USERFULLNAME)
        .setValue(fullName).addOnCompleteListener {
            if (it.isSuccessful) {
                APP.onBackPressed()
                showToast("data saved")
            } else {
                showToast("data not saved")
            }
        }
}

fun ChangeFullNameFragment.initFullName() {
    val fullNameList = user.userfullname.split(" ")
    if (fullNameList.size == 2) {
        binding.changeNameText.setText(fullNameList[0])
        binding.changeSurnameText.setText(fullNameList[1])
    } else {
        binding.changeNameText.setText(fullNameList[0])
    }
}

fun ChangeUserNameFragment.initUserName() {
    binding.changeUserNameText.setText(user.username)
}


fun ChangeUserNameFragment.checkingAndAddUserName() {

    database
        .child(NODE_USER_NAMES).addListenerForSingleValueEvent(AppValueEventListener{
            if (it.hasChild(newUserName)) {
                showToast("name is taken")
            } else {
                checkOldUserName(oldUserName, newUserName)
            }
        })
}

private fun checkOldUserName(oldUserName: String, newUserName: String) {
    database.child(NODE_USER_NAMES).addListenerForSingleValueEvent(AppValueEventListener{
        if (it.hasChild(oldUserName)) {
            deleteOldUserName(oldUserName, newUserName)
        } else {
            addNewUserName(newUserName)
        }
    })
}

private fun deleteOldUserName(oldUserName: String, newUserName: String) {
    database
        .child(NODE_USER_NAMES)
        .child(oldUserName)
        .removeValue()
        .addOnCompleteListener {
            addNewUserName(newUserName)
        }
}

private fun addNewUserName(newUserName: String) {
    database
        .child(NODE_USER_NAMES)
        .child(newUserName)
        .setValue(currentUserId)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                updateCurrentUserName(newUserName)
            }
        }
}

private fun updateCurrentUserName(newUserName: String) {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USERNAME)
        .setValue(newUserName)
        .addOnCompleteListener {
            backToSettingsFragment()
        }
}

private fun backToSettingsFragment() {
    APP.onBackPressed()
    showToast("data update")
}

fun showToast(message: String) {
    Toast.makeText(APP, message, Toast.LENGTH_SHORT).show()
}

