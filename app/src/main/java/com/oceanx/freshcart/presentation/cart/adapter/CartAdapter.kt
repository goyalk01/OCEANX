package com.oceanx.freshcart.presentation.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.freshcart.data.model.CartItem
import com.oceanx.freshcart.databinding.ItemCartBinding
import com.oceanx.freshcart.utils.toCurrencyString

/**
 * RecyclerView adapter for cart items.
 * Displays cart items with quantity controls and remove button.
 * Uses DiffUtil for efficient list updates.
 *
 * @param onQuantityIncrease Callback when "+" button is tapped
 * @param onQuantityDecrease Callback when "-" button is tapped
 * @param onRemove Callback when trash/remove button is tapped
 */
class CartAdapter(
    private val onQuantityIncrease: (Int) -> Unit,
    private val onQuantityDecrease: (Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding, onQuantityIncrease, onQuantityDecrease, onRemove)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding,
        private val onQuantityIncrease: (Int) -> Unit,
        private val onQuantityDecrease: (Int) -> Unit,
        private val onRemove: (CartItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.cartItemName.text = cartItem.name
            binding.cartItemUnit.text = cartItem.unit
            binding.cartItemPrice.text = cartItem.price.toCurrencyString()
            binding.cartItemQuantity.text = cartItem.quantity.toString()
            binding.cartItemTotal.text = cartItem.getTotalPrice().toCurrencyString()

            // TODO: Load image using Glide
            // Glide.with(itemView).load(cartItem.imageResId).into(binding.cartItemImage)

            binding.cartQuantityPlus.setOnClickListener {
                onQuantityIncrease(cartItem.productId)
            }

            binding.cartQuantityMinus.setOnClickListener {
                onQuantityDecrease(cartItem.productId)
            }

            binding.cartRemoveButton.setOnClickListener {
                onRemove(cartItem)
            }
        }
    }

    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
