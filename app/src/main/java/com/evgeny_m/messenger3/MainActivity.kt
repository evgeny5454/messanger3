package com.evgeny_m.messenger3

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.evgeny_m.messenger3.databinding.ActivityMainBinding
import com.evgeny_m.messenger3.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_ACTIVITY = this
        initFirebase()

        if (auth.currentUser?.uid == null) {
            replaceActivity(RegisterActivity())
        } else {
            initNavDrawer() // инициализация DrawerLayout
            CoroutineScope(Dispatchers.IO).launch{
                initContacts()
            }

        }
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.close()
        } else {

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        UserStatus.updateUserStatus(UserStatus.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        UserStatus.updateUserStatus(UserStatus.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }

        }
    }
}