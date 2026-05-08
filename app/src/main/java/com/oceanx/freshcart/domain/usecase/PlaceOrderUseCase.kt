package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.data.model.Order
import com.oceanx.freshcart.utils.OrderIdGenerator
import kotlinx.coroutines.flow.first

/**
 * Use Case: Place an order with the cart items.
 * Generates order ID and prepares order data for display.
 *
 * @param getCartItemsUseCase Use case to fetch current cart items
 * @param clearCartUseCase Use case to clear cart after order (called separately)
 */
class PlaceOrderUseCase(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val clearCartUseCase: ClearCartUseCase
) {

    /**
     * Data class for order placement request.
     */
    data class PlaceOrderRequest(
        val deliveryAddress: String,
        val paymentMethod: String,
        val totalAmount: Double
    )

    /**
     * Places an order with current cart items.
     * @param request Order placement details
     * @return Order object with generated order ID
     */
    suspend operator fun invoke(request: PlaceOrderRequest): Order {
        val cartItems = getCartItemsUseCase().first()
        val orderId = OrderIdGenerator.generate()

        val order = Order(
            orderId = orderId,
            items = cartItems,
            deliveryAddress = request.deliveryAddress,
            paymentMethod = request.paymentMethod,
            totalAmount = request.totalAmount,
            estimatedDeliveryTime = "30 - 40 minutes"
        )

        // Clear cart after successful order
        clearCartUseCase()

        return order
    }
}
