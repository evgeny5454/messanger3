package com.evgeny_m.messenger3.fragments.main.navHeader

import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.utils.*

lateinit var userPhone : TextView
lateinit var userFullName : TextView

fun Fragment.readUserDataToNavHeader(userPhone: TextView, userFullName: TextView) {

    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_PHONE)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userPhone.text = it.value as CharSequence?

        }.addOnFailureListener{
            Log.d("firebase", "Error getting data", it)
        }

    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USERFULLNAME)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userFullName.text = it.value as CharSequence?

        }.addOnFailureListener{
            Log.d("firebase", "Error getting data", it)
        }

}