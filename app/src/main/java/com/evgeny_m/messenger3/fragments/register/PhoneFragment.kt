package com.evgeny_m.messenger3.fragments.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentPhoneBinding
import com.evgeny_m.messenger3.utils.AppTextWatcher
import com.evgeny_m.messenger3.utils.auth
import com.evgeny_m.messenger3.utils.authNewUser
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class PhoneFragment : Fragment() {

    val TAG = "LOG_REG_FRAGMENT"

    private lateinit var binding: FragmentPhoneBinding
    private lateinit var inputPhoneNumber: String
    private lateinit var toolbar: Toolbar
    private lateinit var buttonNext: FloatingActionButton
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        toolbar = binding.enterPhoneToolbar
        buttonNext = binding.enterPhoneNextBtn
        watchToEnterPhone() // следим за вводом номера и показываем кнопку далее

        buttonNext.setOnClickListener {
            sendPhone(
                inputPhoneNumber,
                toolbar
            ) // передаем введенный номер и тулбар, устанавливаем номер в тулбаре
            authUser()
        }
        /**
         * callbacks - автоматически производит аутентификацию по номеру телефонаю
         * В случае успешной авторизации передаются данные пользователя в базу данных
         */
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                singInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(exeptionMessage: FirebaseException) {
                Log.d(TAG, "onVerificationFailed:$exeptionMessage")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token.toString()

                view
                    ?.findNavController()
                    ?.navigate(R.id.action_phoneFragment_to_codeFragment)
            }
        }
    }
    private fun singInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                // данный метод еще не тестировался !
                authNewUser()
            }
        }
    }

    private fun authUser() {

        val option = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(replacePhoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)

    }


    private fun watchToEnterPhone() {
        binding.enterPhoneEditText.addTextChangedListener(AppTextWatcher {
            inputPhoneNumber = binding.enterPhoneEditText.text.toString()
            when {
                inputPhoneNumber.length == 10 -> {
                    buttonNext.visibility = View.VISIBLE
                }
                inputPhoneNumber.length < 5 -> {
                    buttonNext.visibility = View.GONE
                }
            }
        })
    }

    companion object {
        var storedVerificationId: String = ""
        var resendToken: String = ""
    }
}