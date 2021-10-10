package com.evgeny_m.messenger3.fragments.main.settings.settings_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentChangeUserNameBinding
import com.evgeny_m.messenger3.fragments.main.settings.initUserName
import com.evgeny_m.messenger3.utils.*
import java.util.*

class ChangeUserNameFragment : Fragment() {

    lateinit var binding: FragmentChangeUserNameBinding
    private lateinit var toolbar: Toolbar
    lateinit var newUserName: String
    lateinit var oldUserName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeUserNameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        initUserName()
    }

    private fun initToolbar() {
        toolbar = binding.changeNameToolbar
        toolbar.title = "UserName"
        initBackButton(toolbar)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_save -> {
                    changeUserName()
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }
    }

    private fun changeUserName() {
        newUserName = binding.changeUserNameText.text.toString().lowercase(Locale.getDefault())
        oldUserName = user.username

        if (newUserName.isEmpty()) {
            showToast("Enter your user name")
        } else {
            checkingAndAddUserName()
        }
    }
}