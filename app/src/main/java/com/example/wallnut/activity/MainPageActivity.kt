package com.example.wallnut.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.wallnut.R
import com.example.wallnut.databinding.MainPageBinding
import com.example.wallnut.fragment.messagesDisplay
import com.example.wallnut.fragment.reportDisplay
import com.example.wallnut.helper.PermissionHelper

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: MainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(reportDisplay())

        binding.bottomNavigationView.setOnItemSelectedListener() {
            when(it.itemId){
                R.id.messagesTab -> replaceFragment(messagesDisplay())
                R.id.reportsTab -> replaceFragment(reportDisplay())
                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.DisplayView,fragment)
        fragmentTransaction.commit()
    }

    fun refreshSMS(view: View){
        val configState = this.getSharedPreferences( "configState", Context.MODE_PRIVATE)
        val messages = configState?.getString("messages","")
    }
}