package com.oceanx.freshcart.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.oceanx.freshcart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for cart operations.
 * Defines all database queries with Room annotations.
 * All methods return Flow<> for reactive, real-time updates when data changes.
 *
 * Room automatically implements these abstract methods with SQL under the hood.
 */
@Dao
interface CartDao {

    /**
     * Retrieves all cart items as a Flow.
     * Any time cart items change, the flow emits the updated list.
     * @return Flow<List<CartItemEntity>> - reactive stream of all cart items
     */
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    /**
     * Retrieves a specific cart item by product ID.
     * Used to check if an item is already in the cart before adding it.
     * @param productId The product ID to search for
     * @return Flow<CartItemEntity?> - emits the item if found, null otherwise
     */
    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    fun getCartItemByProductId(productId: Int): Flow<CartItemEntity?>

    /**
     * Retrieves the count of unique items in the cart.
     * Used for the cart badge in the toolbar.
     * @return Flow<Int> - reactive stream of total item count
     */
    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): Flow<Int>

    /**
     * Retrieves the sum of all item quantities in the cart.
     * Differs from getCartItemCount() which returns unique items count.
     * @return Flow<Int> - total quantity (sum of all quantities)
     */
    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun getTotalQuantity(): Flow<Int>

    /**
     * Inserts a new cart item.
     * If an item with the same primary key exists, replaces it (OnConflictStrategy.REPLACE).
     * @param item The CartItemEntity to insert
     * @return The row ID of the inserted item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    /**
     * Updates an existing cart item.
     * Typically used to change the quantity.
     * @param item The CartItemEntity with updated values
     * @return Number of rows affected (should be 1 if successful)
     */
    @Update
    suspend fun updateCartItem(item: CartItemEntity): Int

    /**
     * Deletes a cart item by its product ID.
     * @param productId The product ID to remove from cart
     * @return Number of rows deleted
     */
    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItemByProductId(productId: Int): Int

    /**
     * Deletes a specific cart item entity.
     * @param item The CartItemEntity to delete
     * @return Number of rows deleted
     */
    @Delete
    suspend fun deleteCartItem(item: CartItemEntity): Int

    /**
     * Deletes all items from the cart.
     * Called after a successful order is placed.
     * @return Number of rows deleted
     */
    @Query("DELETE FROM cart_items")
    suspend fun clearCart(): Int

    /**
     * Checks if a product is already in the cart.
     * @param productId The product ID to check
     * @return Flow<Int> - emits 1 if exists, 0 if not
     */
    @Query("SELECT COUNT(*) FROM cart_items WHERE productId = :productId")
    fun isProductInCart(productId: Int): Flow<Int>
}
