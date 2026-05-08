package com.oceanx.freshcart.data.model

/**
 * Domain model representing a product category.
 *
 * @param id Unique category identifier
 * @param name Category name (e.g., "Fruits", "Dairy")
 * @param iconResId Drawable resource ID for the category icon
 */
data class Category(
    val id: Int,
    val name: String,
    val iconResId: String = ""
)
