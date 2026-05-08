package com.oceanx.freshcart.data.model

/**
 * Domain model representing an item in the shopping cart.
 * This model combines product information with cart-specific data like quantity.
 *
 * @param id Unique cart item identifier (can be different from product ID if item is added multiple times)
 * @param productId Reference to the product ID
 * @param name Product name
 * @param price Price per unit
 * @param unit Unit of measurement
 * @param quantity Number of units in cart
 * @param imageResId Product image
 */
data class CartItem(
    val id: Int = 0,
    val productId: Int,
    val name: String,
    val price: Double,
    val unit: String,
    val quantity: Int = 1,
    val imageResId: String
) {
    /**
     * Calculates the total price for this cart item (price * quantity).
     */
    fun getTotalPrice(): Double = price * quantity
}
