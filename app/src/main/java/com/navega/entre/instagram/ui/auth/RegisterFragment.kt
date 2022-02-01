package com.navega.entre.instagram.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.navega.entre.instagram.R
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.remote.auth.AuthDataSource
import com.navega.entre.instagram.databinding.FragmentRegisterBinding
import com.navega.entre.instagram.domain.auth.authRepoImplement
import com.navega.entre.instagram.presentation.auth.AuthViewModel
import com.navega.entre.instagram.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            authRepoImplement(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        signUp()


    }

    private fun signUp() {


        binding.btnSiginup.setOnClickListener {
            val useName = binding.editTextUseName.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            if (validareUserData(
                    password,
                    confirmPassword,
                    useName,
                    email
                )
            ) return@setOnClickListener





createUser(email, password,useName)

             }

    }

    private fun createUser(email: String, password: String, useName: String) {
viewModel.signUp(email,password,useName).observe(viewLifecycleOwner, Observer{result->
    when(result){
        is Result.Loading->{
            binding.progressBar.visibility=View.VISIBLE
            binding.btnSiginup.isEnabled=false
        }
        is Result.Success->{
            binding.progressBar.visibility=View.GONE
findNavController().navigate(R.id.action_registerFragment_to_setUpProfileFragment)

        }
        is Result.Failure->{
            binding.progressBar.visibility=View.GONE
            binding.btnSiginup.isEnabled=true
            Toast.makeText(requireContext(),"error: ${result.exception}",Toast.LENGTH_SHORT).show()
        }
    }

})
    }

    private fun validareUserData(
        password: String,
        confirmPassword: String,
        useName: String,
        email: String
    ): Boolean {
        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "Password does not match"
            binding.editTextPassword.error = "Password does not match"
            return true
        }

        if (useName.isEmpty()) {
            binding.editTextUseName.error = "User name is empty"
            return true

        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email is empty"
            return true

        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return true
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Confirm Password is empty"
            return true

        }
        return false
    }
}