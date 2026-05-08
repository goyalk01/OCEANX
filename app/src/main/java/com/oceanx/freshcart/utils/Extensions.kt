package com.oceanx.freshcart.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Kotlin extension functions for common View operations.
 * These extensions reduce boilerplate and make code more readable.
 */

// ===== View Visibility Extensions =====

/**
 * Sets the view visibility to VISIBLE.
 * Cleaner than View.setVisibility(View.VISIBLE)
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Sets the view visibility to GONE.
 * Cleaner than View.setVisibility(View.GONE)
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Sets the view visibility to INVISIBLE.
 * View takes space but is not drawn.
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Toggles between VISIBLE and GONE based on a condition.
 * @param isVisible If true, shows the view; if false, hides it
 */
fun View.setVisibilityByCondition(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

// ===== Toast Extensions =====

/**
 * Shows a short toast message.
 * @param message The message to display
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Shows a long toast message.
 * @param message The message to display
 */
fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Fragment-scoped toast helper.
 */
fun Fragment.toast(message: String) {
    requireContext().toast(message)
}

// ===== Currency Formatting Extension =====

/**
 * Formats a Double value as a currency string with the Indian Rupee symbol.
 * Example: 40.0 → "₹40"
 *          150.5 → "₹150"
 *
 * @return Formatted currency string
 */
fun Double.toCurrencyString(): String {
    return "₹${String.format("%.0f", this)}"
}

/**
 * Formats a Double value as a currency string with decimals.
 * Example: 40.0 → "₹40.00"
 *          150.5 → "₹150.50"
 *
 * @return Formatted currency string with 2 decimal places
 */
fun Double.toCurrencyStringWithDecimals(): String {
    return "₹${String.format("%.2f", this)}"
}

// ===== Text Input Extensions =====

/**
 * Gets text from an EditText and trims whitespace.
 * This is used in many places to avoid repeated .text.toString().trim()
 */
fun android.widget.EditText.getTrimmedText(): String {
    return this.text.toString().trim()
}

/**
 * Clears text from an EditText and removes focus.
 */
fun android.widget.EditText.clear() {
    this.text.clear()
    this.clearFocus()
}
