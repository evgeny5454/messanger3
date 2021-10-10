package com.evgeny_m.messenger3.fragments.main.settings.settings_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentChangeBioBinding
import com.evgeny_m.messenger3.utils.*


class ChangeBioFragment : Fragment() {

    private lateinit var binding: FragmentChangeBioBinding
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeBioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
        initBio()
    }

    private fun initBio() {
        binding.changeUserBioText.setText(user.bio)
    }

    private fun initToolbar() {
        toolbar = binding.changeBioToolbar
        toolbar.title = "Bio"
        initBackButton(toolbar)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_save -> {
                    changeBio()
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }
    }

    private fun changeBio() {
        val newBio = binding.changeUserBioText.text.toString()
        saveUserData(newBio, CHILD_BIO)
    }
}