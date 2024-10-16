package ru.anura.androidinterviewtrainingapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory

class TheoryAdapter: RecyclerView.Adapter<TheoryAdapter.TheoryViewHolder>() {
    var theoryList= listOf<Theory>()
        set(value) {
            val callback = TheoryListDiffCallback(theoryList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }
    var onTheoryItemClickListener: ((Theory,Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choosing_theme_item, parent, false)
        return TheoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TheoryViewHolder, position: Int) {
        val theory = this.theoryList[position]
        holder.tvThemeOption.text = theory.name
        holder.view.setOnClickListener {
            onTheoryItemClickListener?.invoke(theory,position)
        }
    }

    override fun getItemCount(): Int {
        return this.theoryList.size
    }

    inner class TheoryViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val tvThemeOption: TextView = view.findViewById<TextView>(R.id.themeOption)
    }
}