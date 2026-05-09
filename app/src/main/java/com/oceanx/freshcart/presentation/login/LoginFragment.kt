package com.oceanx.freshcart.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    /**
     * ViewModel instantiation using the `by viewModels` delegate with a custom factory.
     * This is the correct, lifecycle-aware way to create ViewModels that have constructor
     * dependencies (SharedPreferences in this case).
     */
    private val viewModel: LoginViewModel by viewModels {
        val sharedPreferences = requireContext()
            .getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        LoginViewModelFactory(sharedPreferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

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
     * Observe ViewModel state changes using lifecycle-safe repeatOnLifecycle.
     * This replaces the deprecated launchWhenStarted and ensures collection
     * is properly cancelled when the view goes below STARTED state.
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect { error ->
                    if (error != null) {
                        binding.phoneInputLayout.error = error
                        requireContext().toast(error)
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * Factory for creating LoginViewModel with dependencies.
 * Required because LoginViewModel has a constructor parameter (SharedPreferences).
 * Without this factory, ViewModelProvider cannot instantiate the ViewModel.
 */
class LoginViewModelFactory(
    private val sharedPreferences: android.content.SharedPreferences
) : androidx.lifecycle.ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
