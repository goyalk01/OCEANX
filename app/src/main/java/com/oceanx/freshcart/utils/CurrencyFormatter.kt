package com.oceanx.freshcart.utils

/**
 * Utility object for formatting currency values.
 * Provides consistent currency formatting across the entire app.
 */
object CurrencyFormatter {
    /**
     * Formats a Double as an Indian Rupee currency string.
     * @param amount The amount to format
     * @param includeDecimals If true, includes .00; if false, rounds to nearest rupee
     * @return Formatted string (e.g., "₹500" or "₹500.50")
     */
    fun format(amount: Double, includeDecimals: Boolean = false): String {
        return if (includeDecimals) {
            "₹${String.format("%.2f", amount)}"
        } else {
            "₹${String.format("%.0f", amount)}"
        }
    }

    /**
     * Formats a price for display in product cards.
     * @param price The product price
     * @return Formatted price without decimals for whole rupees
     */
    fun formatPrice(price: Double): String {
        return format(price, includeDecimals = false)
    }

    /**
     * Formats a total amount (e.g., bill total, cart total).
     * @param total The total amount
     * @return Formatted total with decimals
     */
    fun formatTotal(total: Double): String {
        return format(total, includeDecimals = true)
    }

    /**
     * Formats a discount or savings amount.
     * @param amount The discount/savings amount
     * @return Formatted amount in green-colored context
     */
    fun formatSavings(amount: Double): String {
        return format(amount, includeDecimals = false)
    }
}
