package com.thdlopes.askapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyQuestionViewModel: ViewModel() {

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

    fun getRealTimeUpdate(){
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser?.uid.toString()

        var query : Query = FirebaseDatabase.getInstance().getReference("questions").orderByChild("creatorId").equalTo( firebaseUser)
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

    override fun onCleared() {
        super.onCleared()
        dbquestions.removeEventListener(childEventListener)
    }

}