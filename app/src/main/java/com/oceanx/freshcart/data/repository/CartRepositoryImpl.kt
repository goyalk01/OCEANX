package com.oceanx.freshcart.data.repository

import com.oceanx.freshcart.data.local.dao.CartDao
import com.oceanx.freshcart.data.mapper.CartMapper
import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of CartRepository interface.
 * Bridges the domain layer (which defines what should happen) with the data layer (how it happens).
 *
 * This class:
 * - Receives CartDao for database operations
 * - Uses CartMapper for entity ↔ domain model conversion
 * - Implements all CartRepository methods
 *
 * All suspend functions are safe to call from any coroutine context;
 * Room automatically dispatches queries to a background thread.
 *
 * @param cartDao The Data Access Object for cart database operations
 */
class CartRepositoryImpl(private val cartDao: CartDao) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            CartMapper.entitiesToDomain(entities)
        }
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDao.getCartItemCount()
    }

    override fun getTotalQuantity(): Flow<Int> {
        return cartDao.getTotalQuantity()
    }

    override suspend fun addOrUpdateCartItem(item: CartItem) {
        val entity = CartMapper.domainToEntity(item)
        cartDao.insertCartItem(entity)
    }

    /**
     * Updates the quantity of a cart item using a direct SQL UPDATE query.
     *
     * This is significantly more efficient than the previous approach which:
     * 1. Fetched the entity via Flow
     * 2. Mapped entity → domain model
     * 3. Created a copy with new quantity
     * 4. Mapped domain model → entity
     * 5. Called updateCartItem()
     *
     * Now it's a single SQL: UPDATE cart_items SET quantity = ? WHERE productId = ?
     */
    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        cartDao.updateQuantityByProductId(productId, quantity)
    }

    override suspend fun removeCartItem(productId: Int) {
        cartDao.deleteCartItemByProductId(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override fun isProductInCart(productId: Int): Flow<Boolean> {
        return cartDao.isProductInCart(productId).map { count ->
            count > 0
        }
    }

    override fun getCartItemByProductId(productId: Int): Flow<CartItem?> {
        return cartDao.getCartItemByProductId(productId).map { entity ->
            entity?.let { CartMapper.entityToDomain(it) }
        }
    }
}
