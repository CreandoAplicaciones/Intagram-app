package com.navega.entre.instagram.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.navega.entre.instagram.R
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.remote.auth.AuthDataSource
import com.navega.entre.instagram.databinding.FragmentLoginBinding
import com.navega.entre.instagram.domain.auth.authRepoImplement
import com.navega.entre.instagram.presentation.auth.AuthViewModel
import com.navega.entre.instagram.presentation.auth.AuthViewModelFactory


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            authRepoImplement(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        firebaseAuth.currentUser
        isUserLoggedIn()
        doLogin()
        goToSignUpPage()


    }

    private fun isUserLoggedIn() {
        firebaseAuth.currentUser?.let { user ->


            if (user.displayName.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_loginFragment_to_setUpProfileFragment)
            } else {
                //solo se accede si es diferente a null
                findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
            }
        }
    }

    private fun doLogin() {
        binding.btnSigin.setOnClickListener {
            //trim() si hay espacio alante y/o atras no se tomen en cuenta
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            validateCredentials(email, password)
            signIn(email, password)

        }
    }

    private fun goToSignUpPage() {
        binding.txtSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }


    private fun validateCredentials(email: String, password: String) {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "E-mail is empty"
            return
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return
        }

    }

    private fun signIn(email: String, password: String) {
        viewModel.signIn(email, password).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSigin.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data?.displayName.isNullOrEmpty()) {
                        findNavController().navigate(R.id.action_loginFragment_to_setUpProfileFragment)
                    } else {
                        //solo se accede si es diferente a null
                        findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                    }

                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSigin.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        })


    }

}