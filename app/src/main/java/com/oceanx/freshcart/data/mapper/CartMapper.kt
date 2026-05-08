package com.oceanx.freshcart.data.mapper

import com.oceanx.freshcart.data.local.entity.CartItemEntity
import com.oceanx.freshcart.data.model.CartItem

/**
 * Mapper for converting between database entities and domain models.
 * This adheres to Clean Architecture by keeping database specifics separate from domain logic.
 *
 * The domain layer works with CartItem, while the data layer uses CartItemEntity.
 * This mapper bridges the two.
 */
object CartMapper {

    /**
     * Converts a Room CartItemEntity to a domain CartItem.
     * Used when reading from database.
     * @param entity The database entity
     * @return The domain model
     */
    fun entityToDomain(entity: CartItemEntity): CartItem {
        return CartItem(
            id = entity.id,
            productId = entity.productId,
            name = entity.name,
            price = entity.price,
            unit = entity.unit,
            quantity = entity.quantity,
            imageResId = entity.imageResId
        )
    }

    /**
     * Converts a domain CartItem to a Room CartItemEntity.
     * Used when writing to database.
     * @param cartItem The domain model
     * @return The database entity
     */
    fun domainToEntity(cartItem: CartItem): CartItemEntity {
        return CartItemEntity(
            id = cartItem.id,
            productId = cartItem.productId,
            name = cartItem.name,
            price = cartItem.price,
            unit = cartItem.unit,
            quantity = cartItem.quantity,
            imageResId = cartItem.imageResId
        )
    }

    /**
     * Converts a list of entities to domain models.
     * @param entities List of database entities
     * @return List of domain models
     */
    fun entitiesToDomain(entities: List<CartItemEntity>): List<CartItem> {
        return entities.map { entityToDomain(it) }
    }

    /**
     * Converts a list of domain models to entities.
     * @param cartItems List of domain models
     * @return List of database entities
     */
    fun domainToEntities(cartItems: List<CartItem>): List<CartItemEntity> {
        return cartItems.map { domainToEntity(it) }
    }
}
