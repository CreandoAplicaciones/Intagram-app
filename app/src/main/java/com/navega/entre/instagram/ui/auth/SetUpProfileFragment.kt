package com.navega.entre.instagram.ui.auth

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.navega.entre.instagram.R
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.remote.auth.AuthDataSource
import com.navega.entre.instagram.databinding.FragmentSetUpProfileBinding
import com.navega.entre.instagram.domain.auth.authRepoImplement
import com.navega.entre.instagram.presentation.auth.AuthViewModel
import com.navega.entre.instagram.presentation.auth.AuthViewModelFactory

class SetUpProfileFragment : Fragment(R.layout.fragment_set_up_profile) {

    private lateinit var binding: FragmentSetUpProfileBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            authRepoImplement(
                AuthDataSource()
            )
        )
    }
    private val REQUEST_IMAGE_CAPTURE = 1
    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetUpProfileBinding.bind(view)
        binding.profileImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "you dont have an App Camera", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnCreatePerfile.setOnClickListener {
            val username = binding.etxtUserName.text.toString().trim()
            val alertDialog =
                AlertDialog.Builder(requireContext()).setTitle("Uploading photo").create()

                bitmap?.let {

                    if (!username.isNullOrEmpty()) {
                        viewModel.updateUserProfile(imaBitmap = it, userName = username)
                            .observe(viewLifecycleOwner, { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        alertDialog.show()
                                    }
                                    is Result.Success -> {
                                        alertDialog.dismiss()
                                        findNavController().navigate(R.id.action_setUpProfileFragment_to_homeScreenFragment)
                                    }
                                    is Result.Failure -> {
                                        alertDialog.dismiss()

                                    }
                                }

                            })
                    }

                }
            }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }

    }
}