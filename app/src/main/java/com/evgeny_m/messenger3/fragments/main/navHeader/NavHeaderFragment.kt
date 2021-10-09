package com.evgeny_m.messenger3.fragments.main.navHeader

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentNavHeaderBinding
import com.evgeny_m.messenger3.utils.*


open class NavHeaderFragment : Fragment() {

    lateinit var binding: FragmentNavHeaderBinding
    lateinit var userPhone: TextView
    lateinit var userFullName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavHeaderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        /**
         * readUserData() в этом методе проходит инициализация
         * userPhone и userName и присваиваются значения из базы данных
         */
        readUserData()

    }
}


