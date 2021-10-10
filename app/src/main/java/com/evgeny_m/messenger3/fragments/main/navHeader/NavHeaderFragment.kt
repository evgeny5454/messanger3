package com.evgeny_m.messenger3.fragments.main.navHeader

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.evgeny_m.messenger3.databinding.FragmentNavHeaderBinding
import com.evgeny_m.messenger3.utils.*


open class NavHeaderFragment : Fragment() {

    lateinit var binding: FragmentNavHeaderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavHeaderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
    }

    fun initFields() {
        userPhone = binding.navDrawerUserPhone
        userFullName = binding.navDrawerUserFullName
    }
}


