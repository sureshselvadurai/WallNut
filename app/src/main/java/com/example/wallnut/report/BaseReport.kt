package com.example.wallnut.report

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.provider.Telephony
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.wallnut.R
import com.example.wallnut.activity.MainPageActivity
import com.example.wallnut.utils.Constants
import com.example.wallnut.model.Message
import com.example.wallnut.model.Report
import com.example.wallnut.model.Template
import com.example.wallnut.model.Transaction
import com.example.wallnut.utils.Utils
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

/**
 * BaseReport class is responsible for generating a report by reading SMS data,
 * processing the information, and creating a report.
 *
 * @param context The context in which the report is generated.
 */
class BaseReport(private val context: Context) {

    private var onSmsPermissionGrantedCallback: (() -> Unit)? = null
    private var messages: MutableList<Message> = mutableListOf()
    private var templates: MutableList<Template> = mutableListOf()
    private var transactions: MutableList<Transaction> = mutableListOf()
    private lateinit var messageInfo: HashMap<String, Map<String, String>>
    private var report: Report = Report("")
    private var fileExists: Boolean = false


    init {
        checkExistingReport()
        generateReport()
    }

    private fun checkExistingReport() {
        if(File(Constants.REPORT_PATH).exists()) {
            val reportString = Utils.readFile(Constants.REPORT_PATH)
            val gson = Gson()
            report = gson.fromJson(reportString, Report::class.java)
        }
    }

    /**
     * Generate a report, requesting SMS permissions if needed.
     */
    private fun generateReport() {
        try {
            if (!isSmsPermissionGranted()) {
                requestSmsPermission {
                    readProcessSMS()
                }
            } else {
                readProcessSMS()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Saving messages failed. Please try again", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Check if SMS permission is granted.
     *
     * @return `true` if SMS permission is granted, `false` otherwise.
     */
    private fun isSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request SMS permission and perform a callback action upon approval.
     *
     * @param callback The action to be executed after SMS permission is granted.
     */
    private fun requestSmsPermission(callback: () -> Unit) {
        onSmsPermissionGrantedCallback = callback
        try {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_SMS),
                PackageManager.PERMISSION_GRANTED
            )
        } catch (e: Exception) {
            (context as Activity).finish()
        }
    }

    /**
     * Read and process SMS data, including writing to a file.
     */
    private fun readProcessSMS() {
        readSMSData()
        readTemplateData()
        transactionListCreator()
        reportCreator()

        val fileOutputStream: FileOutputStream = context.openFileOutput(Constants.REPORT, Context.MODE_PRIVATE)
        fileOutputStream.write(report.toString().toByteArray())
        (context as Activity).startActivity(Intent(context, MainPageActivity::class.java))
    }

    /**
     * Read SMS data from the device.
     */
    private fun readSMSData() {
        val cursor =
            context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)

        cursor?.use {
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
            val messageIdIndex = it.getColumnIndex(Telephony.Sms._ID)
            val typeIndex = it.getColumnIndex(Telephony.Sms.TYPE)

            while (it.moveToNext()) {
                val address = it.getString(addressIndex)
                val body = it.getString(bodyIndex)
                val date = it.getString(dateIndex)
                val messageId = it.getString(messageIdIndex)
                val type = it.getString(typeIndex)

                val instant = Instant.ofEpochMilli(date.toLong())
                val zoneId = ZoneId.of("UTC")
                val dateFormat = instant.atZone(zoneId).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                addMessage(address, body, dateFormat, messageId, type)
            }
        }
        writeToFile()
    }

    /**
     * Write SMS data to a file.
     */
    private fun writeToFile() {
        val gson = Gson()
        var json = gson.toJson(messages)
        json = json.toString()

        try {
            val fileOutputStream: FileOutputStream = context.openFileOutput(Constants.MESSAGES, Context.MODE_PRIVATE)
            fileOutputStream.write(json.toByteArray())
        } catch (e: Exception) {
            Toast.makeText(context, "Saving messages failed. Please try again", Toast.LENGTH_SHORT).show()
        }
        val configState = context.getSharedPreferences("configState", Context.MODE_PRIVATE)
        val editor = configState?.edit()
        editor?.putString("messages", json.toString())
        editor?.apply()
    }

    /**
     * Determine the date flag based on the provided date string.
     *
     * @param dateStr The date string to be analyzed.
     * @return The date flag.
     */

    private fun getDateFlag(dateStr: String): Int {
        val dateFormat = SimpleDateFormat("MMMdd")
        val currentDate = Date()
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = currentDate

        // Get the current month as a number (0-11, where 0 is January and 11 is December)
        val currentMonth = currentCalendar.get(Calendar.MONTH)

        // Create a calendar for the provided date
        val providedDate = dateFormat.parse(dateStr)
        val providedCalendar = Calendar.getInstance()

        if (providedDate != null) {
            providedCalendar.time = providedDate
        }

        // Get the month of the provided date
        val providedMonth = providedCalendar.get(Calendar.MONTH)

        val monthsDiff = currentMonth - providedMonth

        return when {
            monthsDiff == 0 -> 1 // Current month
            monthsDiff == 1 || (currentMonth == 0 && providedMonth == 11) -> 2 // Previous month
            monthsDiff == -1 || (currentMonth == 11 && providedMonth == 0) -> 3 // Next month
            else -> 4 // Everything else
        }
    }


    /**
     * Create the report by processing transaction data.
     */
    private fun reportCreator() {
        // Define variables and initialize them to zero or empty strings
        var netBalance = 0.0
        var totalSpends = 0.0
        var totalIncome = 0.0
        val billReminderList = StringBuilder()
        var foodSpend = 0.0
        var utilitiesSpend = 0.0
        var loanSpend = 0.0
        var entertainmentSpend = 0.0
        var previousMonthSpend = 0.0

        for (i: Transaction in transactions) {
            val dateFlag = getDateFlag(i.getDate())

            when {
                dateFlag == 4 -> continue
                dateFlag == 3 -> {
                    if(i.getTransactionInfoBillType() == Constants.BILL_REMINDER)  {
                        billReminderList.append("${i.getDate()} || ${i.getTransactionInfoSpendType()} || ${i.getAmount()} ||| ")
                    }
                }
                dateFlag == 2 -> {
                    previousMonthSpend += i.getAmount().toFloat()
                    continue
                }
                i.getTransactionInfoBillType() == Constants.DEBIT -> {
                    netBalance -= i.getAmount().toFloat()
                    totalSpends += i.getAmount().toFloat()
                }
                i.getTransactionInfoBillType() == Constants.CREDIT -> {
                    netBalance += i.getAmount().toFloat()
                    totalIncome += i.getAmount().toFloat()
                }
                i.getTransactionInfoBillType() == Constants.BILL_REMINDER -> {
                    billReminderList.append("${i.getDate()} || ${i.getTransactionInfoSpendType()} || ${i.getAmount()} ||| ")
                }
            }

            when (i.getTransactionInfoSpendType()) {
                Constants.ENTERTAINMENT -> entertainmentSpend += i.getAmount().toFloat()
                Constants.FOOD -> foodSpend += i.getAmount().toFloat()
                Constants.UTILITIES -> utilitiesSpend += i.getAmount().toFloat()
                Constants.LOAN -> loanSpend += i.getAmount().toFloat()
            }
        }

        // Set the values in the 'report' object
        report = Report.Builder(report.getBudget())
            .foodSpend(foodSpend.toString())
            .utilitiesSpend(utilitiesSpend.toString())
            .loanSpend(loanSpend.toString())
            .entertainmentSpend(entertainmentSpend.toString())
            .previousMonthSpend(previousMonthSpend.toString())
            .totalSpends(totalSpends.toString())
            .totalIncome(totalIncome.toString())
            .netBalance(netBalance.toString())
            .billReminder(billReminderList.toString()).build()

        fileExists = context.getFileStreamPath(Constants.REPORT).exists()
    }

    /**
     * Read a CSV file and create a template information list.
     */
    private fun readCsvToTemplateInfoList() {
        messageInfo = HashMap()
        try {
            val inputStream = context.resources.openRawResource(R.raw.templateinfo)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            // Read each line of the CSV file
            while (reader.readLine().also { line = it } != null) {
                val templateInfoListEle = HashMap<String, String>()

                val record: List<String>? = line?.split("||")
                val transactionIdPattern = record?.get(0) ?: ""
                val type = record?.get(1) ?: ""
                val spendType = record?.get(2) ?: ""
                val others = record?.get(3) ?: ""

                templateInfoListEle["transactionIdPattern"] = transactionIdPattern
                templateInfoListEle["type"] = type
                templateInfoListEle["spendType"] = spendType
                templateInfoListEle["others"] = others

                messageInfo[transactionIdPattern] = templateInfoListEle
            }

            reader.close()
        } catch (e: Exception) {
            Toast.makeText(context, "Saving messages failed. Please try again", Toast.LENGTH_SHORT).show()
        }
        messageInfo.remove("transactionIdPattern")
    }

    /**
     * Create a list of transactions based on templates and messages.
     */
    private fun transactionListCreator() {
        readCsvToTemplateInfoList()
        for (template: Template in templates) {
            for (message: Message in messages) {
                val transactionID = template.getTransactionIdPattern()
                val transaction = extractFieldsFromMessage(
                    message.getBody(),
                    template.getDebitMessageRegex(),
                    transactionID
                )
                if (transaction.getID() == "Success") {
                    if (!transactions.contains(transaction)) {
                        val transactionInfo = messageInfo[transactionID]
                        if (transactionInfo != null) {
                            transaction.setTransactionInfo(transactionInfo)
                        }
                        transactions.add(transaction)
                    }
                }
            }
        }
    }

    /**
     * Extract fields from an SMS message using a regular expression pattern.
     *
     * @param message The SMS message to extract information from.
     * @param regexPattern The regular expression pattern to match against the message.
     * @param transactionID The transaction ID for this message.
     * @return A Transaction object containing extracted information.
     */
    private fun extractFieldsFromMessage(message: String, regexPattern: String, transactionID: String): Transaction {
        val messageParsed = message.lowercase().replace(" ", "").replace("\n", "")
        val regex = regexPattern.lowercase().replace(" ", "").toRegex()

        val matchResults = regex.findAll(messageParsed)

        val extractedInfo = Transaction("", transactionID, "", "", HashMap())

        for (matchResult in matchResults) {
            val groupValues = matchResult.groups
            extractedInfo.putID("Success")

            if (groupValues["amount"] != null) {
                extractedInfo.setAmount(groupValues["amount"]!!.value)
            }

            if (groupValues["date"] != null) {
                extractedInfo.setDate(groupValues["date"]!!.value)
            }
        }

        return extractedInfo
    }

    /**
     * Read templates data from a CSV file and create Template objects.
     */
    private fun readTemplateData() {
        try {
            val inputStream = context.resources.openRawResource(R.raw.templates)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            // Read each line of the CSV file
            while (reader.readLine().also { line = it } != null) {
                val record: List<String>? = line?.split("||")
                val transactionIdPattern = record?.get(0) ?: ""
                val debitMessageRegex = record?.get(1) ?: ""

                val template = Template(transactionIdPattern, debitMessageRegex)
                templates.add(template)
            }
            reader.close()
        } catch (e: Exception) {
            Toast.makeText(context, "Saving messages failed. Please try again", Toast.LENGTH_SHORT).show()
        }
        templates.removeAt(0)
    }

    /**
     * Add a message to the messages list.
     *
     * @param address The address of the message.
     * @param body The body text of the message.
     * @param date The date of the message.
     * @param messageId The message ID.
     * @param type The type of the message.
     */
    private fun addMessage(address: String, body: String, date: String, messageId: String, type: String) {
        val messageJson = Message(address, body, date, messageId, type)
        if (!messages.contains(messageJson)) {
            messages.add(messageJson)
        }
    }

    fun invokeCallback() {
        onSmsPermissionGrantedCallback?.invoke()
    }
}
