package com.oceanx.freshcart.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.freshcart.data.model.Product
import com.oceanx.freshcart.databinding.ItemProductBinding
import com.oceanx.freshcart.utils.toCurrencyString

/**
 * RecyclerView adapter for products in the home screen.
 * Displays product cards in a grid layout using ListAdapter + DiffUtil
 * for efficient, animated list updates.
 *
 * Currently shows a simple "Add" button. In a full implementation,
 * the button would toggle to quantity controls when the product is in the cart.
 *
 * @param onAddClick Callback when "Add" button is tapped
 * @param onQuantityChange Callback when quantity +/- is tapped (product, newQuantity)
 */
class ProductAdapter(
    private val onAddClick: (Product) -> Unit,
    private val onQuantityChange: (Product, Int) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productUnit.text = product.unit
            binding.productPrice.text = product.price.toCurrencyString()

            // TODO: Load image using Glide when product images are available
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
    }

    /**
     * DiffUtil callback for efficient product list updates.
     * Compares by product ID (stable identity) and full equality (content changes).
     */
    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
