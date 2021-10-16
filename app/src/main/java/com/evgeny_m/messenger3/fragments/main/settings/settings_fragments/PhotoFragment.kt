package com.evgeny_m.messenger3.fragments.main.settings.settings_fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger3.databinding.FragmentPhotoBinding
import com.evgeny_m.messenger3.model.PhotoModel
import com.evgeny_m.messenger3.utils.APP_ACTIVITY
import java.util.*


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

        val arrayPhotos = arrayListOf<PhotoModel>()
        val mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        val cursor = APP_ACTIVITY.contentResolver.query(
            mediaUri,
            projection,
            null,
            null,
            orderBy
        )
        cursor?.let {
            while (cursor.moveToNext()) {
                val indexData = it.getColumnIndex(MediaStore.MediaColumns.DATA)
                val absolutePathOfImage = cursor.getString(indexData)
                val newModel = PhotoModel()

                newModel.uri = absolutePathOfImage
                arrayPhotos.add(newModel)
            }
        }
        cursor?.close()



      /*  val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null

        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = APP_ACTIVITY.contentResolver.query(
            uri, projection, null,
            null, null
        );

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }*/
    }
}