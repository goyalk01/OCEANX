package com.oceanx.freshcart.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.oceanx.freshcart.FreshCartApplication
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.ActivityMainBinding
import com.oceanx.freshcart.domain.repository.CartRepository
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
 * - Hide bottom nav on screens where it shouldn't appear (login, checkout, order success)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var cartRepository: CartRepository

    /**
     * Fragments where the bottom navigation should be hidden.
     * This prevents the bottom nav from appearing on login, checkout,
     * and order success screens for a cleaner UX.
     */
    private val hideBottomNavDestinations = setOf(
        R.id.loginFragment,
        R.id.checkoutFragment,
        R.id.orderSuccessFragment
    )

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
     * Also sets up a destination change listener to show/hide bottom nav
     * based on the current fragment.
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

        // Show/hide bottom nav based on current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility =
                if (destination.id in hideBottomNavDestinations) View.GONE else View.VISIBLE
        }
    }

    /**
     * Observe cart item count and update the badge on the cart menu item.
     *
     * Uses lifecycleScope + repeatOnLifecycle for lifecycle safety.
     * The previous implementation used `CoroutineScope(Dispatchers.Main)` which
     * creates an unmanaged scope — coroutines launched in it survive activity
     * destruction, causing memory leaks and potential crashes.
     */
    private fun observeCartBadge() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    /**
     * Handle back navigation through the app.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
