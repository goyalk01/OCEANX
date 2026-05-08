package com.oceanx.freshcart.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.domain.repository.CartRepository
import com.oceanx.freshcart.domain.usecase.ClearCartUseCase
import com.oceanx.freshcart.domain.usecase.GetCartItemsUseCase
import com.oceanx.freshcart.domain.usecase.GetCartTotalUseCase
import com.oceanx.freshcart.domain.usecase.RemoveFromCartUseCase
import com.oceanx.freshcart.domain.usecase.UpdateCartQuantityUseCase
import com.oceanx.freshcart.presentation.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for CartFragment.
 * Manages cart items, bill calculation, and order-related operations.
 *
 * State:
 * - cartItems: List of items in the cart
 * - billBreakdown: MRP, discount, delivery fee, total
 * - uiState: Loading/Success/Error state
 */
class CartViewModel(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val getCartTotalUseCase: GetCartTotalUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: Flow<List<CartItem>> = getCartItemsUseCase()

    val billBreakdown: Flow<GetCartTotalUseCase.BillBreakdown> = getCartTotalUseCase()

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Success(""))
    val uiState: StateFlow<UiState<String>> = _uiState

    /**
     * Increase the quantity of a cart item by 1.
     */
    fun increaseQuantity(productId: Int, currentQuantity: Int) {
        viewModelScope.launch {
            try {
                updateCartQuantityUseCase(productId, currentQuantity + 1)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to update quantity")
            }
        }
    }

    /**
     * Decrease the quantity of a cart item by 1.
     * If quantity reaches 0, removes the item from cart.
     */
    fun decreaseQuantity(productId: Int, currentQuantity: Int) {
        viewModelScope.launch {
            try {
                if (currentQuantity > 1) {
                    updateCartQuantityUseCase(productId, currentQuantity - 1)
                } else {
                    removeFromCartUseCase(productId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to update quantity")
            }
        }
    }

    /**
     * Remove an item from the cart.
     */
    fun removeItem(productId: Int) {
        viewModelScope.launch {
            try {
                removeFromCartUseCase(productId)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to remove item from cart")
            }
        }
    }

    /**
     * Add an item back to cart (used for undo swipe-to-delete).
     */
    fun addItemBackToCart(cartItem: CartItem) {
        viewModelScope.launch {
            try {
                cartRepository.addOrUpdateCartItem(cartItem)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to add item back to cart")
            }
        }
    }

    /**
     * Clear the entire cart.
     * Called after successful order placement.
     */
    fun clearCart() {
        viewModelScope.launch {
            try {
                clearCartUseCase()
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to clear cart")
            }
        }
    }

    /**
     * Get all cart items as a list (not a Flow).
     * Useful for getting current state in coroutines.
     */
    suspend fun getCartItemsSnapshot(): List<CartItem> {
        return cartRepository.getCartItems().let { flow ->
            // In a real app, would use flow.first()
            emptyList() // Placeholder
        }
    }
}
