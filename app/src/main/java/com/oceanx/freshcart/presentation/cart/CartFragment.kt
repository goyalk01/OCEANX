package com.oceanx.freshcart.presentation.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentCartBinding
import com.oceanx.freshcart.presentation.cart.adapter.CartAdapter

/**
 * CartFragment - displays shopping cart items and bill summary.
 */
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)

        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        setupAdapter()
        observeViewModel()
    }

    private fun setupAdapter() {
        cartAdapter = CartAdapter(
            onQuantityIncrease = { productId -> viewModel.increaseQuantity(productId, 1) },
            onQuantityDecrease = { productId -> viewModel.decreaseQuantity(productId, 1) },
            onRemove = { cartItem -> viewModel.removeItem(cartItem.productId) }
        )
        binding.cartRecyclerView.adapter = cartAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.cartItems.collect { items ->
                if (items.isEmpty()) {
                    binding.emptyCartLayout.visibility = View.VISIBLE
                    binding.cartRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyCartLayout.visibility = View.GONE
                    binding.cartRecyclerView.visibility = View.VISIBLE
                    cartAdapter.submitList(items)
                }
            }
        }
    }
}
