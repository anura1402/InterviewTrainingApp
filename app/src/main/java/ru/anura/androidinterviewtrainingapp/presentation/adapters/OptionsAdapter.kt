package ru.anura.androidinterviewtrainingapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.anura.androidinterviewtrainingapp.R

class OptionsAdapter: RecyclerView.Adapter<OptionsAdapter.AnswerOptionViewHolder>() {

    var optionsList = listOf<String>()
        set(value){
            val callback = OptionListDiffCallback(optionsList, value)
            val diffResult= DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
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
        //проверка на правильность ответа
    }

    class AnswerOptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvText = view.findViewById<TextView>(R.id.answerOption)
    }
    companion object {
        const val MAX_POOL_SIZE = 25
    }
}