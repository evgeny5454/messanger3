package com.evgeny_m.messenger3.utils

import android.widget.TextView
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var auth: FirebaseAuth
lateinit var database: DatabaseReference
lateinit var currentUserId: String
lateinit var user: UserModel



const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERFULLNAME = "userfullname"
const val CHILD_USERNAME = "username"
const val CHILD_BIO = "bio"




fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance().reference
    currentUserId = auth.currentUser?.uid.toString()
    user = UserModel()
    initUser()
}

fun initUser() {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .addValueEventListener(AppValueEventListener{
            user = it.getValue(UserModel::class.java) ?: UserModel()
        })
}

fun Fragment.saveUserData(inputUserData: String, child : String ) {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(child)
        .setValue(inputUserData).addOnCompleteListener {
            if (it.isSuccessful) {
                APP.onBackPressed()
                showToast("data saved")
            } else {
                showToast("data not saved")
            }
        }
}


