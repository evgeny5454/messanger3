package com.evgeny_m.messenger3.fragments.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.databinding.FragmentCodeBinding
import com.evgeny_m.messenger3.fragments.register.PhoneFragment.Companion.storedVerificationId
import com.evgeny_m.messenger3.utils.AppTextWatcher
import com.evgeny_m.messenger3.utils.auth
import com.evgeny_m.messenger3.utils.showToast
import com.google.firebase.auth.PhoneAuthProvider


class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.enterCodeEditText.addTextChangedListener(AppTextWatcher{
            val string = binding.enterCodeEditText.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = binding.enterCodeEditText.text.toString()

        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)

        auth.signInWithCredential(credential).addOnCompleteListener { task1 ->
            if (task1.isSuccessful) {
                authNewUser()
            } else {
                showToast("авторизация не прошла")
                Log.d ("TASK1" , task1.exception?.message.toString())
            }
        }
    }
}