package com.thdlopes.askapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thdlopes.askapp.data.Question
import com.thdlopes.askapp.databinding.RecyclerViewMyQuestionsBinding

class MyQuestionAdapter: RecyclerView.Adapter<MyQuestionAdapter.ViewHolderMy>() {

    var questions = mutableListOf<Question>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMy {
        return ViewHolderMy(RecyclerViewMyQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderMy, position: Int) {
        holder.binding.textViewQuestion.text = questions[position].name
        holder.binding.textViewAnswerA.text = questions[position].answerA
        holder.binding.textViewAnswerB.text = questions[position].answerB
        holder.binding.textViewVotes.text = questions[position].votes.toString()
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

    inner class ViewHolderMy (val binding: RecyclerViewMyQuestionsBinding): RecyclerView.ViewHolder(binding.root){
    }

}