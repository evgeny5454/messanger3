package com.evgeny_m.messenger3.fragments.main.settings.photo_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.evgeny_m.messenger3.databinding.FragmentSetPhotoBinding
import com.evgeny_m.messenger3.fragments.main.settings.photo_fragment.PhotoFragment.Companion.receivedPhotoUri
import com.evgeny_m.messenger3.utils.APP_ACTIVITY
import com.lyft.android.scissors.CropView


class SetPhotoFragment : Fragment() {

    private lateinit var binding: FragmentSetPhotoBinding
    lateinit var cropView: CropView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        cropView = binding.cropView
        val uri = receivedPhotoUri


        /*cropView.extensions()
            .load(uri)*/

        Glide
            .with(APP_ACTIVITY)
            .load(uri)
            .into(cropView)

    }
}