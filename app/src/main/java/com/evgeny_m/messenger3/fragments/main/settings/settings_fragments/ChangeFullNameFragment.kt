package com.evgeny_m.messenger3.fragments.main.settings.settings_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentChangeFullNameBinding
import com.evgeny_m.messenger3.fragments.main.settings.initFullName
import com.evgeny_m.messenger3.utils.*


class ChangeFullNameFragment : Fragment() {

    lateinit var binding: FragmentChangeFullNameBinding
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeFullNameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        initFullName()
    }

    private fun initToolbar() {
        toolbar = binding.changeFullNameToolbar
        toolbar.title = "Your Name"
        initBackButton(toolbar)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_save -> {
                    changeFullName()
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFullName() {
        val name = binding.changeNameText.text.toString()
        val surName = binding.changeSurnameText.text.toString()

        if (name.isEmpty()) {
            showToast("Enter your name")
        } else {
            val fullName = "$name $surName"
            saveUserData(fullName, CHILD_USERFULLNAME)
        }
    }
}