package com.example.wallnut.activity

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.wallnut.R
import com.example.wallnut.databinding.MainPageBinding
import com.example.wallnut.fragment.messagesDisplay
import com.example.wallnut.fragment.reportDisplay
import com.example.wallnut.helper.MessagesHelper
import com.example.wallnut.model.Report
import com.google.gson.Gson
import java.io.FileOutputStream

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
            binding.textView4.text = "Total Spends : $ " + report.totalSpends
            binding.textView5.text = "Total Income : $ " + report.totalIncome
            if(report.budget.trim().equals("")){
                binding.textView6.text = "Budget : Not Set "
            }else{
                binding.textView6.text = "Budget : $ "+ report.budget
                val percentage = ((report.totalSpends.toFloat()/report.budget.toFloat())*100).toInt()
                binding.progressBar.progress = percentage;
                binding.textView7.text = percentage.toString() + " %"
            }
        }else{
            binding.textView3.text = "$ 0 "
            binding.textView4.text = "No spends "
            binding.textView5.text = "No income "
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
        binding.button2.setOnClickListener { showBudgetDialog(report) }
    }

    private fun showBudgetDialog(report: Report) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Your Budget")

        val input = EditText(this)
        input.hint = "Enter your budget amount"
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val budgetAmount = input.text.toString()
            binding.textView6.text = "Budget : "+"$budgetAmount"
            report.budget = budgetAmount;
            binding.progressBar.progress = report.totalSpends.toInt()/report.budget.toInt();
            val fileOutputStream: FileOutputStream = this.openFileOutput("report.json", Context.MODE_PRIVATE)
            fileOutputStream.write(report.toString().toByteArray())
        }


        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
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