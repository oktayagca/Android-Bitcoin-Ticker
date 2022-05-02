package com.example.bitcointicker.ui

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bitcointicker.R
import com.example.bitcointicker.databinding.ActivityMainBinding
import com.example.bitcointicker.ui.base.BaseActivity
import com.example.bitcointicker.utils.gone
import com.example.bitcointicker.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun themeSet() {
        setTheme(R.style.Theme_BitcoinTicker)
    }

    override fun onCreateActivity() {

        setupBottomNavigation()
        showLoading()
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemReselectedListener {}

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.favoriteFragment -> {
                    binding.bottomNavigation.show()
                }
                else -> {
                    binding.bottomNavigation.gone()
                }
            }
        }
    }

    fun showLoading() {
        binding.overlayLayout.show()
        binding.animationView.show()
        binding.animationView.playAnimation()

    }
    fun stopLoading() {
        binding.animationView.pauseAnimation()
        binding.overlayLayout.gone()
        binding.animationView.gone()

    }

}