package com.evgeny_m.messenger3.fragments.main.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.RegisterActivity
import com.evgeny_m.messenger3.databinding.FragmentSettingsBinding
import com.evgeny_m.messenger3.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    private lateinit var toolbar: Toolbar
    lateinit var userPhone: TextView
    lateinit var userFullName: TextView
    lateinit var userName: TextView
    lateinit var userBio: TextView
    lateinit var userPhoto: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onResume() {
        super.onResume()
        //showToast("onResume SettingsFragment")
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
        view?.findNavController()?.navigate(R.id.action_settingsFragment_to_changeBioFragment)
    }

    private fun editUserName() {
        view?.findNavController()?.navigate(R.id.action_settingsFragment_to_changeUserNameFragment)
    }

    private fun initToolbarMenu() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings_menu_log_out -> {
                    UserStatus.updateUserStatus(UserStatus.LOG_OUT)
                    auth.signOut()
                    replaceActivity(RegisterActivity())
                    true
                }
                R.id.settings_menu_edit_full_name -> {
                    editFullName()
                    true
                }
                R.id.settings_menu_add_photo -> {
                    //editUserPhoto()
                    //getPhotoFromStorage()
                    view?.findNavController()?.navigate(R.id.action_settingsFragment_to_photoFragment)

                    true
                }
                else -> false
            }
        }
    }

    private fun getPhotoFromStorage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivity(intent)
    }

    private fun editUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600,600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    private fun editFullName() {
        view?.findNavController()?.navigate(R.id.action_settingsFragment_to_changeFullNameFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            updateUserPhoto(uri)
        }
    }
    
}