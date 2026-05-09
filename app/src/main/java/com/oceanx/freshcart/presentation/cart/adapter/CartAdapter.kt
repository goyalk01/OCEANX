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
 * Uses ListAdapter + DiffUtil for efficient, animated list updates.
 *
 * @param onQuantityIncrease Callback when "+" button is tapped, receives productId
 * @param onQuantityDecrease Callback when "-" button is tapped, receives productId
 * @param onRemove Callback when trash/remove button is tapped, receives CartItem for undo support
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
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Set up click listeners in init instead of bind() to avoid
         * re-creating listeners on every rebind (RecyclerView recycles views).
         * Uses bindingAdapterPosition for safe position access.
         */
        init {
            binding.cartQuantityPlus.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onQuantityIncrease(getItem(position).productId)
                }
            }

            binding.cartQuantityMinus.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onQuantityDecrease(getItem(position).productId)
                }
            }

            binding.cartRemoveButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRemove(getItem(position))
                }
            }
        }

        fun bind(cartItem: CartItem) {
            binding.cartItemName.text = cartItem.name
            binding.cartItemUnit.text = cartItem.unit
            binding.cartItemPrice.text = cartItem.price.toCurrencyString()
            binding.cartItemQuantity.text = cartItem.quantity.toString()
            binding.cartItemTotal.text = cartItem.getTotalPrice().toCurrencyString()

            // TODO: Load image using Glide when product images are available
            // Glide.with(itemView).load(cartItem.imageResId).into(binding.cartItemImage)
        }
    }

    /**
     * DiffUtil callback for efficient cart item list updates.
     * Uses productId as stable identity since each product appears only once in cart.
     */
    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
