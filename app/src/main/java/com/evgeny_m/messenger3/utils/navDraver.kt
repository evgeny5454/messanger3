package com.evgeny_m.messenger3.utils

import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.evgeny_m.messenger3.MainActivity
import com.evgeny_m.messenger3.R

lateinit var drawerLayout: DrawerLayout
lateinit var APP: MainActivity

fun MainActivity.initNavDrawer() {
    drawerLayout = binding.drawerLayout
    val navView = binding.navView
    val navController = findNavController(R.id.nav_content_host)
    var appBarConfiguration = AppBarConfiguration(setOf(R.menu.menu_nav_drawer),drawerLayout)
    navView.setupWithNavController(navController)
}

fun initMainNavigationButton(toolbar: Toolbar) {
    toolbar.setNavigationIcon(R.drawable.icon_gamburger_button)
    toolbar.setNavigationOnClickListener {
        drawerLayout.open()
    }
}

fun initBackButton(toolbar: Toolbar) {
    toolbar.setNavigationIcon(R.drawable.icon_back_button)
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    toolbar.setNavigationOnClickListener {
        APP.onBackPressed()
    }
}