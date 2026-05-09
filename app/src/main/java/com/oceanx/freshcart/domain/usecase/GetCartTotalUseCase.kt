package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.domain.repository.CartRepository
import com.oceanx.freshcart.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use Case: Calculate the total bill amount for the cart.
 * Includes discounts and delivery fees based on subtotal.
 *
 * Business Rules (from Constants):
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
     * Emits a new BillBreakdown every time the cart changes (reactive via Flow).
     * @return Flow<BillBreakdown> - emits bill details whenever cart changes
     */
    operator fun invoke(): Flow<BillBreakdown> {
        return getCartItemsUseCase().map { cartItems ->
            val mrpTotal = cartItems.sumOf { it.getTotalPrice() }

            val discount = if (mrpTotal > Constants.DISCOUNT_THRESHOLD) {
                (mrpTotal * Constants.DISCOUNT_PERCENTAGE) / 100.0
            } else {
                0.0
            }

            val subtotalAfterDiscount = mrpTotal - discount

            val deliveryFee = if (subtotalAfterDiscount > Constants.FREE_DELIVERY_THRESHOLD) {
                0.0
            } else {
                Constants.DELIVERY_FEE
            }

            val finalTotal = subtotalAfterDiscount + deliveryFee

            BillBreakdown(
                mrpTotal = mrpTotal,
                discount = discount,
                deliveryFee = deliveryFee,
                finalTotal = finalTotal,
                itemCount = cartItems.size
            )
        }
    }

    /**
     * Data class representing the bill breakdown for display.
     * Contains all pricing components needed by the cart and checkout screens.
     */
    data class BillBreakdown(
        val mrpTotal: Double,
        val discount: Double,
        val deliveryFee: Double,
        val finalTotal: Double,
        val itemCount: Int = 0
    ) {
        /** True if delivery is free (subtotal exceeds threshold) */
        val isFreeDelivery: Boolean
            get() = deliveryFee == 0.0

        /** True if a discount is applied (subtotal exceeds discount threshold) */
        val hasDiscount: Boolean
            get() = discount > 0.0

        /** True if the cart has no items */
        val isEmpty: Boolean
            get() = itemCount == 0
    }
}
