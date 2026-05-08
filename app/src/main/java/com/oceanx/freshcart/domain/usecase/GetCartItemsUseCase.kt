package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use Case: Retrieve all items from the shopping cart.
 * Returns a Flow so the UI can reactively update whenever cart changes.
 *
 * @param repository The repository providing cart operations
 */
class GetCartItemsUseCase(private val repository: CartRepository) {

    /**
     * Retrieves all items in the cart.
     * @return Flow<List<CartItem>> - emits cart items whenever the cart is updated
     */
    operator fun invoke(): Flow<List<CartItem>> {
        return repository.getCartItems()
    }
}
