package com.oceanx.freshcart.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.badge.BadgeDrawable
import com.oceanx.freshcart.FreshCartApplication
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.ActivityMainBinding
import com.oceanx.freshcart.domain.repository.CartRepository
import com.oceanx.freshcart.presentation.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * MainActivity - the single activity host for the entire app.
 * Uses Navigation Component to manage fragment transitions.
 * Hosts a NavHostFragment that displays fragments based on nav_graph.xml.
 *
 * Responsibilities:
 * - Set up navigation
 * - Display bottom navigation (after login)
 * - Update cart badge on bottom nav
 * - Check login state and redirect appropriately
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var cartRepository: CartRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get repository from application
        cartRepository = (application as FreshCartApplication).cartRepository

        // Set up navigation
        setupNavigation()

        // Observe cart count and update badge
        observeCartBadge()
    }

    /**
     * Set up the navigation controller and connect to bottom navigation.
     */
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Connect bottom navigation with nav controller
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navController
        )

        // Hide bottom nav initially (show only after login)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.cartFragment -> {
                    navController.navigate(R.id.cartFragment)
                    true
                }
                R.id.profileFragment -> {
                    // Placeholder for profile
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Observe cart item count and update the badge on the cart menu item.
     */
    private fun observeCartBadge() {
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.getCartItemCount().collect { count ->
                val badgeDrawable = binding.bottomNavigationView
                    .getOrCreateBadge(R.id.cartFragment)
                
                if (count > 0) {
                    badgeDrawable.isVisible = true
                    badgeDrawable.number = count
                } else {
                    badgeDrawable.isVisible = false
                }
            }
        }
    }

    /**
     * Handle back navigation through the app.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
