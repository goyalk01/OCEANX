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
 * - Receives CartMapper for entity ↔ domain model conversion
 * - Implements all CartRepository methods
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

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        val existingItem = cartDao.getCartItemByProductId(productId).map { entity ->
            entity?.let {
                CartMapper.entityToDomain(it).copy(quantity = quantity)
            }
        }.map { cartItem ->
            cartItem?.let { CartMapper.domainToEntity(it) }
        }

        // We need to get the existing item first
        // In a real scenario, this might be optimized with a direct UPDATE query
        val currentCartItem = getCartItemByProductId(productId)
        currentCartItem.collect { item ->
            if (item != null) {
                val updatedItem = item.copy(quantity = quantity)
                val entity = CartMapper.domainToEntity(updatedItem)
                cartDao.updateCartItem(entity)
            }
        }
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
