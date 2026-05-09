package com.oceanx.freshcart.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceanx.freshcart.data.model.Category
import com.oceanx.freshcart.databinding.ItemCategoryBinding

/**
 * RecyclerView adapter for product categories.
 * Displays horizontal scrollable category chips.
 * Uses ListAdapter + DiffUtil for efficient list updates.
 *
 * @param onCategoryClick Callback when a category chip is tapped
 * @param isSelected Function to check if a category is currently selected
 */
class CategoryAdapter(
    private val onCategoryClick: (String) -> Unit,
    private val isSelected: (String) -> Boolean
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                // Use bindingAdapterPosition instead of deprecated adapterPosition.
                // bindingAdapterPosition returns the position relative to this adapter,
                // which is correct when using ConcatAdapter or nested adapters.
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCategoryClick(getItem(position).name)
                }
            }
        }

        fun bind(category: Category) {
            binding.categoryName.text = category.name

            // Update visual state based on selection
            val selected = isSelected(category.name)
            binding.root.isSelected = selected
        }
    }

    /**
     * DiffUtil callback for efficient category list updates.
     */
    private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}
