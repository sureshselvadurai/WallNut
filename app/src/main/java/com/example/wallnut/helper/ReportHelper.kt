package com.example.wallnut.helper

import android.content.Context
import com.example.wallnut.R
import com.example.wallnut.model.Report
import com.example.wallnut.model.Template
import com.example.wallnut.model.Transaction
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar

class ReportHelper {


    fun getDateFlag(dateStr: String): Int {
        val dateFormat = SimpleDateFormat("MMMdd")
        val currentDate = Date()
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = currentDate

        // Get the current month as a number (0-11, where 0 is January and 11 is December)
        val currentMonth = currentCalendar.get(Calendar.MONTH)

        // Create a calendar for the provided date
        val providedDate = dateFormat.parse(dateStr)
        val providedCalendar = Calendar.getInstance()
        providedCalendar.time = providedDate

        // Get the month of the provided date
        val providedMonth = providedCalendar.get(Calendar.MONTH)

        when {
            currentMonth == providedMonth -> return 1 // Current month
            (currentMonth - providedMonth == 1) || (currentMonth == 0 && providedMonth == 11) -> return 2 // Previous month
            else -> return 3 // Any other case
        }
    }


    fun reportCreator(transactionList: MutableList<Transaction>,context: Context): Report {

        val messagesInfo =  readCsvToTemplateInfoList(context)

        var netBalence = 0.0;
        var totalSpends = 0.0;
        var totalIncome = 0.0;
        var billReminderList = ""
        var foodSpend = 0.0;
        var utilitiesSpend = 0.0;
        var loanSpend = 0.0;
        var EntertainmentSpend = 0.0;
        var previousMonthSpend = 0.0;

        var report = Report("","","","", "")

        var one = getDateFlag("oct2")
        var two = getDateFlag("sep3")
        var three = getDateFlag("jan22")

        for(i:Transaction in transactionList){

            if(getDateFlag(i.date)==3){
                continue;
            }
            if(getDateFlag(i.date)==2){
                previousMonthSpend+=i.amount.toFloat()
                continue
            }

            if(i.transactionInfo?.get("type")=="Debit"){
                netBalence -= i.amount.toFloat();
                totalSpends += i.amount.toFloat();
            }else if (i.transactionInfo?.get("type")=="Credit"){
                netBalence += i.amount.toFloat();
                totalIncome += i.amount.toFloat();
            }else if (i.transactionInfo?.get("type")=="BillReminder"){
                billReminderList += i.date+" || "+i.transactionInfo.get("type")+" || Amount : $" +i.amount+" ||| "
            }

            if(i.transactionInfo?.get("spendType")=="Entertainment"){
                EntertainmentSpend += i.amount.toFloat();
            }
            if(i.transactionInfo?.get("spendType")=="Food"){
                foodSpend += i.amount.toFloat();
            }
            if(i.transactionInfo?.get("spendType")=="Utilities"){
                utilitiesSpend += i.amount.toFloat();
            }
            if(i.transactionInfo?.get("spendType")=="Loan"){
                loanSpend += i.amount.toFloat();
            }
        }
        report.netBalence = netBalence.toString()
        report.totalIncome = totalIncome.toString()
        report.totalSpends = totalSpends.toString()
        report.billReminder =  billReminderList
        report.foodSpend= foodSpend.toString()
        report.loanSpend= loanSpend.toString()
        report.utilitiesSpend= utilitiesSpend.toString()
        report.EntertainmentSpend= EntertainmentSpend.toString()
        report.previousMonthSpend = previousMonthSpend.toString()

        return  report
    }

    fun readCsvToTemplateInfoList(context: Context): Map<String,Map<String,String>> {
        val out = HashMap<String,Map<String, String>>()
        try {

            val inputStream = context.resources.openRawResource(R.raw.templateinfo)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            // Read each line of the CSV file
            while (reader.readLine().also { line = it } != null) {
                val templateInfoListEle = HashMap<String,String>()

                var record: List<String>? = line?.split("||")
                val transactionIdPattern = record?.get(0) ?: ""
                val type = record?.get(1) ?: ""
                val spendType = record?.get(2) ?: ""
                val others = record?.get(3) ?: ""
                templateInfoListEle.put("transactionIdPattern",transactionIdPattern)
                templateInfoListEle.put("type",type)
                templateInfoListEle.put("spendType",spendType)
                templateInfoListEle.put("others",others)
                out.put(transactionIdPattern,templateInfoListEle)
            }

            reader.close()
        } catch (e: Exception) {
            println("Error reading template")
            return out
        }
        out.remove("transactionIdPattern")

        return out
    }


}

