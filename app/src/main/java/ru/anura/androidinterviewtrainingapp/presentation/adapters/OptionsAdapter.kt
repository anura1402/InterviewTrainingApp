package ru.anura.androidinterviewtrainingapp.presentation.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.anura.androidinterviewtrainingapp.R


class OptionsAdapter() : RecyclerView.Adapter<OptionsAdapter.AnswerOptionViewHolder>() {
    var answerResults: Map<Int, Boolean> = emptyMap()
    var currentQuestionId: Int = 0

    private var optionsList: List<String> = listOf()
    private var onItemClick: (Int) -> Unit = {}
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    private var isOptionSelectable = true

    var items: List<String>
        get() = optionsList
        set(value) {
            val callback = OptionListDiffCallback(optionsList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            optionsList = value
        }

    fun setSelectedPosition(position: Int) {
        val previousItemPosition = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previousItemPosition) // Сбрасываем выделение с предыдущего
        notifyItemChanged(selectedItemPosition) // Применяем выделение к новому
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClick = listener
    }

    fun setIsOptionSelectable(isSelectable: Boolean) {
        isOptionSelectable = isSelectable
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerOptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.option_item, parent, false)
        return AnswerOptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    override fun onBindViewHolder(holder: AnswerOptionViewHolder, position: Int) {
        val optionItem = optionsList[position]
        holder.tvText.text = optionItem
        holder.bind(position)
    }

    inner class AnswerOptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvText = view.findViewById<TextView>(R.id.answerOption)
        fun bind(position: Int) {
            val isCorrect = answerResults[currentQuestionId] == true
            itemView.setOnClickListener {
                if (isOptionSelectable) {
                    // Уведомляем об изменении выделенного элемента
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                    onItemClick(position)
                    isOptionSelectable = false
                }
            }
            val correctColor = ContextCompat.getColor(itemView.context, R.color.correct_answer)
            val wrongColor = ContextCompat.getColor(itemView.context, R.color.wrong_answer)
            itemView.setBackgroundColor(
                if (position == selectedItemPosition && isCorrect) correctColor
                else if (position == selectedItemPosition && !isCorrect) wrongColor
                else Color.WHITE // цвет по умолчанию
            )

        }
    }

    companion object {
        const val MAX_POOL_SIZE = 25
    }
}