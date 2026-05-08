package com.oceanx.freshcart.presentation.ordersuccess

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentOrderSuccessBinding
import com.oceanx.freshcart.utils.toast

/**
 * OrderSuccessFragment - displays order confirmation and delivery tracking.
 */
class OrderSuccessFragment : Fragment(R.layout.fragment_order_success) {

    private lateinit var binding: FragmentOrderSuccessBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderSuccessBinding.bind(view)

        // Get arguments
        val orderId = arguments?.getString("orderId") ?: "ORD#0000"
        val totalAmount = arguments?.getDouble("totalAmount") ?: 0.0
        val deliveryAddress = arguments?.getString("deliveryAddress") ?: ""
        val paymentMethod = arguments?.getString("paymentMethod") ?: ""

        // Display order details
        binding.orderIdValue.text = orderId
        binding.totalAmountValue.text = "₹${String.format("%.0f", totalAmount)}"
        binding.deliveryAddressValue.text = deliveryAddress
        binding.paymentMethodValue.text = paymentMethod

        // Set up buttons
        binding.trackOrderButton.setOnClickListener {
            requireContext().toast("Tracking feature coming soon!")
        }

        binding.continueShoppingButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_orderSuccessFragment_to_homeFragment
            )
        }
    }
}
