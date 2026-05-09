package com.oceanx.freshcart.utils

/**
 * Centralized input validation utilities.
 * Prevents duplication of validation logic across LoginViewModel and CheckoutViewModel.
 *
 * All methods are pure functions with no side effects — easy to test and explain in interviews.
 */
object ValidationUtils {

    /**
     * Validates an Indian phone number.
     *
     * Rules:
     * - Must be exactly 10 digits
     * - Must contain only numeric characters
     * - Must start with 6, 7, 8, or 9 (valid Indian mobile prefixes)
     *
     * @param phone The phone number string to validate
     * @return true if the phone number is valid, false otherwise
     */
    fun isValidIndianPhone(phone: String): Boolean {
        if (phone.length != Constants.PHONE_LENGTH) return false
        if (!phone.all { it.isDigit() }) return false
        val firstDigit = phone.first().digitToInt()
        return firstDigit in 6..9
    }

    /**
     * Validates a 6-digit Indian pincode.
     *
     * @param pincode The pincode string to validate
     * @return true if the pincode is exactly 6 digits, false otherwise
     */
    fun isValidPincode(pincode: String): Boolean {
        return pincode.length == 6 && pincode.all { it.isDigit() }
    }

    /**
     * Validates that a name meets minimum requirements.
     *
     * Rules:
     * - At least 3 characters
     * - Contains only letters and whitespace
     *
     * @param name The name to validate
     * @return null if valid, or an error message string if invalid
     */
    fun validateName(name: String): String? {
        return when {
            name.isEmpty() -> "Name is required"
            name.length < 3 -> "Name must be at least 3 characters"
            !name.all { it.isLetter() || it.isWhitespace() } -> "Name should contain only letters"
            else -> null
        }
    }

    /**
     * Validates a delivery address.
     *
     * @param address The address string to validate
     * @return null if valid, or an error message string if invalid
     */
    fun validateAddress(address: String): String? {
        val trimmed = address.trim()
        return when {
            trimmed.isEmpty() -> "Address is required"
            trimmed.length < 10 -> "Address must be at least 10 characters"
            else -> null
        }
    }
}
