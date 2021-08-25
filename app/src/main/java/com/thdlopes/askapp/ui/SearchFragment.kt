package com.thdlopes.askapp.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
            textViewFilter.text = selectedChipText
            Toast.makeText(requireContext(), "${selectedChipText}", Toast.LENGTH_SHORT).show()
            viewModel.getRealTimeUpdate(selectedChipText)
            adapter.clearAdapter()
        }

        viewModel.question.observe(viewLifecycleOwner, Observer{
            adapter.addQuestion(it)
        })

        viewModel.getRealTimeUpdate("")
    }

    private fun getSelectedText(chipGroup: ChipGroup, id: Int): String {
        val selectedChip = chipGroup.findViewById<Chip>(id)
        return selectedChip?.text?.toString()?:""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}