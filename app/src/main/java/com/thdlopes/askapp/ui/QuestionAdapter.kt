package com.thdlopes.askapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thdlopes.askapp.data.Question
import com.thdlopes.askapp.databinding.RecyclerViewQuestionsBinding

class QuestionAdapter: RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    var questions = mutableListOf<Question>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewQuestion.text = questions[position].name
        holder.binding.answerA.text = questions[position].answerA
        holder.binding.answerB.text = questions[position].answerB
        holder.binding.chipCategory.text = questions[position].category
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun addQuestion(question: Question){
        if(!questions.contains(question)){
            questions.add(question)
        }else{
            val index = questions.indexOf(question)
            if(question.isDeleted){
                questions.removeAt(index)
            }else {
                questions[index] = question
            }
        }
        notifyDataSetChanged()
    }

    fun clearAdapter(){
        questions.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder (val binding: RecyclerViewQuestionsBinding): RecyclerView.ViewHolder(binding.root){
    }

}