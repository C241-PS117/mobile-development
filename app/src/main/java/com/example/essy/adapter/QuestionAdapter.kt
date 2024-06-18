package com.example.essy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.essy.data.model.QuestionResult
import com.example.essy.databinding.ListJawabanBinding

class QuestionAdapter(
    private var questionList: List<QuestionResult>,
    private val onItemClickListener: (QuestionResult) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListJawabanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questionList.size

    fun setData(newQuestions: List<QuestionResult>) {
        questionList = newQuestions
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListJawabanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuestionResult) {
            binding.apply {
                txtQuestion.text = question.soal
                txtAnswer.text = question.jawaban
                root.setOnClickListener { onItemClickListener(question) }
            }
        }
    }
}
