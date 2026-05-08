package com.oceanx.freshcart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room @Entity representing a cart item row in the SQLite database.
 * Room automatically creates a table named "cart_items" with these columns.
 *
 * This entity is only used for persistence. The domain layer uses CartItem model.
 * Mappers handle conversion between CartItemEntity ↔ CartItem.
 */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val productId: Int,
    val name: String,
    val price: Double,
    val unit: String,
    val quantity: Int = 1,
    val imageResId: String
)
