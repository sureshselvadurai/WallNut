package com.example.wallnut.activity

import android.app.AlertDialog
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Reminders
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.wallnut.R
import java.util.Calendar
import com.example.wallnut.databinding.MainPageBinding
import com.example.wallnut.fragment.messagesDisplay
import com.example.wallnut.fragment.reportDisplay
import com.example.wallnut.helper.MessagesHelper
import com.example.wallnut.model.BillReminder
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
                if(percentage>=100){
                    binding.progressBar.progressTintList =  ColorStateList.valueOf(Color.RED)
                }else if(percentage>=80){
                    binding.progressBar.progressTintList =  ColorStateList.valueOf(Color.YELLOW)
                }
                binding.textView7.text = percentage.toString() + " %"
            }
            setMonthlyReport(binding,report)
            setCurrentSavings(binding,report)
            setBudgetReport(binding,report)
            binding.textView8.text = report.loanSpend + " $"
            binding.textView10.text = report.utilitiesSpend+ " $"
            binding.textView12.text = report.foodSpend+ " $"
            binding.textView14.text = report.EntertainmentSpend+ " $"
            binding.textView16.text = report.totalIncome+ " $"
            setReminderMessages(binding,report)
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
        binding.textView28.movementMethod = ScrollingMovementMethod()
    }

    private fun setBudgetReport(binding: MainPageBinding, report: Report) {
        val savings = (report.totalIncome.toFloat()-report.totalSpends.toFloat()).toInt()
        var budget=0
        if(report.budget!=""){
            budget = (report.budget.toFloat()-report.totalSpends.toFloat()).toInt()
        }

        val safeSpend = calculateDailySpendingLimit(budget.toDouble())

        if(savings>=0){
            binding.textView21.text = savings.toString() + " $"
            binding.textView21.setTextColor(Color.GREEN)
        }else{
            binding.textView21.text = (savings*-1).toString() + " $"
            binding.textView21.setTextColor(Color.RED)
        }


        if(budget>0){
            binding.textView22.text = budget.toString() + " $"
            binding.textView22.setTextColor(Color.GREEN)
        }else{
            binding.textView22.text = "0" + " $"
            binding.textView22.setTextColor(Color.RED)
        }

        if(safeSpend>0){
            binding.textView23.text = safeSpend.toString() + " $"
            binding.textView23.setTextColor(Color.GREEN)
        }else{
            binding.textView23.text = "0" + " $"
            binding.textView23.setTextColor(Color.RED)
        }


    }

    fun calculateDailySpendingLimit(totalAmount: Double): Double {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val remainingDays = lastDay - currentDay

        if (remainingDays <= 0) {
            return 0.0 // It's the end of the month, no more days left
        }

        val dailyLimit = totalAmount / remainingDays
        return dailyLimit
    }

    private fun setCurrentSavings(binding: MainPageBinding, report: Report) {
        val savingsPercent = (((report.totalIncome.toFloat()-report.totalSpends.toFloat())*100)/report.totalIncome.toFloat()).toInt()
        binding.textView25.text = savingsPercent.toString();
        if(savingsPercent>20){
            binding.textView25.setTextColor(Color.GREEN)
        }else if(savingsPercent<=20){
            binding.textView25.setTextColor(Color.YELLOW)
        }else{
            binding.textView25.setTextColor(Color.RED)
        }

    }

    private fun setMonthlyReport(binding: MainPageBinding, report: Report) {
        val spendPercent:Int
        if(report.previousMonthSpend.toFloat().toInt()!=0) {
            spendPercent =
                (((report.totalSpends.toFloat() - report.previousMonthSpend.toFloat()) * 100) / report.previousMonthSpend.toFloat()).toInt()
        }else{
            binding.textView26.text = "Previous Month Data Not available"
            binding.textView26.setTextColor(Color.BLUE)
            return
        }

        if(spendPercent>0){
            binding.textView26.text = "Spends are up by "+spendPercent+" percent. Please review"
            binding.textView26.setTextColor(Color.RED)
        }
        if(spendPercent<0){
            binding.textView26.text = "Spends are down by "+spendPercent+" percent. Great Job !!"
            binding.textView26.setTextColor(Color.GREEN)
        }
        if(spendPercent.toInt() ==0){
            binding.textView26.text = "Spends are the same as previous month. Keep Hustling"
            binding.textView26.setTextColor(Color.MAGENTA)
        }

    }

    private fun setReminderMessages(binding: MainPageBinding, report: Report) {
        val substringList = report.billReminder.replace(" ","").split("|||").toList()
        val output = mutableListOf<BillReminder>()
        for(reminder in substringList){
            if(reminder.split("||").size==3) {
                var reminderInfo = reminder.split("||")
                var reminder =
                    BillReminder(reminderInfo.get(0), reminderInfo.get(1), reminderInfo.get(2))
                output.add(reminder)
            }
        }

        val personListString = output.joinToString("\n") {
            "Due Date: ${it.date}, Due Amount: ${it.amount} , Info : ${it.type}"
        }
        binding.textView28.setText(personListString)
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
            val budgetPercent = (report.totalSpends.toFloat()*100/report.budget.toFloat()).toInt();
            binding.progressBar.progress = budgetPercent
            if(budgetPercent>=100){
                binding.progressBar.progressTintList =  ColorStateList.valueOf(Color.RED)
            }else if(budgetPercent>=80){
                binding.progressBar.progressTintList =  ColorStateList.valueOf(Color.YELLOW)
            }
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