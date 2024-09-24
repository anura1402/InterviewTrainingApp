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
    private val selectedItemsMap = mutableMapOf<Int, Boolean?>()
    private var selectedItemPositions = mutableListOf<Int>()
    private var resultForOptions = mutableListOf<Boolean>()
    private var numberOfQuestion: Int = 0

    var items: List<String>
        get() = optionsList
        set(value) {
            val callback = OptionListDiffCallback(optionsList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            optionsList = value
        }

    fun setSelectedPositions(positions: Set<Int>, numberOfQuestion: Int) {
        val previousNumberOfQuestion = this.numberOfQuestion
        this.numberOfQuestion = numberOfQuestion
        if (previousNumberOfQuestion != numberOfQuestion
            || positions.contains(-1)
            || selectedItemPositions.contains(-1)
        ) {
            selectedItemPositions.clear()
            Log.d("OptionsAdapter", "IS CLEARED")
        }
        for (position in positions) {
            if (!selectedItemPositions.contains(position)) {
                selectedItemPositions.add(position)
            }
        }
        //selectedItemPositions.addAll(positions)
        //selectedItemPositions.addAll(positions)
        notifyDataSetChanged()
        Log.d(
            "OptionsAdapter",
            "selectedItemPositions: $selectedItemPositions, numberOfQuestion: $numberOfQuestion, previousNumberOfQuestion: $previousNumberOfQuestion positions: $positions"
        )
    }

    fun setResultForOptions(resultForOptions: List<Boolean>) {
        this.resultForOptions.addAll(resultForOptions)
        Log.d("OptionsAdapter2", "2 resultForOptions: $resultForOptions")
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
        val tvText: TextView = view.findViewById(R.id.answerOption)
        private val correctColor = ContextCompat.getColor(itemView.context, R.color.correct_answer)
        private val wrongColor = ContextCompat.getColor(itemView.context, R.color.wrong_answer)
        private var positions: Set<Int> = emptySet()
        fun bind(position: Int) {
            var isCorrect =
                if (answerResults[currentQuestionId] == null) null else answerResults[currentQuestionId] == true
            if (isCorrect != null && !isCorrect) {
                isOptionSelectable = false
            }
            itemView.setOnClickListener {
                if (isOptionSelectable) {
                    isCorrect = answerResults[currentQuestionId] == true
//                    Log.d(
//                        "OptionsAdapter",
//                        "2 answerResults: $answerResults, isCorrect: $isCorrect, selectedItemsMap $selectedItemsMap position $position"
//                    )
                    positions += position
                    setSelectedPositions(positions, numberOfQuestion)

                    //setResultForOptions(listOf(isCorrect!!))

                    //selectedItemsMap[position] = isCorrect
//                    Log.d(
//                        "OptionsAdapter",
//                        "---position: $position selectedItemPositions $selectedItemPositions isCorrect: $isCorrect resultForOptions $resultForOptions"
//                    )
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                    onItemClick(position)
                }
            }
            if (position == selectedItemPosition) {
                if (isCorrect !== null) {
                    setResultForOptions(listOf(isCorrect!!))
                } else {
                    resultForOptions.clear()
                }
                Log.d("OptionsAdapter", "3 resultForOptions: $resultForOptions")
            }
            val index = selectedItemPositions.indexOf(position)
            Log.d("OptionsAdapter", "position: $position, index: $index, selectedItemPositions: $selectedItemPositions, resultForOptions: $resultForOptions")
            if (selectedItemPositions.contains(position) && resultForOptions[selectedItemPositions.indexOf(position)]) {
                itemView.setBackgroundColor(correctColor)
            } else if (selectedItemPositions.contains(position) && !resultForOptions[selectedItemPositions.indexOf(position)]) {
                itemView.setBackgroundColor(wrongColor)
            } else {
                itemView.setBackgroundColor(Color.WHITE)
            }
//            Log.d(
//                "OptionsAdapter",
//                "selectedItemPositions: $selectedItemPositions, position: $position isCorrect: $isCorrect resultForOptions: $resultForOptions"
//            )
//            if (selectedItemPositions.size >= 2 && isCorrect == false) {
//                for (i in 0..selectedItemPositions.size - 2) {
//                    if (position == i) {
//                        itemView.setBackgroundColor(correctColor)
//                    }
//                }
//            }

            //Log.d("OptionsAdapter", "selectedItemPositions: $selectedItemPositions")

            if (isCorrect == null) {
                itemView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    companion object {
        const val MAX_POOL_SIZE = 25
    }
}