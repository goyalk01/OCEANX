package com.oceanx.freshcart.presentation.checkout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentCheckoutBinding
import com.oceanx.freshcart.presentation.common.UiState

/**
 * CheckoutFragment - collects delivery address and processes order placement.
 */
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var viewModel: CheckoutViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCheckoutBinding.bind(view)

        viewModel = ViewModelProvider(this).get(CheckoutViewModel::class.java)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.placeOrderButton.setOnClickListener {
            // TODO: get total from viewModel.billBreakdown
            viewModel.placeOrder(0.0)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.orderPlacedState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.placeOrderButton.isEnabled = false
                        // Show loading indicator
                    }
                    is UiState.Success -> {
                        state.data?.let { order ->
                            // Navigate to order success
                            findNavController().navigate(
                                R.id.action_checkoutFragment_to_orderSuccessFragment,
                                Bundle().apply {
                                    putString("orderId", order.orderId)
                                    putDouble("totalAmount", order.totalAmount)
                                    putString("deliveryAddress", order.deliveryAddress)
                                    putString("paymentMethod", order.paymentMethod)
                                }
                            )
                        }
                    }
                    is UiState.Error -> {
                        binding.placeOrderButton.isEnabled = true
                        requireContext().toast(state.message)
                    }
                }
            }
        }
    }
}
