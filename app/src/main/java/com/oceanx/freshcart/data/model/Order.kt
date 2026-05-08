package com.oceanx.freshcart.data.model

/**
 * Domain model representing a completed order.
 *
 * @param orderId Unique order identifier (e.g., "ORD#4521")
 * @param items List of items ordered
 * @param deliveryAddress Full delivery address
 * @param paymentMethod Payment method used (e.g., "Cash on Delivery", "Card")
 * @param totalAmount Final amount paid
 * @param estimatedDeliveryTime Estimated time of delivery
 * @param orderPlacedTime Timestamp when order was placed
 */
data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val deliveryAddress: String,
    val paymentMethod: String,
    val totalAmount: Double,
    val estimatedDeliveryTime: String,
    val orderPlacedTime: String = ""
)
