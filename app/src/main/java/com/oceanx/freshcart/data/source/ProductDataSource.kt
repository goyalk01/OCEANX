package com.oceanx.freshcart.data.source

import com.oceanx.freshcart.data.model.Category
import com.oceanx.freshcart.data.model.Product

/**
 * Fake data source for products and categories.
 * In a production app, this data would come from a real backend API.
 * For now, we provide hardcoded data for development and demonstration.
 *
 * Data structure:
 * - 8 categories (All, Fruits, Vegetables, Dairy, Bakery, Beverages, Snacks, Grains)
 * - 20+ products distributed across categories
 */
object ProductDataSource {

    /**
     * List of all product categories.
     */
    fun getCategories(): List<Category> {
        return listOf(
            Category(1, "All"),
            Category(2, "Fruits"),
            Category(3, "Vegetables"),
            Category(4, "Dairy"),
            Category(5, "Bakery"),
            Category(6, "Beverages"),
            Category(7, "Snacks"),
            Category(8, "Grains")
        )
    }

    /**
     * List of all products with realistic grocery items.
     * Each product has: id, name, category, price (in ₹), unit, image, description
     */
    fun getProducts(): List<Product> {
        return listOf(
            // Fruits (IDs 1-5)
            Product(
                id = 1,
                name = "Banana",
                category = "Fruits",
                price = 40.0,
                unit = "1 Dozen",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh yellow bananas, ripe and ready to eat"
            ),
            Product(
                id = 2,
                name = "Apple",
                category = "Fruits",
                price = 120.0,
                unit = "1 kg",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Crisp red apples, sweet and juicy"
            ),
            Product(
                id = 3,
                name = "Mango",
                category = "Fruits",
                price = 80.0,
                unit = "1 kg",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "King of fruits, seasonal special"
            ),
            Product(
                id = 4,
                name = "Grapes",
                category = "Fruits",
                price = 60.0,
                unit = "500g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh sweet grapes"
            ),
            Product(
                id = 5,
                name = "Watermelon",
                category = "Fruits",
                price = 35.0,
                unit = "1 Piece",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Large, sweet watermelon"
            ),

            // Vegetables (IDs 6-10)
            Product(
                id = 6,
                name = "Tomato",
                category = "Vegetables",
                price = 30.0,
                unit = "500g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh red tomatoes"
            ),
            Product(
                id = 7,
                name = "Onion",
                category = "Vegetables",
                price = 25.0,
                unit = "1 kg",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Golden onions, fresh and pungent"
            ),
            Product(
                id = 8,
                name = "Potato",
                category = "Vegetables",
                price = 20.0,
                unit = "1 kg",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh potatoes"
            ),
            Product(
                id = 9,
                name = "Spinach",
                category = "Vegetables",
                price = 15.0,
                unit = "1 Bunch",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh leafy spinach"
            ),
            Product(
                id = 10,
                name = "Carrot",
                category = "Vegetables",
                price = 40.0,
                unit = "500g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Orange carrots, crunchy and sweet"
            ),

            // Dairy (IDs 11-15)
            Product(
                id = 11,
                name = "Full Cream Milk",
                category = "Dairy",
                price = 60.0,
                unit = "1 L",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh full cream milk"
            ),
            Product(
                id = 12,
                name = "Paneer",
                category = "Dairy",
                price = 85.0,
                unit = "200g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh cottage cheese (paneer)"
            ),
            Product(
                id = 13,
                name = "Curd",
                category = "Dairy",
                price = 45.0,
                unit = "400g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh yogurt (curd)"
            ),
            Product(
                id = 14,
                name = "Butter",
                category = "Dairy",
                price = 55.0,
                unit = "100g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Pure butter"
            ),
            Product(
                id = 15,
                name = "Cheese",
                category = "Dairy",
                price = 110.0,
                unit = "200g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Processed cheese slices"
            ),

            // Bakery (IDs 16-18)
            Product(
                id = 16,
                name = "Bread",
                category = "Bakery",
                price = 45.0,
                unit = "1 Loaf",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh white bread"
            ),
            Product(
                id = 17,
                name = "Bun",
                category = "Bakery",
                price = 20.0,
                unit = "6 Pieces",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Soft buns"
            ),
            Product(
                id = 18,
                name = "Croissant",
                category = "Bakery",
                price = 35.0,
                unit = "2 Pieces",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Buttery croissants"
            ),

            // Beverages (IDs 19-20)
            Product(
                id = 19,
                name = "Orange Juice",
                category = "Beverages",
                price = 75.0,
                unit = "1 L",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh orange juice"
            ),
            Product(
                id = 20,
                name = "Coconut Water",
                category = "Beverages",
                price = 40.0,
                unit = "1 Piece",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Fresh coconut water"
            ),

            // Snacks (IDs 21-23)
            Product(
                id = 21,
                name = "Chips",
                category = "Snacks",
                price = 50.0,
                unit = "200g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Crispy potato chips"
            ),
            Product(
                id = 22,
                name = "Biscuits",
                category = "Snacks",
                price = 30.0,
                unit = "200g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Sweet biscuits"
            ),
            Product(
                id = 23,
                name = "Cookies",
                category = "Snacks",
                price = 60.0,
                unit = "300g",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Chocolate cookies"
            ),

            // Grains (IDs 24-25)
            Product(
                id = 24,
                name = "Rice",
                category = "Grains",
                price = 50.0,
                unit = "1 kg",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Basmati rice"
            ),
            Product(
                id = 25,
                name = "Wheat Flour",
                category = "Grains",
                price = 40.0,
                unit = "1 kg",
                imageResId = "@drawable/ic_launcher_foreground",
                description = "Whole wheat flour"
            )
        )
    }

    /**
     * Filters products by category.
     * @param category Category name to filter by, or null to get all
     * @return Filtered list of products
     */
    fun getProductsByCategory(category: String?): List<Product> {
        return if (category == null || category == "All") {
            getProducts()
        } else {
            getProducts().filter { it.category == category }
        }
    }

    /**
     * Searches products by name (case-insensitive).
     * @param query Search query string
     * @return Products matching the search query
     */
    fun searchProducts(query: String): List<Product> {
        return if (query.isEmpty()) {
            getProducts()
        } else {
            getProducts().filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)
            }
        }
    }
}
