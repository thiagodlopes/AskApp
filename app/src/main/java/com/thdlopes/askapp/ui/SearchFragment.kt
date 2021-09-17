package com.thdlopes.askapp.ui

import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
        var textViewFilter = binding.textViewFilter


        //Colocar no FilterDialog
        val res: Resources = resources
        categories = res.getStringArray(R.array.categories)
        for (i in 1 until categories.size) { //Começa no 1 pra ignorar o "Selecione"
            var chip = Chip(binding.chipFilter.context)
            var chipText = categories[i]
            chip.text = chipText
            chip.isClickable = true
            chip.isCheckable = true
            binding.chipFilter.addView(chip)
        }
        //

        binding.chipFilter.setOnCheckedChangeListener { chipGroup, id ->
            var selectedChipText = getSelectedText(chipGroup, id)
            if (selectedChipText != ""){
                textViewFilter.text = selectedChipText
            } else textViewFilter.text = "Todos"

            Toast.makeText(requireContext(), "${selectedChipText}", Toast.LENGTH_SHORT).show()
            viewModel.getRealTimeUpdate(selectedChipText)
            adapter.clearAdapter()
        }

        binding.filterDrop.setOnClickListener {
            val chipGroup = binding.chipFilter
            if (chipGroup.visibility == View.VISIBLE){
                chipGroup.visibility = View.GONE
                binding.filterDrop.setImageResource(R.drawable.ic_drop_down)
            } else {
                chipGroup.visibility = View.VISIBLE
                binding.filterDrop.setImageResource(R.drawable.ic_drop_up)
            }
        }

        viewModel.question.observe(viewLifecycleOwner, Observer{
            adapter.addQuestion(it)
        })

        viewModel.getRealTimeUpdate("")

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewQuestions)
    }

    private fun getSelectedText(chipGroup: ChipGroup, id: Int): String {
        val selectedChip = chipGroup.findViewById<Chip>(id)
        return selectedChip?.text?.toString()?:""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
            var currentFinance = adapter.questions[position]

            when(direction){
                ItemTouchHelper.RIGHT -> {
                    viewModel.updateVote(currentFinance, "anwserB")
                }

                ItemTouchHelper.LEFT -> {
                    viewModel.updateVote(currentFinance, "anwserA")
                }
            }
            binding.recyclerViewQuestions.adapter?.notifyDataSetChanged()
        }

    }

}