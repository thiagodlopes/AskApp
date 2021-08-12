package com.thdlopes.askapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.thdlopes.askapp.R
import com.thdlopes.askapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val createFragment = CreateFragment()
    private val favoriteFragment = FavoriteFragment()
    private val accountFragment = AccountFragment()

    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        changeFragment(homeFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> changeFragment(homeFragment)
                R.id.search -> changeFragment(searchFragment)
                R.id.create -> changeFragment(createFragment)
                R.id.favorite -> changeFragment(favoriteFragment)
                R.id.account -> changeFragment(accountFragment)
            }
            true
        }

    }

    private fun changeFragment(frag: Fragment){
        if (frag != null) {
            val fragment = supportFragmentManager.beginTransaction()
            fragment.replace(R.id.fragment_container,frag).commit()
        }
    }

}