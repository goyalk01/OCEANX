package com.oceanx.freshcart.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.data.model.Product
import com.oceanx.freshcart.databinding.ItemProductBinding
import com.oceanx.freshcart.utils.toCurrencyString

/**
 * RecyclerView adapter for products in the home screen.
 * Displays product cards in a grid or list layout.
 * Uses DiffUtil for efficient list updates.
 *
 * Shows either:
 * - "+" Add button if product not in cart
 * - Quantity controls (+/-) if product already in cart
 *
 * @param onAddClick Callback when "Add" button is tapped
 * @param onQuantityChange Callback when quantity +/- is tapped
 * @param getCartItem Function to get current cart item (quantity) if it exists
 */
class ProductAdapter(
    private val onAddClick: (Product) -> Unit,
    private val onQuantityChange: (Product, Int) -> Unit,
    private val getCartItem: suspend (Int) -> CartItem?
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding, onAddClick, onQuantityChange)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val onAddClick: (Product) -> Unit,
        private val onQuantityChange: (Product, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productUnit.text = product.unit
            binding.productPrice.text = product.price.toCurrencyString()

            // TODO: Load image using Glide
            // Glide.with(itemView).load(product.imageResId).into(binding.productImage)

            binding.addButton.setOnClickListener {
                onAddClick(product)
            }

            binding.quantityMinusButton.setOnClickListener {
                val currentQty = binding.quantityText.text.toString().toIntOrNull() ?: 1
                if (currentQty > 1) {
                    onQuantityChange(product, currentQty - 1)
                }
            }

            binding.quantityPlusButton.setOnClickListener {
                val currentQty = binding.quantityText.text.toString().toIntOrNull() ?: 1
                onQuantityChange(product, currentQty + 1)
            }
        }

        /**
         * Update the quantity display.
         */
        fun setQuantity(quantity: Int) {
            binding.quantityText.text = quantity.toString()
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
