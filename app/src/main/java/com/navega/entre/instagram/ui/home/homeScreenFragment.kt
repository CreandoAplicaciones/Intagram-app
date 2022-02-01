package com.navega.entre.instagram.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.navega.entre.instagram.R
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.core.hide
import com.navega.entre.instagram.core.show
import com.navega.entre.instagram.data.model.Poster
import com.navega.entre.instagram.data.model.post

import com.navega.entre.instagram.data.remote.home.HomeScreenDataSource
import com.navega.entre.instagram.databinding.FragmentHomeScreenBinding
import com.navega.entre.instagram.domain.home.HomeScreenRepoImpl
import com.navega.entre.instagram.presentation.HomeScreenViewModeFactory
import com.navega.entre.instagram.presentation.homeSceenViewModel
import com.navega.entre.instagram.ui.home.adapter.homeScreenAdapter


class homeScreenFragment : Fragment(R.layout.fragment_home_screen),
    homeScreenAdapter.OnPostClickListener {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<homeSceenViewModel> {
        HomeScreenViewModeFactory(HomeScreenRepoImpl(HomeScreenDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewModel.fetLatesPosts().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.show()
                        return@Observer
                    } else {
                        binding.emptyContainer.hide()
                        binding.rvHome.adapter = homeScreenAdapter(result.data, this)
                    }

                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        requireContext(), "Ocurrió un error: ${result.exception}", Toast.LENGTH_LONG
                    ).show()

                }
            }

        })


        //  binding.rvHome.adapter = homeScreenAdapter(postList)

    }

    override fun onLikeButtonClick(post: post, liked: Boolean) {
        viewModel.registrarLikeButtonState(post.id, liked).observe(viewLifecycleOwner) {result->

                when (result) {
                    is Result.Loading -> {}

                    is Result.Success -> {}
                    is Result.Failure -> {
                        Toast.makeText(
                            requireContext(), "Ocurrió un error: ${result.exception}", Toast.LENGTH_LONG
                        ).show()

                    }
                }
        }
    }


}