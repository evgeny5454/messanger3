package com.evgeny_m.messenger3.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.databinding.FragmentChatsBinding
import com.evgeny_m.messenger3.utils.initMainNavigationButton


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        toolbar = binding.chatsToolbar // получил ссылку на тулбар
        toolbar.title = "Messenger" // Сдесь передаем название приложения
        initMainNavigationButton(toolbar) // установил в тулбаре гамбургер
    }
}