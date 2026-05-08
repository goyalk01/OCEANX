package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.domain.repository.CartRepository

/**
 * Use Case: Remove a product from the shopping cart.
 * Deletes the entire item regardless of quantity.
 *
 * @param repository The repository providing cart operations
 */
class RemoveFromCartUseCase(private val repository: CartRepository) {

    /**
     * Removes an item from the cart by product ID.
     * @param productId The ID of the product to remove
     */
    suspend operator fun invoke(productId: Int) {
        repository.removeCartItem(productId)
    }
}
