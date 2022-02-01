package com.navega.entre.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.navega.entre.instagram.core.hide
import com.navega.entre.instagram.core.show
import com.navega.entre.instagram.databinding.ActivityMainBinding
import com.navega.entre.instagram.databinding.FragmentRegisterBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        observeDestinationChanger()

    }

    private fun observeDestinationChanger() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {

                    //hide and show is viewExtensions
                    binding.bottomNavigation.hide()
                }

                R.id.registerFragment -> {
                    binding.bottomNavigation.hide()

                }

                R.id.setUpProfileFragment->{
                    binding.bottomNavigation.hide()

                }

                else -> {
                    binding.bottomNavigation.show()

                }
            }
        }
    }

}
