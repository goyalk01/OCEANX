package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use Case: Calculate the total bill amount for the cart.
 * Includes discounts and delivery fees based on subtotal.
 *
 * Rules:
 * - Apply 10% discount if subtotal > ₹500
 * - Apply FREE delivery if (subtotal - discount) > ₹500
 * - Otherwise, add ₹30 delivery fee
 *
 * @param repository The repository providing cart operations
 * @param getCartItemsUseCase Use case to fetch cart items
 */
class GetCartTotalUseCase(
    private val repository: CartRepository,
    private val getCartItemsUseCase: GetCartItemsUseCase
) {

    /**
     * Calculates and returns the total bill breakdown.
     * @return Flow<BillBreakdown> - emits bill details whenever cart changes
     */
    operator fun invoke(): Flow<BillBreakdown> {
        return getCartItemsUseCase().map { cartItems ->
            val mrpTotal = cartItems.sumOf { it.getTotalPrice() }
            val discount = if (mrpTotal > 500) {
                (mrpTotal * 10) / 100
            } else {
                0.0
            }
            val subtotalAfterDiscount = mrpTotal - discount
            val deliveryFee = if (subtotalAfterDiscount > 500) 0.0 else 30.0
            val finalTotal = subtotalAfterDiscount + deliveryFee

            BillBreakdown(
                mrpTotal = mrpTotal,
                discount = discount,
                deliveryFee = deliveryFee,
                finalTotal = finalTotal
            )
        }
    }

    /**
     * Data class representing the bill breakdown for display.
     */
    data class BillBreakdown(
        val mrpTotal: Double,
        val discount: Double,
        val deliveryFee: Double,
        val finalTotal: Double
    ) {
        val isFreeDelivery: Boolean
            get() = deliveryFee == 0.0

        val hasDiscount: Boolean
            get() = discount > 0.0
    }
}
