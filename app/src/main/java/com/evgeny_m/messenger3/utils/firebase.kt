package com.evgeny_m.messenger3.utils

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.evgeny_m.messenger3.MainActivity
import com.evgeny_m.messenger3.fragments.main.settings.SettingsFragment
import com.evgeny_m.messenger3.fragments.main.settings.backToSettingsFragment
import com.evgeny_m.messenger3.fragments.main.settings.settings_fragments.ChangeUserNameFragment
import com.evgeny_m.messenger3.fragments.main.settings.showToast
import com.evgeny_m.messenger3.fragments.register.replacePhoneNumber
import com.evgeny_m.messenger3.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var auth: FirebaseAuth
lateinit var database: DatabaseReference
lateinit var storage: StorageReference
lateinit var currentUserId: String
lateinit var user: UserModel

/**
 *  Статические поля для NavDrawer, что бы обновлять данные с помощъю ChatsFragment
 *  они проходят инициализацию в NavHeaderFragment в методе onStart
 */

@SuppressLint("StaticFieldLeak")
lateinit var userPhone : TextView
@SuppressLint("StaticFieldLeak")
lateinit var userFullName : TextView
@SuppressLint("StaticFieldLeak")
lateinit var userPhoto : ImageView

const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERFULLNAME = "userfullname"
const val CHILD_USERNAME = "username"
const val CHILD_BIO = "bio"
const val CHILD_USER_PHOTO = "photoUrl"
const val CHILD_STATUS = "status"



fun initFirebase(){
    auth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance().reference
    storage = FirebaseStorage.getInstance().reference
    currentUserId = auth.currentUser?.uid.toString()
    user = UserModel()
    initUser()
}

/**
 * initUser() - используется для однократного отображения данных пользователя, например когда надо
 * поменять имя пользователя
 */

fun initUser() {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .addValueEventListener(AppValueEventListener{
            user = it.getValue(UserModel::class.java) ?: UserModel()
        })
}

fun saveUserData(inputUserData: String, child : String ) {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(child)
        .setValue(inputUserData).addOnCompleteListener {
            if (it.isSuccessful) {
                APP_ACTIVITY.onBackPressed()
                showToast("data saved")
            } else {
                showToast("data not saved")
            }
        }
}

/**
 * readUserData() - обновляет пользовательские данные когда у SettingsFragment вызывается onResume
 * это дает неплохой результат, и данные отоброжаются всегда
 */

@SuppressLint("SetTextI18n")
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

    /**
     * Читаем photoURL пользователя и передаем обновляем фото пользователя во фрагменте настроек
     */

  database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USER_PHOTO)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            userPhoto = binding.settingsUserImage
            Glide
                .with(this)
                .load(it.value)
                .centerCrop()
                .into(userPhoto)

        }.addOnFailureListener{
            Log.d("firebase", "Error getting data", it)
        }
}

/**
 * checkingAndAddUserName() - передаем в этот метод новое имя пользователя и старое имя пользователя
 * если в базе данных отсудствует имя данного пользователя записываем. Если есть старое и новое имя
 * и они отличаются удаляем старое и пишем новое. Обновляем так же данные в пользовательских данных
 *
 * updateCurrentUserName() - последний метод из данной функции
 */

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

/**
 * readUserDataToNavHeader() - обновляет данные в header navDrawer
 */

fun readUserDataToNavHeader() {

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

    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(CHILD_USER_PHOTO)
        .get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")

            Glide
                .with(APP_ACTIVITY)
                .load(it.value)
                .centerCrop()
                .into(userPhoto)


        }.addOnFailureListener{
            Log.d("firebase", "Error getting data", it)
        }
}

/**
 * authNewUser() - производит ззапись при регистрации пользовательских данных
 */

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
}

fun updateUserPhoto(uri: Uri?) {
    val path = storage.child(FOLDER_PROFILE_IMAGE)
        .child(currentUserId)
    if (uri != null) {
        path.putFile(uri).addOnSuccessListener {
            path.downloadUrl.addOnSuccessListener {
                val photoUrl = it.toString()
                saveUserPhotoUrl(photoUrl, CHILD_USER_PHOTO)
            }
        }
    }
}

fun saveUserPhotoUrl(photoUrl: String, childUserPhoto: String) {
    database
        .child(NODE_USERS)
        .child(currentUserId)
        .child(childUserPhoto)
        .setValue(photoUrl).addOnCompleteListener {
            if (it.isSuccessful) {
                showToast("data saved")
            } else {
                showToast("data not saved")
            }
        }
}






