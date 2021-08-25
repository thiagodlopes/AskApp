package com.thdlopes.askapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.thdlopes.askapp.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerViewCategories

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.buttonLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(getActivity(), LoginActivity::class.java))
        }

//        viewModel.category.observe(viewLifecycleOwner, Observer{
//            adapter.addCategory(it)
//        })
//
//        viewModel.getRealTimeUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}