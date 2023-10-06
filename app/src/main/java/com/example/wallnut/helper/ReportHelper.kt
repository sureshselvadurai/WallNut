package com.example.wallnut.helper

import android.content.Context
import com.example.wallnut.R
import com.example.wallnut.model.Report
import com.example.wallnut.model.Template
import com.example.wallnut.model.Transaction
import java.io.BufferedReader
import java.io.InputStreamReader

class ReportHelper {


    fun reportCreator(transactionList: MutableList<Transaction>,context: Context): Report {

        val messagesInfo =  readCsvToTemplateInfoList(context)


        var netBalence = 0.0;
        var report = Report("","","")

        for(i:Transaction in transactionList){
            netBalence += i.amount.toFloat();
        }
        report.netBalence = netBalence.toString()
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

