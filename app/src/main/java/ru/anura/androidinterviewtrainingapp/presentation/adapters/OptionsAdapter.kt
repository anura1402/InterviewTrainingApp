package ru.anura.androidinterviewtrainingapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.domain.entity.Test

class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.AnswerOptionViewHolder>() {
    var correctAnswer: String? = null
    var answeredQuestions: MutableList<Boolean> = mutableListOf()
    var currentAnswerId: Int = 0

    var optionsList = listOf<String>()
        set(value) {
            val oldSize = optionsList.size
            val newSize = value.size
            Log.d("OptionsAdapter", "Updating optionsList from size $oldSize to $newSize")
            val callback = OptionListDiffCallback(optionsList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }
    var onOptionItemClickListener: ((String) -> Unit)? = null


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

        //проверка на правильность ответа
        holder.tvText.setOnClickListener {
            onOptionItemClickListener?.invoke(optionItem)
            if (!answeredQuestions[currentAnswerId]) {
                if (holder.tvText.text == correctAnswer) {
                    holder.view.setBackgroundResource(R.color.correct_answer)
                } else {
                    holder.view.setBackgroundResource(R.color.wrong_answer)
                }
            }
            answeredQuestions[currentAnswerId] = true
        }
    }

    class AnswerOptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvText = view.findViewById<TextView>(R.id.answerOption)
    }

    companion object {
        const val MAX_POOL_SIZE = 25
    }
}