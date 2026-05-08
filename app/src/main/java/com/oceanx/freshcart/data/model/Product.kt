package com.oceanx.freshcart.data.model

/**
 * Domain model representing a Product in the app.
 * This is a pure Kotlin data class with no Android or Room annotations.
 * It's used throughout the domain and presentation layers.
 *
 * @param id Unique product identifier
 * @param name Product display name (e.g., "Apple", "Milk")
 * @param category Product category (e.g., "Fruits", "Dairy")
 * @param price Price in Indian Rupees
 * @param unit Unit of measurement (e.g., "1 kg", "500g", "1 dozen")
 * @param imageResId Drawable resource ID or URL for the product image
 * @param description Optional detailed description
 */
data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val unit: String,
    val imageResId: String, // Can be "@drawable/xxx" or a URL
    val description: String = ""
)
