package com.example.wallnut.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.wallnut.R
import com.example.wallnut.databinding.MainPageBinding
import com.example.wallnut.helper.MessagesHelper
import com.example.wallnut.helper.PermissionHelper
import com.example.wallnut.helper.ReportHelper
import com.example.wallnut.helper.TransactionHelper
import com.example.wallnut.model.Message
import com.example.wallnut.model.Messages
import com.example.wallnut.model.Report
import com.example.wallnut.model.Template
import com.example.wallnut.model.Transaction
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class SMSSlider : AppCompatActivity() {

    private lateinit var binding: MainPageBinding

    private var onSmsPermissionGrantedCallback: (() -> Unit)? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (grantResults[0]) {
            0 -> {
                onSmsPermissionGrantedCallback?.invoke()
            }
            -1 -> {
                Toast.makeText(this, "SMS permission denied. App will exit.", Toast.LENGTH_SHORT).show()
                finish() // Exit the frame
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_permission)
        binding = MainPageBinding.inflate(layoutInflater)
//        binding.button.setOnClickListener { readSMS(this) }
    }



    private fun readSMSData(context:Context): Messages {
        val messageManager = Messages
        val cursor =
            context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)


        cursor?.use {
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
            val messageId = it.getColumnIndex(Telephony.Sms._ID)
            val type = it.getColumnIndex(Telephony.Sms.TYPE)

            while (it.moveToNext()) {
                val address = it.getString(addressIndex)
                val body = it.getString(bodyIndex)
                val date = it.getString(dateIndex)
                val messageId = it.getString(messageId)
                val type = it.getString(type)

                val instant = Instant.ofEpochMilli(date.toLong())
                val zoneId = ZoneId.of("UTC")
                val dateFormat = instant.atZone(zoneId).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                messageManager.addMessage(address, body, dateFormat,messageId,type)
            }
        }
        MessagesHelper.writeToFile(messageManager,context)
        return messageManager

    }

    private fun requestSmsPermission(callback: () -> Unit) {
        onSmsPermissionGrantedCallback = callback
        try {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                PackageManager.PERMISSION_GRANTED
            )
        }catch (e:Exception){
        }
    }

    fun createTemplateList(messages:Messages,context: Context): List<Template> {
        val templatePath = "app/src/main/res/raw/templates.csv"
        val transactionHelper = TransactionHelper()
        val templates =  transactionHelper.readCsvToTemplatesList(context)
        return templates
    }

    fun readSMS(context: Context){
        var permissonHelper = PermissionHelper
        try {
            if (!permissonHelper.isSmsPermissionGranted(context)) {
                requestSmsPermission {
                    readProcessSMS(context)
                }
            } else {
                readProcessSMS(context)
            }
        }
        catch (e:Exception){
            println("Permission Granted")
        }
    }



    fun readSMS(view: View){
        var permissonHelper = PermissionHelper
        try {
            if (!permissonHelper.isSmsPermissionGranted(this)) {
                requestSmsPermission {
                    readProcessSMS(this)
                }
            } else {
                readProcessSMS(this)
            }
        }
        catch (e:Exception){
            println("Permission Not Granted")
        }
    }


    fun readProcessSMS(context: Context){

        val fileName = "report.json";
        val messages = readSMSData(context)
        val templates = createTemplateList(messages,context)
        val transactionList =  trasactionListWHCreator(context,messages,templates)
        var reportHelper = ReportHelper()
        var report = reportHelper.reportCreator(transactionList,context)

        val fileExists = context.getFileStreamPath(fileName).exists()
        if (fileExists) {
            var messagesHelper =  MessagesHelper()
            var reportString = messagesHelper.readFile("/data/user/0/com.example.wallnut/files/report.json")
            val gson = Gson()
            var currentReport = gson.fromJson(reportString, Report::class.java)
            report.budget = currentReport.budget
        }

        val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(report.toString().toByteArray())

        context.startActivity(Intent(context, IntroRouterActivity::class.java))
    }

    private fun readExistingReport(context: Context, fileName: String): String {
        val fileInputStream: FileInputStream = context.openFileInput(fileName)
        val buffer = ByteArray(fileInputStream.available())
        fileInputStream.read(buffer)
        fileInputStream.close()
        return String(buffer, Charsets.UTF_8)
    }


    private fun trasactionListWHCreator(context: Context, messages: Messages, templates: List<Template>): MutableList<Transaction> {

        var transactionHeper = TransactionHelper()
        var trasactionListWH = mutableListOf<Transaction>()
        var reportHelper = ReportHelper()

        val messagesInfo =  reportHelper.readCsvToTemplateInfoList(context)

        for(template:Template in templates) {
            for (message: Message in messages.getMessageData()) {

                val transactionID = template.transactionIdPattern
                var trasaction = transactionHeper.extractFieldsFromMessage(
                    message.body,
                    template.debitMessageRegex,
                    transactionID
                )
                if(trasaction.id=="Success") {
                    if(!trasactionListWH.contains(trasaction)) {
                        trasactionListWH.add(trasaction)
                        val transactionInfo = messagesInfo.get(transactionID)
                        if (transactionInfo != null) {
                            trasaction.transactionInfo = transactionInfo
                        }
                    }
                }

            }
        }
        return trasactionListWH
    }



}
