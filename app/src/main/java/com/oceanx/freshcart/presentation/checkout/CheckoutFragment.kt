package com.oceanx.freshcart.presentation.checkout

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
import com.oceanx.freshcart.databinding.FragmentCheckoutBinding
import com.oceanx.freshcart.presentation.common.UiState
import com.oceanx.freshcart.presentation.common.ViewModelFactory
import com.oceanx.freshcart.utils.toast
import com.oceanx.freshcart.utils.toCurrencyString
import kotlinx.coroutines.launch

/**
 * CheckoutFragment - collects delivery address and processes order placement.
 *
 * Features:
 * - Delivery address form with validation
 * - Payment method selection (COD / Online)
 * - Order summary with bill breakdown
 * - Loading state during order placement
 * - Navigation to success screen on completion
 */
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutViewModel by viewModels {
        val app = requireActivity().application as FreshCartApplication
        ViewModelFactory(app.cartRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckoutBinding.bind(view)

        setupUI()
        observeViewModel()
    }

    /**
     * Set up form input listeners and button click handlers.
     */
    private fun setupUI() {
        // Text change listeners for form fields
        binding.fullNameEdit.addTextChangedListener(SimpleTextWatcher { viewModel.setFullName(it) })
        binding.phoneEdit.addTextChangedListener(SimpleTextWatcher { viewModel.setPhoneNumber(it) })
        binding.flatEdit.addTextChangedListener(SimpleTextWatcher { viewModel.setFlatNumber(it) })
        binding.areaEdit.addTextChangedListener(SimpleTextWatcher { viewModel.setAreaLocality(it) })
        binding.pincodeEdit.addTextChangedListener(SimpleTextWatcher { viewModel.setPincode(it) })

        // Payment method selection
        binding.paymentMethodGroup.setOnCheckedChangeListener { _, checkedId ->
            val method = when (checkedId) {
                R.id.codRadio -> "Cash on Delivery"
                R.id.onlineRadio -> "Pay Online (UPI / Card)"
                else -> "Cash on Delivery"
            }
            viewModel.setPaymentMethod(method)
        }

        // Place order button
        binding.placeOrderButton.setOnClickListener {
            viewModel.placeOrder()
        }
    }

    /**
     * Observe ViewModel state changes using lifecycle-safe repeatOnLifecycle.
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe order placement state
                launch {
                    viewModel.orderPlacedState.collect { state ->
                        when (state) {
                            is UiState.Loading -> {
                                binding.placeOrderButton.isEnabled = false
                                binding.placeOrderButton.text = getString(R.string.loading)
                            }
                            is UiState.Success -> {
                                state.data?.let { order ->
                                    val action = CheckoutFragmentDirections.actionCheckoutFragmentToOrderSuccessFragment(
                                        orderId = order.orderId,
                                        totalAmount = order.totalAmount.toFloat(),
                                        deliveryAddress = order.deliveryAddress,
                                        paymentMethod = order.paymentMethod
                                    )
                                    findNavController().navigate(action)
                                }
                            }
                            is UiState.Error -> {
                                binding.placeOrderButton.isEnabled = true
                                binding.placeOrderButton.text = getString(R.string.checkout_place_order)
                                requireContext().toast(state.message)
                            }
                        }
                    }
                }

                // Observe bill breakdown for displaying total on button
                launch {
                    viewModel.billBreakdown.collect { bill ->
                        viewModel.setTotalAmount(bill.finalTotal)
                    }
                }

                // Observe validation errors
                launch {
                    viewModel.validationErrors.collect { errors ->
                        // Show/clear field-level errors if needed
                        if (errors.isNotEmpty()) {
                            requireContext().toast(getString(R.string.checkout_validation_error))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * Simplified TextWatcher that only cares about text changes.
 * Reduces boilerplate from implementing all 3 methods of TextWatcher.
 */
private class SimpleTextWatcher(
    private val onChanged: (String) -> Unit
) : android.text.TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onChanged(s?.toString().orEmpty())
    }
    override fun afterTextChanged(s: android.text.Editable?) {}
}
