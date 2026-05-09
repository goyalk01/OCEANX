package com.oceanx.freshcart.domain.usecase

import com.oceanx.freshcart.data.model.Order
import com.oceanx.freshcart.utils.Constants
import com.oceanx.freshcart.utils.OrderIdGenerator
import kotlinx.coroutines.flow.first

/**
 * Use Case: Place an order with the cart items.
 * Generates order ID, constructs Order object, and clears the cart.
 *
 * This is the core business logic for order placement. In a production app,
 * this would also communicate with a backend API.
 *
 * @param getCartItemsUseCase Use case to fetch current cart items
 * @param clearCartUseCase Use case to clear cart after order
 */
class PlaceOrderUseCase(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val clearCartUseCase: ClearCartUseCase
) {

    /**
     * Data class for order placement request.
     * Encapsulates all data needed from the checkout form.
     */
    data class PlaceOrderRequest(
        val deliveryAddress: String,
        val paymentMethod: String,
        val totalAmount: Double
    )

    /**
     * Places an order with current cart items.
     *
     * Steps:
     * 1. Get current cart items snapshot
     * 2. Generate a unique order ID
     * 3. Construct the Order object
     * 4. Clear the cart
     * 5. Return the completed Order
     *
     * @param request Order placement details from the checkout form
     * @return Order object with generated order ID
     * @throws IllegalStateException if the cart is empty
     */
    suspend operator fun invoke(request: PlaceOrderRequest): Order {
        val cartItems = getCartItemsUseCase().first()

        if (cartItems.isEmpty()) {
            throw IllegalStateException("Cannot place order with empty cart")
        }

        val orderId = OrderIdGenerator.generate()

        val order = Order(
            orderId = orderId,
            items = cartItems,
            deliveryAddress = request.deliveryAddress,
            paymentMethod = request.paymentMethod,
            totalAmount = request.totalAmount,
            estimatedDeliveryTime = Constants.ESTIMATED_DELIVERY_TIME
        )

        // Clear cart after successful order
        clearCartUseCase()

        return order
    }
}
