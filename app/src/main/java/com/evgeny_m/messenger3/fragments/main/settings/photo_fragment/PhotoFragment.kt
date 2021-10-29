package com.evgeny_m.messenger3.fragments.main.settings.photo_fragment

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.evgeny_m.messenger3.R
import com.evgeny_m.messenger3.databinding.FragmentPhotoBinding
import com.evgeny_m.messenger3.fragments.main.settings.photo_fragment.model.MediaStoreImage
import com.evgeny_m.messenger3.utils.APP_ACTIVITY
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val arrayPhotos = mutableListOf<MediaStoreImage>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        val selection = "${MediaStore.Images.Media.DATE_ADDED} >= ?"

        val selectionArgs = arrayOf(
            // Release day of the G1. :)
            dateToTimestamp(day = 22, month = 10, year = 2008).toString()
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        APP_ACTIVITY.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateModifiedColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idColumn)
                val dateModified =
                    Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                val image = MediaStoreImage(id, displayName, dateModified, contentUri)
                arrayPhotos.add(image)

            }
            cursor.close()
        }

        val adapter = PhotoFragmentAdapter()
        val recyclerView = binding.fragmentPhotoRecyclerView
        val layoutManager = GridLayoutManager(APP_ACTIVITY,3)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.addImages(arrayPhotos)
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }

    fun setPhoto(contentUri: Uri) {

        receivedPhotoUri = contentUri

        APP_ACTIVITY.findNavController(R.id.nav_content_host).navigate(R.id.action_photoFragment_to_setPhotoFragment)


    }
    companion object{
        lateinit var receivedPhotoUri: Uri
    }

}