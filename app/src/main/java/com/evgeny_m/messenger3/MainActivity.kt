package com.evgeny_m.messenger3

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.evgeny_m.messenger3.databinding.ActivityMainBinding
import com.evgeny_m.messenger3.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private  val MY_PERMISSIONS_REQUEST = 1234
    private val PERMISSIONS = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS )


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isPermissions()){
            requestPermissions(PERMISSIONS, MY_PERMISSIONS_REQUEST)
            return
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
        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.isNotEmpty()){
            if (isPermissions()){
                (Objects.requireNonNull(this.getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager).clearApplicationUserData()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun isPermissions():Boolean{
        PERMISSIONS.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED){
                return true
            }
        }
        return false
    }
}