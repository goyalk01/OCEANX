package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.data.model.Product
import com.oceanx.freshcart.domain.repository.CartRepository

/**
 * Use Case: Add a product to the shopping cart.
 * If the product is already in the cart, increments its quantity.
 * If not in cart, adds it with quantity 1.
 *
 * This follows the invoke operator pattern, making the use case callable as a function:
 * ```
 * addToCartUseCase(product)
 * ```
 *
 * @param repository The repository providing cart operations
 */
class AddToCartUseCase(private val repository: CartRepository) {

    /**
     * Adds a product to the cart or increases its quantity if already present.
     * @param product The product to add
     */
    suspend operator fun invoke(product: Product) {
        val cartItem = product.toCartItem()
        repository.addOrUpdateCartItem(cartItem)
    }

    /**
     * Extension function to convert Product to CartItem.
     */
    private fun Product.toCartItem(): CartItem {
        return CartItem(
            productId = this.id,
            name = this.name,
            price = this.price,
            unit = this.unit,
            quantity = 1,
            imageResId = this.imageResId
        )
    }
}
