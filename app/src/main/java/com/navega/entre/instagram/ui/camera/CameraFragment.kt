package com.navega.entre.instagram.ui.camera


import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment

import android.view.View

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.navega.entre.instagram.R
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.remote.camera.CameraDataSource
import com.navega.entre.instagram.data.remote.home.HomeScreenDataSource
import com.navega.entre.instagram.databinding.FragmentCameraBinding
import com.navega.entre.instagram.domain.camera.CameraRepoImpl
import com.navega.entre.instagram.domain.home.HomeScreenRepoImpl
import com.navega.entre.instagram.presentation.HomeScreenViewModeFactory
import com.navega.entre.instagram.presentation.camera.cameraViewModel
import com.navega.entre.instagram.presentation.homeSceenViewModel

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val REQUEST_IMAGE_CAPTURE=2
    private lateinit var binding:FragmentCameraBinding
    private var bitmap: Bitmap? = null

    private val viewModel by viewModels<cameraViewModel> {
        cameraViewModel.CameraViewModelFactory(CameraRepoImpl(CameraDataSource()))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCameraBinding.bind(view)

        val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        }catch (e:ActivityNotFoundException){
            Toast.makeText(requireContext(),"You dont have an App Camera",Toast.LENGTH_SHORT).show()

        }
        binding.btnUploadPhoto.setOnClickListener {
            bitmap?.let {
                viewModel.uploadPhoto(it, binding.etxtPhotoDescription.text.toString().trim())
                    .observe(viewLifecycleOwner, { result ->
                        when (result) {
                            is Result.Loading -> {

                                Toast.makeText(requireContext(),"Uploading photo", Toast.LENGTH_SHORT).show()
                            }

                            is Result.Success -> {
                                findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
                            }

                            is Result.Failure -> {

                                Toast.makeText(requireContext(),"Error: ${result.exception}", Toast.LENGTH_SHORT).show()

                            }
                        }
                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){

            val imagBitmap=data?.extras?.get("data") as Bitmap
            binding.imgAddPhoto.setImageBitmap(imagBitmap)
            bitmap=imagBitmap
        }

    }


}