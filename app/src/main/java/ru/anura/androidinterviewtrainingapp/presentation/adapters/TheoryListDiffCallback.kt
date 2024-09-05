package ru.anura.androidinterviewtrainingapp.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory

class TheoryListDiffCallback(
    private val oldList: List<Theory>,
    private val newList: List<Theory>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}