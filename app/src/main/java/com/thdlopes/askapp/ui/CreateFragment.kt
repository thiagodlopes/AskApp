package com.thdlopes.askapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.thdlopes.askapp.data.MyQuestionViewModel
import com.thdlopes.askapp.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private val adapter = MyQuestionAdapter()

    private lateinit var viewModelMy: MyQuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        viewModelMy = ViewModelProviders.of(this).get(MyQuestionViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewMyQuestions.adapter = adapter

        binding.addButton.setOnClickListener{
            CreateQuestionDialogFragment().show(childFragmentManager, "")
        }

        viewModelMy.question.observe(viewLifecycleOwner, Observer{
            adapter.addQuestion(it)
        })

        viewModelMy.getRealTimeUpdate()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}