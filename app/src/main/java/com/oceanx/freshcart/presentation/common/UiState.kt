package com.oceanx.freshcart.presentation.common

/**
 * Sealed class representing all possible UI states for reactive UI management.
 * This pattern separates data loading, success, and error states cleanly.
 *
 * Usage in Fragment:
 * ```
 * viewModel.uiState.collect { state ->
 *     when (state) {
 *         is UiState.Loading -> showLoadingSpinner()
 *         is UiState.Success -> bindData(state.data)
 *         is UiState.Error -> showErrorMessage(state.message)
 *     }
 * }
 * ```
 */
sealed class UiState<out T> {
    /**
     * Represents a loading state — typically shown with a progress spinner.
     */
    object Loading : UiState<Nothing>()

    /**
     * Represents successful data retrieval.
     * @param data The successfully fetched data of type T
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Represents an error state.
     * @param message Human-readable error message to display to the user
     */
    data class Error(val message: String) : UiState<Nothing>()
}
