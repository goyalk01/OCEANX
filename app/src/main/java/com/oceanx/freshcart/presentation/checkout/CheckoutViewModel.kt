package com.oceanx.freshcart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceanx.freshcart.data.model.Order
import com.oceanx.freshcart.domain.usecase.GetCartTotalUseCase
import com.oceanx.freshcart.domain.usecase.PlaceOrderUseCase
import com.oceanx.freshcart.presentation.common.UiState
import com.oceanx.freshcart.utils.Constants
import com.oceanx.freshcart.utils.ValidationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for CheckoutFragment.
 * Manages delivery address input, payment method selection, and order placement.
 *
 * State:
 * - Delivery address fields (name, phone, address, city, pincode)
 * - Payment method selection
 * - Cart total
 * - Validation state
 * - Order placement state
 */
class CheckoutViewModel(
    private val getCartTotalUseCase: GetCartTotalUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase
) : ViewModel() {

    // Delivery Address Fields
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _flatNumber = MutableStateFlow("")
    val flatNumber: StateFlow<String> = _flatNumber

    private val _areaLocality = MutableStateFlow("")
    val areaLocality: StateFlow<String> = _areaLocality

    private val _city = MutableStateFlow("Your City")
    val city: StateFlow<String> = _city

    private val _pincode = MutableStateFlow("")
    val pincode: StateFlow<String> = _pincode

    // Payment Method
    private val _paymentMethod = MutableStateFlow("Cash on Delivery")
    val paymentMethod: StateFlow<String> = _paymentMethod

    // Validation
    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors

    private val _allFieldsValid = MutableStateFlow(false)
    val allFieldsValid: StateFlow<Boolean> = _allFieldsValid

    // Bill
    val billBreakdown: Flow<GetCartTotalUseCase.BillBreakdown> = getCartTotalUseCase()

    // Cached total from bill breakdown observation
    private var cachedTotalAmount: Double = 0.0

    /**
     * Order placement state.
     *
     * Uses UiState<Order?> with an initial Success(null) to represent the "idle" state.
     * - Success(null) = idle, no order placed yet
     * - Loading = order is being placed
     * - Success(order) = order placed successfully
     * - Error = order placement failed
     */
    private val _orderPlacedState = MutableStateFlow<UiState<Order?>>(UiState.Success(null))
    val orderPlacedState: StateFlow<UiState<Order?>> = _orderPlacedState

    // Update Methods
    fun setFullName(name: String) {
        _fullName.value = name
        validateFields()
    }

    fun setPhoneNumber(phone: String) {
        _phoneNumber.value = phone
        validateFields()
    }

    fun setFlatNumber(flat: String) {
        _flatNumber.value = flat
        validateFields()
    }

    fun setAreaLocality(area: String) {
        _areaLocality.value = area
        validateFields()
    }

    fun setCity(c: String) {
        _city.value = c
        validateFields()
    }

    fun setPincode(pin: String) {
        _pincode.value = pin
        validateFields()
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    /**
     * Cache the total amount from bill breakdown observation in the Fragment.
     * This avoids needing to re-collect the Flow when placing the order.
     */
    fun setTotalAmount(amount: Double) {
        cachedTotalAmount = amount
    }

    /**
     * Validates all form fields using centralized ValidationUtils.
     * Updates _allFieldsValid and _validationErrors StateFlows.
     */
    private fun validateFields() {
        val errors = mutableMapOf<String, String>()

        // Validate Full Name
        ValidationUtils.validateName(_fullName.value)?.let {
            errors["fullName"] = it
        }

        // Validate Phone Number
        if (_phoneNumber.value.isEmpty()) {
            errors["phoneNumber"] = "Phone number is required"
        } else if (!ValidationUtils.isValidIndianPhone(_phoneNumber.value)) {
            errors["phoneNumber"] = "Enter a valid 10-digit Indian phone number"
        }

        // Validate Address
        val fullAddress = "${_flatNumber.value} ${_areaLocality.value}"
        ValidationUtils.validateAddress(fullAddress)?.let {
            errors["address"] = it
        }

        // Validate Pincode
        if (_pincode.value.isEmpty()) {
            errors["pincode"] = "Pincode is required"
        } else if (!ValidationUtils.isValidPincode(_pincode.value)) {
            errors["pincode"] = "Pincode must be exactly 6 digits"
        }

        _validationErrors.value = errors
        _allFieldsValid.value = errors.isEmpty()
    }

    /**
     * Place the order using the cached total amount from bill breakdown.
     * Shows loading state, simulates API delay, then navigates.
     */
    fun placeOrder() {
        if (!_allFieldsValid.value) {
            validateFields()
            return
        }

        viewModelScope.launch {
            try {
                _orderPlacedState.value = UiState.Loading

                // Simulate API call with delay
                delay(Constants.CHECKOUT_LOADING_DELAY_MS)

                val request = PlaceOrderUseCase.PlaceOrderRequest(
                    deliveryAddress = getFullAddress(),
                    paymentMethod = _paymentMethod.value,
                    totalAmount = cachedTotalAmount
                )

                val order = placeOrderUseCase(request)
                _orderPlacedState.value = UiState.Success(order)

            } catch (e: Exception) {
                _orderPlacedState.value = UiState.Error("Failed to place order: ${e.message}")
            }
        }
    }

    /**
     * Constructs the full delivery address string.
     */
    private fun getFullAddress(): String {
        return buildString {
            append(_fullName.value).append(", ")
            append(_flatNumber.value).append(", ")
            append(_areaLocality.value).append(", ")
            append(_city.value).append(" - ")
            append(_pincode.value)
        }
    }

    /**
     * Get a formatted delivery address (truncated for display).
     */
    fun getFormattedDeliveryAddress(maxLength: Int = 50): String {
        val fullAddress = getFullAddress()
        return if (fullAddress.length > maxLength) {
            fullAddress.take(maxLength) + "..."
        } else {
            fullAddress
        }
    }
}
