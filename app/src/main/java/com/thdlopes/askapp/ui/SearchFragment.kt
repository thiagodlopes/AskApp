package com.thdlopes.askapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.thdlopes.askapp.R
import com.thdlopes.askapp.data.QuestionViewModel
import com.thdlopes.askapp.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var categories: Array<String> //Usado pra criar o chipGroup
    private val adapter = QuestionAdapter()
    private lateinit var viewModel: QuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this).get(QuestionViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewQuestions.adapter = adapter
        var selectedCategory = "Todos"

        val spinnerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = binding.spinnerCategory
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinner.selectedItem.toString()
                if (selectedItem != "") {
                    selectedCategory = selectedItem
                } else selectedCategory = "Todos"
                viewModel.getRealTimeUpdate(selectedCategory)
                adapter.clearAdapter()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.filterDrop.setOnClickListener {
            val filterLayout = binding.filterLayout
            if (filterLayout.visibility == View.VISIBLE){
                filterLayout.visibility = View.GONE
            } else {
                filterLayout.visibility = View.VISIBLE
            }
            Toast.makeText(requireContext(), "Exibindo '$selectedCategory'", Toast.LENGTH_SHORT).show()
        }

        viewModel.question.observe(viewLifecycleOwner, Observer {
            adapter.addQuestion(it)
        })

        viewModel.getRealTimeUpdate("")

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewQuestions)
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val currentQuestion = adapter.questions[position]
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val voters = currentQuestion.voters
            val toast = Toast.makeText(requireContext(), "Você já voltou.", Toast.LENGTH_SHORT)
            when(direction){
                ItemTouchHelper.RIGHT -> {
                    if (voters.contains(currentUser)) {
                        toast.show()
                    } else {
                        viewModel.updateVote(currentQuestion, "answerB")
                    }
                }
                ItemTouchHelper.LEFT -> {
                    if (voters.contains(currentUser)) {
                        toast.show()
                    } else {
                        viewModel.updateVote(currentQuestion, "answerA")
                    }
                }
            }
            binding.recyclerViewQuestions.adapter?.notifyDataSetChanged()
        }

    }
}