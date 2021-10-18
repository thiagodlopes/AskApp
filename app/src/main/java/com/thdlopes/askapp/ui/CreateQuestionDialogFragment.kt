package com.thdlopes.askapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.thdlopes.askapp.R
import com.thdlopes.askapp.data.MyQuestionViewModel
import com.thdlopes.askapp.data.Question
import com.thdlopes.askapp.databinding.FragmentCreateQuestionDialogBinding


class CreateQuestionDialogFragment : DialogFragment() {

    private var _binding: FragmentCreateQuestionDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelMy: MyQuestionViewModel

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateQuestionDialogBinding.inflate(inflater, container, false)

        viewModelMy = ViewModelProviders.of(this).get(MyQuestionViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser?.uid.toString()

        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.spinnerCategory
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selectedItem = parent!!.getItemAtPosition(position)
                if (selectedItem != "Selecione") {
                    binding.textViewSelectedCategory.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.textViewSelectedCategory.text = "${selectedItem}"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        viewModelMy.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                "Pergunta adicionada com sucesso"
            } else {
                "{it.message}"
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        binding.buttonCreateQuestion.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val answerA = binding.editTextAnswerA.text.toString().trim()
            val answerB = binding.editTextAnswerB.text.toString().trim()
            val creatorId = firebaseUser
            val category = binding.textViewSelectedCategory.text.toString().trim()

            if(name.isEmpty()){
                binding.editTextName.error = "Este campo é obrigatório"
                return@setOnClickListener
            }
            if(name.length > 100){
                binding.editTextName.error = "Limite de 100 caracteres"
                return@setOnClickListener
            }
            if(answerA.isEmpty()){
                binding.editTextAnswerA.error = "Este campo é obrigatório"
                return@setOnClickListener
            }
            if(answerA.length > 100){
                binding.editTextAnswerA.error = "Limite de 100 caracteres"
                return@setOnClickListener
            }
            if(answerB.isEmpty()){
                binding.editTextAnswerB.error = "Este campo é obrigatório"
                return@setOnClickListener
            }
            if(answerB.length > 100){
                binding.editTextAnswerB.error = "Limite de 100 caracteres"
                return@setOnClickListener
            }

            val question = Question()
            question.name = name
            question.answerA = answerA
            question.answerB = answerB
            question.creatorId = creatorId
            question.category = category
            question.voters.add(creatorId)

            viewModelMy.addQuestion(question)
        }

    }
}