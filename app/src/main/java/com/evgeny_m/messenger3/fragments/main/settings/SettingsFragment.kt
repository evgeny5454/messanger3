package com.evgeny_m.messenger3.fragments.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.RegisterActivity
import com.evgeny_m.messenger3.databinding.FragmentSettingsBinding
import com.evgeny_m.messenger3.utils.auth
import com.evgeny_m.messenger3.utils.initBackButton
import com.evgeny_m.messenger3.utils.replaceActivity
import com.evgeny_m.messenger3.utils.showToast


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    private lateinit var toolbar: Toolbar
    lateinit var userPhone : TextView
    lateinit var userFullName: TextView
    lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        toolbar = binding.settingsToolbar
        toolbar.title = "Settings"
        initBackButton(toolbar)
        readUserData()
        initToolbarMenu()
        initSettingsItems() // инициализация кнопок во фрагменте настроек
    }

    private fun initSettingsItems() {
        binding.settingsEditUserName.setOnClickListener {
            editUserName()
        }
        binding.settingsEditUserBio.setOnClickListener {
            editUserBio()
        }
        binding.settingsBtnEditUserPhoto.setOnClickListener {
            editUserPhoto()
        }
        binding.settingsEditUserPhone.setOnClickListener {
            editUserPhone()
        }
    }

    private fun editUserPhone() {
        showToast("editUserPhone")
    }

    private fun editUserBio() {
        showToast("editUserBio")
    }

    private fun editUserName() {
        showToast("editUserName")
    }

    private fun initToolbarMenu() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings_menu_log_out -> {
                    auth.signOut()
                    replaceActivity(RegisterActivity())
                    true
                }
                R.id.settings_menu_edit_full_name -> {
                    editFullName()
                    true
                }
                R.id.settings_menu_add_photo -> {
                    editUserPhoto()
                    true
                }
                else -> false
            }
        }
    }

    private fun editUserPhoto() {
        showToast("editUserPhoto")
    }

    private fun editFullName() {
        view?.
        findNavController()?.
        navigate(R.id.action_settingsFragment_to_changeFullNameFragment)
    }

}