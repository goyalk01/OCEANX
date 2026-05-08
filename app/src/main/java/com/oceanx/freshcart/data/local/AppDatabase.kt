package com.oceanx.freshcart.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oceanx.freshcart.data.local.dao.CartDao
import com.oceanx.freshcart.data.local.entity.CartItemEntity
import com.oceanx.freshcart.utils.Constants

/**
 * Room Database definition.
 * This is the main entry point for accessing the app's local SQLite database.
 *
 * @Database annotation tells Room:
 * - entities: List of @Entity classes (tables) in this database
 * - version: Schema version (increment when modifying entities)
 * - exportSchema: Whether to export the schema to version control (default false)
 */
@Database(
    entities = [CartItemEntity::class],
    version = Constants.DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to CartDao for all cart-related queries.
     */
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Returns a singleton instance of the database.
         * Uses double-checked locking to ensure thread safety.
         * Creates the database on first access using Room.databaseBuilder().
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }
        }

        /**
         * Creates the database with proper configuration.
         * Includes fallbackToDestructiveMigration() for v1 (no migrations needed yet).
         */
        private fun createDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                Constants.DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
