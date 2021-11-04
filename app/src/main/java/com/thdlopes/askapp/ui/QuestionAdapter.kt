package com.thdlopes.askapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.thdlopes.askapp.data.*
import com.thdlopes.askapp.databinding.RecyclerViewQuestionsBinding


class QuestionAdapter: RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    var questions = mutableListOf<Question>()
    var currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageViewA.visibility = View.INVISIBLE
        holder.binding.imageViewB.visibility = View.INVISIBLE
        holder.binding.ownerIcon.visibility = View.INVISIBLE
        var voters = questions[position].voters
        holder.binding.textViewQuestion.text = questions[position].name
        holder.binding.answerA.text = questions[position].answerA
        holder.binding.answerB.text = questions[position].answerB
        holder.binding.chipCategory.text = questions[position].category
        if (voters.contains(currentUser)) {
            //Voted
            var answers = questions[position].answers
            var answerIndex = voters.indexOf(currentUser)
            if (answers.size >= answerIndex){
                if (answers[answerIndex] == "answerA") {
                    holder.binding.imageViewA.visibility = View.VISIBLE
                } else if (answers[answerIndex] == "answerB"){
                    holder.binding.imageViewB.visibility = View.VISIBLE
                } else {
                    holder.binding.ownerIcon.visibility = View.VISIBLE
                }
            }

            holder.binding.aPercentage.visibility = View.VISIBLE
            holder.binding.bPercentage.visibility = View.VISIBLE
            var aVotes = questions[position].aVotes
            var bVotes = questions[position].bVotes
            var votesSum = questions[position].aVotes + questions[position].bVotes
            var aPercentage: String
            var bPercentage: String
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
            var totalVotes = "Votos: $votesSum"
            holder.binding.chipCategory.text = totalVotes
        } else {
            //Unvoted
            holder.binding.aPercentage.height = 0
            holder.binding.bPercentage.height = 0

        }
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