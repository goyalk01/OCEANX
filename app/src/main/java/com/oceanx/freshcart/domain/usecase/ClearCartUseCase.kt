package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.domain.repository.CartRepository

/**
 * Use Case: Clear all items from the cart.
 * Called after a successful order placement.
 *
 * @param repository The repository providing cart operations
 */
class ClearCartUseCase(private val repository: CartRepository) {

    /**
     * Removes all items from the cart.
     */
    suspend operator fun invoke() {
        repository.clearCart()
    }
}
