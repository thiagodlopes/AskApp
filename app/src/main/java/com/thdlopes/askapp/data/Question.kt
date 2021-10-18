package com.thdlopes.askapp.data

import com.google.firebase.database.Exclude

data class Question(
    @get:Exclude
    var id: String? = null,
    var creatorId: String? = null,
    var name: String? = null,
    var answerA: String? = null,
    var answerB: String? = null,
    var category: String? = null,
    var aVotes: Long = 0,
    var bVotes: Long = 0,
    var voters: ArrayList<String> = arrayListOf(),

    @get:Exclude
    var isDeleted: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return if(other is Question){
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (creatorId?.hashCode() ?: 0)
        result = 31 * result + (answerA?.hashCode() ?: 0)
        result = 31 * result + (answerB?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + aVotes.hashCode()
        result = 31 * result + bVotes.hashCode()
        result = 31 * result + isDeleted.hashCode()
        return result
    }

}