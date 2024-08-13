package ru.anura.androidinterviewtrainingapp.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

class OptionListDiffCallback(
    private val oldList: List<String>,
    private val newList: List<String>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Для строк это всегда true, потому что у нас нет идентификаторов.
        // Но если бы это были объекты с уникальными ID, здесь было бы сравнение их ID здесь.
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}