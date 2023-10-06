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
import com.example.wallnut.helper.MessagesHelper
import com.example.wallnut.helper.PermissionHelper
import com.example.wallnut.model.Report
import com.google.gson.Gson

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: MainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(reportDisplay())

        var messagesHelper =  MessagesHelper()
        var reportString = messagesHelper.readFile("/data/user/0/com.example.wallnut/files/report.json")
        val gson = Gson()
        var report = gson.fromJson(reportString, Report::class.java)

        if(report!=null){
            binding.textView3.text = "$ " + report.netBalence
        }else{
            binding.textView3.text = "$ 0 "
        }

        binding.bottomNavigationView.setOnItemSelectedListener() {
            when(it.itemId){
                R.id.messagesTab -> replaceFragment(messagesDisplay())
                R.id.reportsTab -> replaceFragment(reportDisplay())
                else -> {
                }
            }
            true
        }

        binding.button.setOnClickListener {refreshSMS(this)}
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.DisplayView,fragment)
        fragmentTransaction.commit()
    }

    fun refreshSMS(context: Context){
        SMSSlider().readSMS(context)
    }
}