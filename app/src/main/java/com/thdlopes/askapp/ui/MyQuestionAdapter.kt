package com.thdlopes.askapp.ui

import android.view.LayoutInflater
import android.view.View
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
        holder.binding.answerA.text = questions[position].answerA
        holder.binding.answerB.text = questions[position].answerB
        holder.binding.textViewCategory.text = questions[position].category
        holder.binding.aPercentage.visibility = View.VISIBLE
        holder.binding.bPercentage.visibility = View.VISIBLE
        var aVotes = questions[position].aVotes
        var bVotes = questions[position].bVotes
        var votesSum = questions[position].aVotes + questions[position].bVotes
        val aPercentage: String
        val bPercentage: String
        if (votesSum == 0.toLong()) {
            aPercentage = "0%"
            bPercentage = "0%"
        } else {
            aVotes = (aVotes * 100)/votesSum
            bVotes = (bVotes * 100)/votesSum
            aPercentage = "$aVotes%"
            bPercentage = "$bVotes%"
        }

        holder.binding.aPercentage.text = aPercentage
        holder.binding.bPercentage.text = bPercentage
        val totalVotes = "Votos: $votesSum"
        holder.binding.textViewVotes.text = totalVotes
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