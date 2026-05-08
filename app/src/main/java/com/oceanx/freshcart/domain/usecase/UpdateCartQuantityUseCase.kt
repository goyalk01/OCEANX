package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.domain.repository.CartRepository

/**
 * Use Case: Update the quantity of an item in the cart.
 * Used when user taps the +/- buttons in the cart.
 *
 * @param repository The repository providing cart operations
 */
class UpdateCartQuantityUseCase(private val repository: CartRepository) {

    /**
     * Updates the quantity of a cart item.
     * @param productId The product ID to update
     * @param quantity The new quantity value
     */
    suspend operator fun invoke(productId: Int, quantity: Int) {
        // Don't update if quantity is 0 or negative
        if (quantity <= 0) {
            repository.removeCartItem(productId)
        } else {
            repository.updateQuantity(productId, quantity)
        }
    }
}
