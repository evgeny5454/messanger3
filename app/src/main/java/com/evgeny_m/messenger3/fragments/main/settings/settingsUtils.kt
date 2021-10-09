package com.evgeny_m.messenger3.fragments.main.settings

import android.util.Log
import com.evgeny_m.messenger3.fragments.main.settings.settings_fragments.ChangeFullNameFragment
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

        }.addOnFailureListener{
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

        }.addOnFailureListener{
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

        }.addOnFailureListener{
            Log.d("firebase", "Error getting data", it)
        }
}

fun ChangeFullNameFragment.saveNewFullName(fullName: String) {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USERFULLNAME)
        .setValue(fullName).addOnCompleteListener {
            if (it.isSuccessful){
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