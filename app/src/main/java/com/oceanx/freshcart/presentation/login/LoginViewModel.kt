package com.oceanx.freshcart.presentation.login

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceanx.freshcart.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the LoginFragment.
 * Manages authentication state and OTP verification logic.
 *
 * State:
 * - phoneNumber: The entered phone number
 * - otpVisible: Whether the OTP input section should be shown
 * - loginSuccess: Whether login was successful
 * - errorMessage: Any error message to display
 */
class LoginViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _otpVisible = MutableStateFlow(false)
    val otpVisible: StateFlow<Boolean> = _otpVisible

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Update the entered phone number.
     * @param phone The phone number text
     */
    fun setPhoneNumber(phone: String) {
        _phoneNumber.value = phone
        _errorMessage.value = null
    }

    /**
     * Send OTP to the entered phone number.
     * Validates phone format, then shows OTP input section.
     * @return true if validation passed, false otherwise
     */
    fun sendOtp(): Boolean {
        val phone = _phoneNumber.value.trim()

        // Validate phone number
        if (!isValidPhone(phone)) {
            _errorMessage.value = "Please enter a valid 10-digit Indian phone number"
            return false
        }

        // Show OTP section
        _otpVisible.value = true
        _errorMessage.value = null
        return true
    }

    /**
     * Verify OTP entered by user.
     * Compares with the fake OTP from Constants.
     * @param otp The OTP entered by user
     * @return true if OTP is correct, false otherwise
     */
    fun verifyOtp(otp: String): Boolean {
        val trimmedOtp = otp.trim()

        if (trimmedOtp == Constants.FAKE_OTP) {
            // Save login state
            saveLoginState(_phoneNumber.value)
            _loginSuccess.value = true
            _errorMessage.value = null
            return true
        } else {
            _errorMessage.value = "Incorrect OTP. Please try again"
            return false
        }
    }

    /**
     * Resend OTP (mock implementation).
     * In a real app, this would make an API call.
     */
    fun resendOtp() {
        viewModelScope.launch {
            _errorMessage.value = "OTP sent to +91${_phoneNumber.value}"
            // In a real app, would call backend API here
            // For now, just show a message
        }
    }

    /**
     * Validates phone number format (Indian phone numbers).
     * Rules: 10 digits, starts with 6, 7, 8, or 9
     */
    private fun isValidPhone(phone: String): Boolean {
        if (phone.length != Constants.PHONE_LENGTH) return false
        if (!phone.all { it.isDigit() }) return false
        val firstDigit = phone.first().digitToInt()
        return firstDigit in 6..9
    }

    /**
     * Saves login state to SharedPreferences.
     * @param phone The logged-in phone number
     */
    private fun saveLoginState(phone: String) {
        sharedPreferences.edit().apply {
            putString(Constants.SHARED_PREF_KEY_PHONE, phone)
            putBoolean(Constants.SHARED_PREF_KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    /**
     * Checks if user is already logged in.
     * Used by LoginFragment in onStart() to redirect to home if already logged in.
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(Constants.SHARED_PREF_KEY_IS_LOGGED_IN, false)
    }

    /**
     * Clears login state (logout).
     */
    fun logout() {
        sharedPreferences.edit().apply {
            putBoolean(Constants.SHARED_PREF_KEY_IS_LOGGED_IN, false)
            putString(Constants.SHARED_PREF_KEY_PHONE, "")
            apply()
        }
    }
}
