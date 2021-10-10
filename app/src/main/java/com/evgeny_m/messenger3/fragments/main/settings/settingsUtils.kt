package com.evgeny_m.messenger3.fragments.main.settings

import android.widget.Toast
import com.evgeny_m.messenger3.fragments.main.settings.settings_fragments.ChangeFullNameFragment
import com.evgeny_m.messenger3.fragments.main.settings.settings_fragments.ChangeUserNameFragment
import com.evgeny_m.messenger3.utils.APP
import com.evgeny_m.messenger3.utils.user


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

fun backToSettingsFragment() {
    APP.onBackPressed()
    showToast("data update")
}

fun showToast(message: String) {
    Toast.makeText(APP, message, Toast.LENGTH_SHORT).show()
}

