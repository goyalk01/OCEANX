package com.oceanx.freshcart.presentation.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.oceanx.freshcart.FreshCartApplication
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentCartBinding
import com.oceanx.freshcart.presentation.cart.adapter.CartAdapter
import com.oceanx.freshcart.presentation.common.ViewModelFactory
import com.oceanx.freshcart.utils.toCurrencyString
import kotlinx.coroutines.launch

/**
 * CartFragment - displays shopping cart items and bill summary.
 *
 * Features:
 * - List of cart items with quantity controls (+/-)
 * - Real-time bill breakdown (MRP, discount, delivery fee, total)
 * - Empty state with "Browse Products" CTA
 * - Proceed to checkout button
 */
class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels {
        val app = requireActivity().application as FreshCartApplication
        ViewModelFactory(app.cartRepository)
    }

    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        setupAdapter()
        setupClickListeners()
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

    private fun setupClickListeners() {
        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }

        binding.browseProductsButton.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
        }
    }

    /**
     * Observe cart items and bill breakdown using lifecycle-safe repeatOnLifecycle.
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe cart items
                launch {
                    viewModel.cartItems.collect { items ->
                        if (items.isEmpty()) {
                            binding.emptyCartLayout.visibility = View.VISIBLE
                            binding.cartRecyclerView.visibility = View.GONE
                            binding.checkoutButton.isEnabled = false
                        } else {
                            binding.emptyCartLayout.visibility = View.GONE
                            binding.cartRecyclerView.visibility = View.VISIBLE
                            binding.checkoutButton.isEnabled = true
                            cartAdapter.submitList(items)
                        }
                    }
                }

                // Observe bill breakdown
                launch {
                    viewModel.billBreakdown.collect { bill ->
                        binding.mrpTotalValue.text = bill.mrpTotal.toCurrencyString()
                        binding.discountValue.text = if (bill.hasDiscount) {
                            "-${bill.discount.toCurrencyString()}"
                        } else {
                            bill.discount.toCurrencyString()
                        }
                        binding.totalAmountValue.text = bill.finalTotal.toCurrencyString()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.cartRecyclerView.adapter = null
        _binding = null
    }
}
