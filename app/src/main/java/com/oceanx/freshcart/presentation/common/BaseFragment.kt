package com.oceanx.freshcart.presentation.common

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.oceanx.freshcart.utils.toast

/**
 * Abstract base Fragment class that provides common functionality.
 * All app Fragments should extend this class to get consistent behavior.
 *
 * Provides:
 * - Common logging and error handling
 * - Toast utility methods
 * - Safe argument access pattern
 * - Loading/error state management helpers
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        observeViewModelUpdates()
    }

    /**
     * Initialize all views, set up listeners, configure adapters, etc.
     * Called once in onViewCreated after the view hierarchy is created.
     * Subclasses override this to set up their specific UI.
     */
    protected open fun initializeViews() {
        // Override in subclasses
    }

    /**
     * Set up observers on ViewModel data.
     * Called after initializeViews().
     * Subclasses override this to observe LiveData/StateFlow from their ViewModel.
     */
    protected open fun observeViewModelUpdates() {
        // Override in subclasses
    }

    /**
     * Shows an error message to the user via Toast.
     * @param message The error message to display
     */
    protected fun showError(message: String) {
        requireContext().toast(message)
    }

    /**
     * Shows an informational message to the user via Toast.
     * @param message The message to display
     */
    protected fun showMessage(message: String) {
        requireContext().toast(message)
    }

    /**
     * Helper to safely get arguments from a Fragment's Bundle.
     * Prevents null pointer exceptions when accessing arguments.
     */
    protected inline fun <reified T> getArgument(key: String): T? {
        return arguments?.get(key) as? T
    }
}
