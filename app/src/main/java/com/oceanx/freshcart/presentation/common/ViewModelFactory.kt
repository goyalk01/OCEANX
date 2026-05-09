package com.oceanx.freshcart.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oceanx.freshcart.domain.repository.CartRepository
import com.oceanx.freshcart.domain.usecase.AddToCartUseCase
import com.oceanx.freshcart.domain.usecase.ClearCartUseCase
import com.oceanx.freshcart.domain.usecase.GetCartItemsUseCase
import com.oceanx.freshcart.domain.usecase.GetCartTotalUseCase
import com.oceanx.freshcart.domain.usecase.PlaceOrderUseCase
import com.oceanx.freshcart.domain.usecase.RemoveFromCartUseCase
import com.oceanx.freshcart.domain.usecase.UpdateCartQuantityUseCase
import com.oceanx.freshcart.presentation.cart.CartViewModel
import com.oceanx.freshcart.presentation.checkout.CheckoutViewModel
import com.oceanx.freshcart.presentation.home.HomeViewModel

/**
 * ViewModelFactory that provides all app ViewModels with their required dependencies.
 *
 * Without this factory, ViewModelProvider cannot instantiate ViewModels that have
 * constructor parameters. This is the standard approach for manual dependency injection
 * (without Hilt/Koin).
 *
 * For a production app, consider migrating to Hilt for automatic ViewModel injection.
 *
 * @param cartRepository The shared CartRepository instance from FreshCartApplication
 */
class ViewModelFactory(
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {

    // Create use cases lazily — only instantiated when the corresponding ViewModel is requested
    private val getCartItemsUseCase by lazy { GetCartItemsUseCase(cartRepository) }
    private val addToCartUseCase by lazy { AddToCartUseCase(cartRepository) }
    private val updateCartQuantityUseCase by lazy { UpdateCartQuantityUseCase(cartRepository) }
    private val removeFromCartUseCase by lazy { RemoveFromCartUseCase(cartRepository) }
    private val clearCartUseCase by lazy { ClearCartUseCase(cartRepository) }
    private val getCartTotalUseCase by lazy { GetCartTotalUseCase(cartRepository, getCartItemsUseCase) }
    private val placeOrderUseCase by lazy { PlaceOrderUseCase(getCartItemsUseCase, clearCartUseCase) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(cartRepository, addToCartUseCase) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(
                    getCartItemsUseCase,
                    getCartTotalUseCase,
                    updateCartQuantityUseCase,
                    removeFromCartUseCase,
                    clearCartUseCase,
                    cartRepository
                ) as T
            }
            modelClass.isAssignableFrom(CheckoutViewModel::class.java) -> {
                CheckoutViewModel(getCartTotalUseCase, placeOrderUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
