package com.oceanx.freshcart.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceanx.freshcart.R
import com.oceanx.freshcart.databinding.FragmentHomeBinding
import com.oceanx.freshcart.presentation.home.adapter.CategoryAdapter
import com.oceanx.freshcart.presentation.home.adapter.ProductAdapter

/**
 * HomeFragment - displays products and categories for browsing.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupRecyclerViews()
        observeViewModel()
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
        }

        // Product adapter (2-column grid)
        productAdapter = ProductAdapter(
            onAddClick = { product -> viewModel.addToCart(product) },
            onQuantityChange = { product, qty -> viewModel.updateCartQuantity(product.id, qty) },
            getCartItem = { productId -> null } // TODO: implement
        )
        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        // Observe products
        lifecycleScope.launchWhenStarted {
            viewModel.products.collect { products ->
                productAdapter.submitList(products)
            }
        }

        // Observe categories
        lifecycleScope.launchWhenStarted {
            viewModel.categories.collect { categories ->
                categoryAdapter.submitList(categories)
            }
        }
    }
}
