package com.oceanx.freshcart.domain.repository

import com.oceanx.freshcart.data.model.CartItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for cart operations.
 * This is a contract that defines what data operations are available.
 * Implementation is in the data layer (CartRepositoryImpl).
 *
 * By using an interface:
 * - The domain layer is decoupled from the data layer
 * - Testing is easier (can mock the repository)
 * - Implementation details are hidden
 */
interface CartRepository {

    /**
     * Retrieves all items currently in the cart.
     * @return Flow<List<CartItem>> - reactive stream of cart items
     */
    fun getCartItems(): Flow<List<CartItem>>

    /**
     * Retrieves the count of items in the cart.
     * @return Flow<Int> - reactive count
     */
    fun getCartItemCount(): Flow<Int>

    /**
     * Retrieves the total quantity (sum of all quantities).
     * Example: 2 Bananas + 3 Apples = total quantity 5
     * @return Flow<Int> - reactive total quantity
     */
    fun getTotalQuantity(): Flow<Int>

    /**
     * Adds a new item to cart or updates quantity if already exists.
     * @param item The CartItem to add
     */
    suspend fun addOrUpdateCartItem(item: CartItem)

    /**
     * Updates the quantity of an existing cart item.
     * @param productId The product to update
     * @param quantity New quantity value
     */
    suspend fun updateQuantity(productId: Int, quantity: Int)

    /**
     * Removes an item from the cart by product ID.
     * @param productId The product ID to remove
     */
    suspend fun removeCartItem(productId: Int)

    /**
     * Clears all items from the cart.
     * Called after a successful order.
     */
    suspend fun clearCart()

    /**
     * Checks if a product is already in the cart.
     * @param productId The product ID to check
     * @return Flow<Boolean> - true if product is in cart, false otherwise
     */
    fun isProductInCart(productId: Int): Flow<Boolean>

    /**
     * Retrieves a specific cart item by product ID.
     * @param productId The product ID to find
     * @return Flow<CartItem?> - the item if found, null otherwise
     */
    fun getCartItemByProductId(productId: Int): Flow<CartItem?>
}
