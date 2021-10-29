package com.evgeny_m.messenger3.fragments.main.settings.photo_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.ItemPhotoBinding
import com.evgeny_m.messenger3.fragments.main.settings.photo_fragment.model.MediaStoreImage
import com.evgeny_m.messenger3.utils.APP_ACTIVITY

class PhotoFragmentAdapter: RecyclerView.Adapter<PhotoFragmentAdapter.PhotoFragmentHolder>(){

    private var listImagesCache = emptyList<MediaStoreImage>()

    class PhotoFragmentHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemPhotoBinding.bind(view)
        val image = binding.itemImage
        val imageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoFragmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo,parent,false)
        return PhotoFragmentHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoFragmentHolder, position: Int) {

        Glide.with(APP_ACTIVITY)
            .load(listImagesCache[position].contentUri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(holder.image)
        holder.imageView.setOnClickListener {
            val photoFragment = PhotoFragment()
            photoFragment.setPhoto(listImagesCache[position].contentUri)
            //APP_ACTIVITY.findNavController(R.id.nav_content_host).navigate(R.id.action_photoFragment_to_setPhotoFragment)
        }

    }

    override fun getItemCount(): Int {
        return listImagesCache.size
    }

    fun addImages(list: List<MediaStoreImage>){
        listImagesCache = list
        notifyDataSetChanged()
    }
}