package com.oceanx.freshcart.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.data.model.Category
import com.oceanx.freshcart.data.model.Product
import com.oceanx.freshcart.data.source.ProductDataSource
import com.oceanx.freshcart.domain.repository.CartRepository
import com.oceanx.freshcart.domain.usecase.AddToCartUseCase
import com.oceanx.freshcart.presentation.common.UiState
import com.oceanx.freshcart.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for HomeFragment.
 * Manages product list, category filtering, search, and cart operations.
 *
 * State:
 * - products: Current filtered/searched product list
 * - categories: All available categories
 * - selectedCategory: Currently selected category filter
 * - searchQuery: Current search query
 * - cartBadgeCount: Number of items in cart (for toolbar badge)
 * - uiState: Loading/Success/Error state for the UI
 */
class HomeViewModel(
    private val cartRepository: CartRepository,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    /**
     * Cart badge count converted from a cold Flow (Room) to a hot StateFlow.
     * Uses stateIn() with WhileSubscribed to share the subscription and
     * automatically cancel when no collectors are active.
     */
    val cartBadgeCount: StateFlow<Int> = cartRepository.getCartItemCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Success(""))
    val uiState: StateFlow<UiState<String>> = _uiState

    init {
        loadCategories()
        loadAllProducts()

        // Set up reactive search with debounce
        viewModelScope.launch {
            _searchQuery
                .debounce(Constants.SEARCH_DEBOUNCE_MS)
                .collect { _ ->
                    filterProducts()
                }
        }
    }

    /**
     * Load all categories from data source.
     */
    private fun loadCategories() {
        _categories.value = ProductDataSource.getCategories()
    }

    /**
     * Load all products initially.
     */
    private fun loadAllProducts() {
        try {
            _uiState.value = UiState.Loading
            val allProducts = ProductDataSource.getProducts()
            _products.value = allProducts
            _uiState.value = UiState.Success("Products loaded")
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Failed to load products: ${e.message}")
        }
    }

    /**
     * Handle category selection.
     * Tapping the same category again deselects it (returns to All).
     */
    fun onCategorySelected(categoryName: String) {
        _selectedCategory.value = if (_selectedCategory.value == categoryName) {
            "All"
        } else {
            categoryName
        }
        filterProducts()
    }

    /**
     * Update search query.
     * Automatically triggers filtering with debounce via the init block collector.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /**
     * Filter products based on selected category and search query.
     * Applies both filters sequentially: category first, then search text.
     */
    private fun filterProducts() {
        val allProducts = ProductDataSource.getProducts()

        // Apply category filter
        val categoryFiltered = if (_selectedCategory.value == "All") {
            allProducts
        } else {
            allProducts.filter { it.category == _selectedCategory.value }
        }

        // Apply search filter
        val finalProducts = if (_searchQuery.value.isEmpty()) {
            categoryFiltered
        } else {
            categoryFiltered.filter { product ->
                product.name.contains(_searchQuery.value, ignoreCase = true) ||
                product.description.contains(_searchQuery.value, ignoreCase = true)
            }
        }

        _products.value = finalProducts
    }

    /**
     * Add a product to cart.
     * Called when user taps the "+" button on a product card.
     */
    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                addToCartUseCase(product)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to add item to cart")
            }
        }
    }

    /**
     * Update quantity of a product in cart.
     * Called when user changes quantity via +/- buttons on product card.
     */
    fun updateCartQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            try {
                if (newQuantity > 0) {
                    cartRepository.updateQuantity(productId, newQuantity)
                } else {
                    cartRepository.removeCartItem(productId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to update quantity")
            }
        }
    }

    /**
     * Check if a product is in the cart.
     * Used to decide whether to show quantity controls or the "Add" button.
     */
    fun isProductInCart(productId: Int): Flow<Boolean> {
        return cartRepository.isProductInCart(productId)
    }

    /**
     * Get the cart item for a product to display its current quantity.
     */
    fun getCartItemByProductId(productId: Int): Flow<CartItem?> {
        return cartRepository.getCartItemByProductId(productId)
    }

    /**
     * Get greeting based on current time of day.
     * @return Greeting string (Good morning/afternoon/evening)
     */
    fun getTimeBasedGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}
