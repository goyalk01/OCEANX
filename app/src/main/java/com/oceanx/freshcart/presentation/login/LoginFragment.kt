package com.oceanx.freshcart.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentLoginBinding
import com.oceanx.freshcart.utils.Constants
import com.oceanx.freshcart.utils.toast
import kotlinx.coroutines.launch

/**
 * LoginFragment - handles user authentication via phone and OTP.
 *
 * Flow:
 * 1. User enters 10-digit phone number
 * 2. User taps "Send OTP"
 * 3. OTP input section appears
 * 4. User enters 4-digit OTP (fake OTP is "1234")
 * 5. On successful verification, user is logged in and navigated to Home
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        // Initialize ViewModel with SharedPreferences
        val sharedPreferences = requireContext()
            .getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        
        val viewModelFactory = LoginViewModelFactory(sharedPreferences)
        val factory by lazy { viewModelFactory }
        viewModel = ViewModels(factory).create(LoginViewModel::class.java)

        // Check if already logged in
        if (viewModel.isLoggedIn()) {
            navigateToHome()
            return
        }

        setupUI()
        observeViewModel()
    }

    /**
     * Set up UI event listeners.
     */
    private fun setupUI() {
        binding.sendOtpButton.setOnClickListener {
            val phone = binding.phoneEditText.text.toString().trim()
            if (viewModel.sendOtp()) {
                binding.otpSection.visibility = View.VISIBLE
                binding.phoneInputLayout.isErrorEnabled = false
            }
        }

        binding.verifyOtpButton.setOnClickListener {
            val otp = binding.otpEditText.text.toString().trim()
            if (viewModel.verifyOtp(otp)) {
                navigateToHome()
            }
        }

        binding.resendOtpButton.setOnClickListener {
            viewModel.resendOtp()
        }

        binding.phoneEditText.addTextChangedListener(
            object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setPhoneNumber(s.toString())
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            }
        )
    }

    /**
     * Observe ViewModel state changes.
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                if (error != null) {
                    binding.phoneInputLayout.error = error
                    requireContext().toast(error)
                }
            }
        }
    }

    /**
     * Navigate to Home and clear login from back stack.
     */
    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }
}

/**
 * Factory for creating LoginViewModel with dependencies.
 */
class LoginViewModelFactory(private val sharedPreferences: android.content.SharedPreferences) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(sharedPreferences) as T
    }
}

/**
 * Helper to create ViewModels using a factory.
 */
class ViewModels(private val factory: androidx.lifecycle.ViewModelProvider.Factory) {
    inline fun <reified T : androidx.lifecycle.ViewModel> create(): T {
        return factory.create(T::class.java) as T
    }
}
