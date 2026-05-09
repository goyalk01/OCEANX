package com.oceanx.freshcart.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceanx.freshcart.FreshCartApplication
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentHomeBinding
import com.oceanx.freshcart.presentation.common.ViewModelFactory
import com.oceanx.freshcart.presentation.home.adapter.CategoryAdapter
import com.oceanx.freshcart.presentation.home.adapter.ProductAdapter
import kotlinx.coroutines.launch

/**
 * HomeFragment - displays products and categories for browsing.
 *
 * Features:
 * - Horizontal scrollable category chips for filtering
 * - 2-column product grid with add-to-cart functionality
 * - Dynamic greeting based on time of day
 * - Reactive updates when cart state changes
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    /**
     * ViewModel created via custom factory because HomeViewModel requires
     * CartRepository and AddToCartUseCase as constructor dependencies.
     */
    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as FreshCartApplication
        ViewModelFactory(app.cartRepository)
    }

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupGreeting()
        setupRecyclerViews()
        observeViewModel()
    }

    /**
     * Set the time-based greeting text.
     */
    private fun setupGreeting() {
        binding.greetingText.text = viewModel.getTimeBasedGreeting()
    }

    private fun setupRecyclerViews() {
        // Category adapter
        categoryAdapter = CategoryAdapter(
            onCategoryClick = { category -> viewModel.onCategorySelected(category) },
            isSelected = { category -> viewModel.selectedCategory.value == category }
        )
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

        // Product adapter (2-column grid)
        productAdapter = ProductAdapter(
            onAddClick = { product -> viewModel.addToCart(product) },
            onQuantityChange = { product, qty -> viewModel.updateCartQuantity(product.id, qty) }
        )
        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * Observe ViewModel state using lifecycle-safe repeatOnLifecycle.
     * Each Flow is collected in a separate coroutine to avoid blocking.
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Launch each collector in parallel within the lifecycle scope
                launch {
                    viewModel.products.collect { products ->
                        productAdapter.submitList(products)
                    }
                }

                launch {
                    viewModel.categories.collect { categories ->
                        categoryAdapter.submitList(categories)
                    }
                }

                launch {
                    viewModel.selectedCategory.collect {
                        // Re-notify adapter when selection changes so chips update their state
                        categoryAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear adapter references to prevent RecyclerView leak
        binding.categoryRecyclerView.adapter = null
        binding.productRecyclerView.adapter = null
        _binding = null
    }
}
