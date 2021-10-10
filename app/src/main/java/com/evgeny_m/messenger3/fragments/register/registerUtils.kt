package com.evgeny_m.messenger3.fragments.register

import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.MainActivity
import com.evgeny_m.messenger3.utils.*

/**
 *  fun sendPhone
 *  Производит обработку введенного номера, что позволяет вводить номер с цифр 8,7,9
 *  в итоге получается +7 *** *** ** **
 */

lateinit var replacePhoneNumber: String

fun PhoneFragment.sendPhone(inputPhoneNumber: String, toolbar: Toolbar) {

    replacePhoneNumber =
        when {
            inputPhoneNumber[0].toString() == "8" -> {
                inputPhoneNumber.replaceFirst("8", "+7")
            }
            inputPhoneNumber[0].toString() == "7" -> {
                inputPhoneNumber.replaceFirst("7", "+7")
            }
            inputPhoneNumber[0].toString() == "9" -> {
                "+7$inputPhoneNumber"
            }
            else -> {
                inputPhoneNumber
            }
            //сделал разные условия ввода номера телефона, пользователь сможет вводить свой номер
        }

    if (replacePhoneNumber.length == 12) { // проверяем номер должно быть 12 знаков
        toolbar.title = replacePhoneNumber // устанавливаем текст после обработки в тулбаре
    } else {
        showToast("Введите номер телефона в корректном формате")
    }
}

/*
fun Fragment.authNewUser() {
    val dateMap = mutableMapOf<String, Any>()
    val uid = auth.currentUser?.uid.toString()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_PHONE] = replacePhoneNumber
    dateMap[CHILD_USERFULLNAME] = uid
    dateMap[CHILD_USERNAME] = uid

    database
        .child(NODE_USERS)
        .child(uid)
        .updateChildren(dateMap)
        .addOnCompleteListener { task2 ->
            if (task2.isSuccessful) {
                showToast("авторизация прошла")
                replaceActivity(MainActivity())
            } else {
                showToast("авторизация прошла но данные не записаны")
                Log.d ("TASK2" , task2.exception?.message.toString())
            }
        }
}*/
