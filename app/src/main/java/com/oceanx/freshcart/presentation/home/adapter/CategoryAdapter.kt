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
 * Uses DiffUtil for efficient list updates.
 *
 * @param onCategoryClick Callback when a category is tapped
 * @param isSelected Function to check if a category is selected
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
        return CategoryViewHolder(binding, onCategoryClick, isSelected)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onCategoryClick: (String) -> Unit,
        private val isSelected: (String) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onCategoryClick(getItem(adapterPosition).name)
            }
        }

        fun bind(category: Category) {
            binding.categoryName.text = category.name
            
            // Update background based on selection state
            val isSelected = isSelected(category.name)
            binding.root.isSelected = isSelected
            
            // Apply styling (could be done via selector drawable)
            // Selected: filled blue chip
            // Unselected: outline chip
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}
