package com.example.wallnut.helper

import android.content.Context
import com.example.wallnut.R
import com.example.wallnut.model.Messages
import com.example.wallnut.model.Template
import com.example.wallnut.model.Transaction
import java.io.BufferedReader
import java.io.InputStreamReader

class TransactionHelper {

    fun extractFieldsFromMessage(message: String, regexPattern: String, transactionID: String): Transaction {
        val transactionList = mutableListOf<Template>()

        var message = message.replace(" ","")
        var regexPattern  = regexPattern.replace(" ","")
        val regex  = regexPattern.toRegex()

        val matchResults = regex.findAll(message)
        val groupValues :List<Transaction> = mutableListOf()

        val extractedInfo = Transaction("", transactionID, "", "")

        for (matchResult in matchResults) {
            val groupValues = matchResult.groups
            extractedInfo.putID("Success")
            when {
                groupValues["date"] != null -> extractedInfo.date = groupValues["date"]!!.value
                groupValues["amount"] != null -> extractedInfo.amount =groupValues["amount"]!!.value
            }
        }

        return extractedInfo;

    }


    fun readCsvToMessagesList(messages:Messages,
                              filePath: String,context:Context): List<Template> {
        val messagesList = mutableListOf<Template>()
        try {


            val inputStream = context.resources.openRawResource(R.raw.templates)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?

            // Read each line of the CSV file
            while (reader.readLine().also { line = it } != null) {

                var record: List<String>? = line?.split("||")

                val transactionIdPattern = record?.get(0) ?: ""
                val amountPattern = record?.get(1) ?: ""
                val dateTimePattern = record?.get(2) ?: ""
                val merchantNamePattern = record?.get(3) ?: ""
                val debitMessageRegex = record?.get(4) ?: ""

                val template = Template(transactionIdPattern,amountPattern,dateTimePattern,merchantNamePattern,debitMessageRegex)
                messagesList.add(template)


            }

            reader.close()
        } catch (e: Exception) {
            println("Error reading template")
            return messagesList
        }
        messagesList.removeAt(0)

        return messagesList
    }

}