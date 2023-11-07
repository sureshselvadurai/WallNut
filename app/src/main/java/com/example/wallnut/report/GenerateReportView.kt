package com.example.wallnut.report

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wallnut.R
import com.example.wallnut.activity.MainPageActivity
import com.example.wallnut.adapter.PieChartItemView
import com.example.wallnut.databinding.MainPageBinding
import com.example.wallnut.fragment.MessagesDisplay
import com.example.wallnut.fragment.ReportDisplay
import com.example.wallnut.model.BillReminder
import com.example.wallnut.model.Report
import com.example.wallnut.utils.Constants
import com.example.wallnut.utils.Utils
import com.google.gson.Gson
import java.io.FileOutputStream
import java.lang.Math.round
import java.util.Calendar

/**
 * GenerateReportView class is responsible for managing and displaying the report data on the main page.
 *
 * @param mainPageActivity The activity that hosts the main page.
 */
class GenerateReportView(private val mainPageActivity: MainPageActivity) {

    private var binding: MainPageBinding
    private var report: Report

    init {
        val reportString = Utils.readFile(Constants.REPORT_PATH)
        val gson = Gson()
        report = gson.fromJson(reportString, Report::class.java)
        binding = MainPageBinding.inflate(mainPageActivity.layoutInflater)

        // Initialize the main page content
        replaceFragment(ReportDisplay())
        initializeConfig()
        generateReportData()
        setupBottomView()
        binding.refreshButton.setOnClickListener { BaseReport(mainPageActivity) }
        binding.setBudgetButton.setOnClickListener { showBudgetDialog() }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = mainPageActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.DisplayView, fragment)
        fragmentTransaction.commit()
    }

    private fun setupBottomView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.messagesTab -> replaceFragment(MessagesDisplay())
                R.id.reportsTab -> replaceFragment(ReportDisplay())
                else -> {
                }
            }
            true
        }
    }

    private fun generateReportData() {
        setupBalanceHeader()
        setupBudgetReports()
        setupPieChartReport()
        setMonthlyReport()
        setCurrentSavings()
        setBudgetReport()
        setupSpendReport()
        setReminderMessages()
        setupSummaryHeaders()
    }

    private fun shouldAlertUser(context: Activity, report: Report) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AlertPrefs", Context.MODE_PRIVATE)
        val value =
                ((report.getTotalSpends().toFloat() / report.getBudget().toFloat()) * 100).toInt()

        val currentDate = Calendar.getInstance()
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentYear = currentDate.get(Calendar.YEAR)

        // Check if the user has already been alerted this month
        val alertedMonth = sharedPreferences.getInt("alertedMonth", -1)
        val alertedYear = sharedPreferences.getInt("alertedYear", -1)

        if (value > 100 && (alertedMonth != currentMonth || alertedYear != currentYear)) {
            // Show an alert dialog to the user
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Budget Alert")
            val messageText = TextView(context)
            messageText.text = context.getString(R.string.budget_exceeded)
            messageText.setTextColor(Color.RED)
            alertDialogBuilder.setView(messageText)
            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                // Update the shared preferences to mark the user as alerted for this month
                val editor = sharedPreferences.edit()
                editor.putInt("alertedMonth", currentMonth)
                editor.putInt("alertedYear", currentYear)
                editor.apply()
                dialog.dismiss()
            }
            alertDialogBuilder.create().show()
        }
    }

    private fun setupPieChartReport() {
        // Read your data from a file or any source
        var data = listOf(
            report.getEntertainmentSpend().toFloat(),
            report.getFoodSpend().toFloat(),
            report.getUtilitiesSpend().toFloat(),
            report.getLoanSpend().toFloat()
        )
        if(report.getTotalSpends().toFloat().toInt()==0){
            data= emptyList()
        }
        val colors = listOf(
            Color.parseColor("#FFC0CB"),
            Color.parseColor("#E6E6FA"),
            Color.parseColor("#98FB98"),
            Color.parseColor("#FFDAB9")
        )
        val legends = listOf("Entertainment", "Food", "Utilities", "Loan")

        // Update the chart with the new data
        val pieChartView = mainPageActivity.findViewById<PieChartItemView>(R.id.PieChartViewItem)
        pieChartView.setData(data, colors, legends)
    }

    private fun setupSummaryHeaders() {
        binding.netBalanceView.text = mainPageActivity.getString(R.string.net_balance, report.getNetBalance())
        binding.totalSpendsView.text = mainPageActivity.getString(R.string.total_spends, report.getTotalSpends())
        binding.totalIncomeView.text = mainPageActivity.getString(R.string.total_income, report.getTotalIncome())
    }

    private fun setupSpendReport() {
        binding.loanSpendView.text = report.getLoanSpend()
        binding.utilitiesSpendView.text = report.getUtilitiesSpend()
        binding.foodSpendView.text = report.getFoodSpend()
        binding.entertainmentSpendView.text = report.getEntertainmentSpend()
        binding.totalIncomeReportView.text = report.getTotalIncome()
    }

    private fun setReminderMessages() {
        binding.reminderView.movementMethod = ScrollingMovementMethod()
        val substringList = report.getBillReminder().replace(" ", "").split("|||").toList()
        var output = mutableListOf<BillReminder>()
        for (reminder in substringList) {
            if (reminder.split("||").size == 3) {
                val reminderInfo = reminder.split("||")
                val reminderItem =
                    BillReminder(
                        Utils.camelCaseToSpaceSeparated(reminderInfo[0]),
                        Utils.camelCaseToSpaceSeparated(reminderInfo[1]),
                        reminderInfo[2]
                    )
                output.add(reminderItem)
            }
        }
        output = output.sortedBy { it.getDateAsDate() }.toMutableList()
        val bulletList = output.joinToString("<br/>") {
            "&#8226;" + mainPageActivity.getString(
                R.string.bill_reminder,
                it.getDate(),
                it.getType(),
                it.getAmount()
            )
        }
        binding.reminderView.text = Html.fromHtml(bulletList, Html.FROM_HTML_MODE_COMPACT)
    }

    private fun calculateDailySpendingLimit(totalAmount: Double): Double {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val remainingDays = lastDay - currentDay

        if (remainingDays <= 0 || totalAmount.toInt() == 0) {
            return 0.0 // It's the end of the month, no more days left
        }

        val result = totalAmount / remainingDays
        return round(result * 10.0) / 10.0
    }

    private fun setBudgetReport() {
        val savings = (report.getTotalIncome().toFloat() - report.getTotalSpends().toFloat()).toInt()
        var remainingBudget = 0
        if (report.getBudget() != "") {
            remainingBudget = (report.getBudget().toFloat() - report.getTotalSpends().toFloat()).toInt()
        }

        val safeSpend = calculateDailySpendingLimit(remainingBudget.toDouble())

        binding.savingsView.text = savings.toString()
        if (savings >= 0) {
            binding.savingsView.setTextColor(Color.parseColor("#12C30B"))
        } else {
            binding.savingsView.setTextColor(Color.RED)
        }

        binding.remainingBudgetView.text = remainingBudget.toString()
        if (remainingBudget > 0) {
            binding.remainingBudgetView.setTextColor(Color.parseColor("#12C30B"))
        } else {
            binding.remainingBudgetView.setTextColor(Color.RED)
        }

        binding.safeSpendView.text = safeSpend.toString()
        if (safeSpend > 0) {
            binding.safeSpendView.setTextColor(Color.parseColor("#12C30B"))
        } else {
            binding.safeSpendView.setTextColor(Color.RED)
        }
    }

    private fun setCurrentSavings() {
        val savingsPercent =
            (((report.getTotalIncome().toFloat() - report.getTotalSpends().toFloat()) * 100) / report.getTotalIncome().toFloat()).toInt()
        binding.savingsPercentView.text = savingsPercent.toString()
        if (savingsPercent > 20) {
            binding.savingsPercentView.setTextColor(Color.parseColor("#12C30B"))
        } else if (savingsPercent >= 0) {
            binding.savingsPercentView.setTextColor(Color.YELLOW)
        } else binding.savingsPercentView.setTextColor(Color.RED)
    }

    private fun setMonthlyReport() {
        val spendPercent: Int
        if (report.getPreviousMonthSpend().toFloat().toInt() != 0) {
            spendPercent =
                (((report.getTotalSpends().toFloat() - report.getPreviousMonthSpend().toFloat()) * 100) / report.getPreviousMonthSpend().toFloat()).toInt()
        } else {
            binding.previousMonthReport.text = mainPageActivity.getString(R.string.previous_month_report_same)
            binding.previousMonthReport.setTextColor(Color.BLUE)
            return
        }

        if (spendPercent > 0) {
            binding.previousMonthReport.text = mainPageActivity.getString(R.string.previous_month_report_up, spendPercent.toString())
            binding.previousMonthReport.setTextColor(Color.RED)
        } else if (spendPercent < 0) {
            binding.previousMonthReport.text = mainPageActivity.getString(R.string.previous_month_report_down, spendPercent.toString())
            binding.previousMonthReport.setTextColor(Color.parseColor("#034003"))
        } else {
            binding.previousMonthReport.text = mainPageActivity.getString(R.string.previous_month_report_same)
            binding.previousMonthReport.setTextColor(Color.MAGENTA)
        }
    }

    private fun setupBudgetReports() {
        if (report.getBudget().trim() == "") {
            binding.budgetSetView.text = mainPageActivity.getString(R.string.budget_not_set)
        } else {
            binding.budgetSetView.text = mainPageActivity.getString(R.string.budget_set, report.getBudget())
            setupProgressBar()
            shouldAlertUser(mainPageActivity,report)
        }
    }

    private fun setupProgressBar() {
        val percentage = ((report.getTotalSpends().toFloat() / report.getBudget().toFloat()) * 100).toInt()
        binding.progressBar.progress = percentage
        if (percentage >= 100) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
        } else if (percentage >= 80) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(Color.YELLOW)
        }
        binding.budgetPercentText.text = "$percentage%"
    }

    private fun setupBalanceHeader() {
        binding.netBalanceView.text = mainPageActivity.getString(R.string.net_balance, report.getNetBalance())
        binding.totalSpendsView.text = mainPageActivity.getString(R.string.total_spends, report.getTotalSpends())
        binding.totalIncomeView.text = mainPageActivity.getString(R.string.total_income, report.getTotalIncome())
    }

    private fun initializeConfig() {
        val reportString = Utils.readFile(Constants.REPORT_PATH)
        val gson = Gson()

        binding = MainPageBinding.inflate(mainPageActivity.layoutInflater)
        mainPageActivity.setContentView(binding.root)
        report = gson.fromJson(reportString, Report::class.java)
    }

    private fun showBudgetDialog() {
        val builder = AlertDialog.Builder(mainPageActivity)
        builder.setTitle("Enter Your Budget")

        val input = EditText(mainPageActivity)
        input.hint = "Enter your budget amount"
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val budgetAmount = input.text.toString()
            binding.budgetSetView.text = mainPageActivity.getString(R.string.budget_set, budgetAmount)
            report.setBudget(budgetAmount)
            setupBudgetReports()

            val fileOutputStream: FileOutputStream = mainPageActivity.openFileOutput(Constants.REPORT, Context.MODE_PRIVATE)
            fileOutputStream.write(report.toString().toByteArray())
            GenerateReportView(mainPageActivity)
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}
