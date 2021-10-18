package com.thdlopes.askapp.ui

import android.util.Log
import android.view.LayoutInflater
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
        var voters = questions[position].voters
        Log.d("voters", voters.toString())
        if (voters.contains(currentUser)) {
            //BackView
            holder.binding.textViewQuestion.text = questions[position].name
            holder.binding.answerA.text = questions[position].aVotes.toString()
            holder.binding.answerB.text = questions[position].bVotes.toString()
            var totalVotes = "Total de votos: ${questions[position].aVotes + questions[position].bVotes}"
            holder.binding.chipCategory.text = totalVotes
        } else {
            //FrontView
            holder.binding.textViewQuestion.text = questions[position].name
            holder.binding.answerA.text = questions[position].answerA
            holder.binding.answerB.text = questions[position].answerB
            holder.binding.chipCategory.text = questions[position].category
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