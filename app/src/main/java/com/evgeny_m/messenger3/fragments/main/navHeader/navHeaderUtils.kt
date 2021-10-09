package com.evgeny_m.messenger3.fragments.main.navHeader

import android.util.Log
import com.evgeny_m.messenger3.utils.*

fun NavHeaderFragment.readUserData() {

    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_PHONE)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userPhone = binding.navDrawerUserPhone
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
            userFullName = binding.navDrawerUserFullName
            userFullName.text = it.value as CharSequence?

        }.addOnFailureListener{
            Log.d("firebase", "Error getting data", it)
        }




}