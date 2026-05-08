package com.oceanx.freshcart

import android.app.Application
import com.oceanx.freshcart.data.local.AppDatabase
import com.oceanx.freshcart.data.repository.CartRepositoryImpl
import com.oceanx.freshcart.domain.repository.CartRepository

/**
 * Application class for FreshCart app.
 * Called once when the app process is created.
 *
 * Responsibilities:
 * - Initialize singletons (database, repository, etc.)
 * - Set up app-wide configuration
 * - Create dependency injection container (or prepare for DI framework)
 *
 * Note: For a production app, consider using Hilt or Koin for dependency injection.
 */
class FreshCartApplication : Application() {

    // Lazy-initialize the database singleton
    private val database by lazy { AppDatabase.getInstance(this) }

    // Lazy-initialize the repository singleton
    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(database.cartDao())
    }

    override fun onCreate() {
        super.onCreate()
        // App initialization code here
        // - Initialize analytics
        // - Set up crash reporting
        // - Configure networking
    }

    companion object {
        @Volatile
        private var instance: FreshCartApplication? = null

        /**
         * Get the application instance (singleton).
         */
        fun getInstance(): FreshCartApplication {
            return instance ?: error("FreshCartApplication not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Initialize app-wide resources
    }
}
