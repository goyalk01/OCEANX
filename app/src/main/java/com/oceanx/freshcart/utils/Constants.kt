package com.oceanx.freshcart.utils

/**
 * Application-wide constants.
 * This object holds all magic strings, numbers, and configurations
 * to avoid hardcoding values throughout the codebase.
 */
object Constants {
    // Authentication & Login
    const val FAKE_OTP = "1234"
    const val SHARED_PREF_NAME = "com.oceanx.freshcart.prefs"
    const val SHARED_PREF_KEY_PHONE = "phone_number"
    const val SHARED_PREF_KEY_IS_LOGGED_IN = "is_logged_in"

    // Delivery & Pricing
    const val DELIVERY_FEE = 30.0
    const val FREE_DELIVERY_THRESHOLD = 500.0
    const val DISCOUNT_PERCENTAGE = 10
    const val DISCOUNT_THRESHOLD = 500.0

    // Phone number validation
    const val PHONE_LENGTH = 10

    // Database
    const val DB_NAME = "freshcart.db"
    const val DB_VERSION = 1

    // Timing
    const val OTP_RESEND_DELAY_MS = 30000L
    const val CHECKOUT_LOADING_DELAY_MS = 1500L
    const val SEARCH_DEBOUNCE_MS = 300L

    // Category names
    val DEFAULT_CATEGORIES = listOf(
        "All",
        "Fruits",
        "Vegetables",
        "Dairy",
        "Bakery",
        "Beverages",
        "Snacks",
        "Grains"
    )

    // Miscellaneous
    const val ORDER_ID_PREFIX = "ORD#"
    const val ESTIMATED_DELIVERY_TIME = "30 - 40 minutes"
}
