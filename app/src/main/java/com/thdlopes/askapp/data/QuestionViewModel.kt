package com.thdlopes.askapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class QuestionViewModel: ViewModel() {

    private lateinit var firebaseAuth: FirebaseAuth

    private val dbquestions = FirebaseDatabase.getInstance().getReference(NODE_QUESTIONS)
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question> get() = _question

    fun addQuestion(question: Question){
        question.id = dbquestions.push().key

        dbquestions.child(question.id!!).setValue(question).addOnCompleteListener{
            if (it.isSuccessful){
                _result.value = null
            }else{
                _result.value = it.exception
            }
        }
    }

    private val childEventListener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val question = snapshot.getValue(Question::class.java)
            question?.id = snapshot.key
            _question.value = question!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val question = snapshot.getValue(Question::class.java)
            question?.id = snapshot.key
            _question.value = question!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val question = snapshot.getValue(Question::class.java)
            question?.id = snapshot.key
            question?.isDeleted = true
            _question.value = question!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }

    fun getRealTimeUpdate(filter : String){
        firebaseAuth = FirebaseAuth.getInstance()
        var query : Query = FirebaseDatabase.getInstance().getReference("questions").orderByChild("category")
        if (filter != ""){
            if (filter == "Todos"){
                query = FirebaseDatabase.getInstance().getReference("questions").orderByChild("category")
            } else {
                Log.v("TAG", "== $filter")
                query = FirebaseDatabase.getInstance().getReference("questions").orderByChild("category").equalTo(filter)
            }

        }
        query.addChildEventListener(childEventListener)
    }
    fun updateQuestion(question: Question){
        dbquestions.child(question.id!!).setValue(question)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        _result.value = null
                    }else{
                        _result.value = it.exception
                    }
                }
    }

    fun updateVote(question: Question, answer: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser?.uid.toString()
        val voters = question.voters
        val answers = question.answers
        val category = question.category
        voters.add(firebaseUser)
        answers.add(answer)
        val currentQuestion = dbquestions.child(question.id!!)
        if (answer == "answerA") {
            val aCurrentVotes = question.aVotes + 1
            currentQuestion.child("aVotes").setValue(aCurrentVotes)
        } else if (answer == "answerB") {
            val bCurrentVotes = question.bVotes + 1
            currentQuestion.child("bVotes").setValue(bCurrentVotes)
        } else {
            Log.d("ERROVOTEUPDATE","deu ruim")
        }
        currentQuestion.child("voters").setValue(voters)
        currentQuestion.child("answers").setValue(answers)
        getRealTimeUpdate(category!!)
    }

    fun deleteQuestion(question: Question){
        dbquestions.child(question.id!!).setValue(null)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        _result.value = null
                    }else{
                        _result.value = it.exception
                    }
                }
    }

}