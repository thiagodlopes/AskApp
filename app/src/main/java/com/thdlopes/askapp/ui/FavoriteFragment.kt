package com.thdlopes.askapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thdlopes.askapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
}