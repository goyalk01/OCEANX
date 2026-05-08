package com.oceanx.freshcart.utils

import kotlin.random.Random

/**
 * Utility object for generating unique order IDs.
 * Order IDs follow the format: "ORD#XXXX" where XXXX is a random 4-digit number.
 */
object OrderIdGenerator {
    /**
     * Generates a random order ID with the format "ORD#XXXX".
     * Example output: "ORD#7392", "ORD#1024", "ORD#9999"
     *
     * @return A unique order ID string
     */
    fun generate(): String {
        val randomNumber = Random.nextInt(1000, 10000) // Generates 4-digit number (1000-9999)
        return "${Constants.ORDER_ID_PREFIX}$randomNumber"
    }

    /**
     * Generates multiple order IDs (useful for testing).
     *
     * @param count The number of order IDs to generate
     * @return A list of unique order ID strings
     */
    fun generateMultiple(count: Int): List<String> {
        return (1..count).map { generate() }
    }
}
