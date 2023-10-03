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
import com.example.wallnut.helper.MessagesHelper
import com.example.wallnut.helper.PermissionHelper
import com.example.wallnut.helper.ReportHelper
import com.example.wallnut.helper.TransactionHelper
import com.example.wallnut.model.Message
import com.example.wallnut.model.Messages
import com.example.wallnut.model.Report
import com.example.wallnut.model.Template
import com.example.wallnut.model.Transaction
import java.io.FileOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class SMSSlider : AppCompatActivity() {

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
    }

    private fun readSMSData(): Messages {
        val messageManager = Messages
        val cursor =
            this.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)

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
        MessagesHelper.writeToFile(messageManager,this)
        return messageManager

    }

    private fun requestSmsPermission(callback: () -> Unit) {
        onSmsPermissionGrantedCallback = callback
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_SMS),
            PackageManager.PERMISSION_GRANTED
        )
    }

    fun createTemplateList(messages:Messages): List<Template> {
        val templatePath = "app/src/main/res/raw/templates.csv"
        val transactionHelper = TransactionHelper()
        val templates =  transactionHelper.readCsvToMessagesList(messages,templatePath,this)
        return templates
    }


    fun readSMS(view: View){
        var permissonHelper = PermissionHelper

        if (!permissonHelper.isSmsPermissionGranted(this)) {
            requestSmsPermission {
                // Permission granted callback
            }
        } else {
            // Permission already granted

        }

        val messages = readSMSData()
        val templates = createTemplateList(messages)
        val transactionList =  trasactionListWHCreator(messages,templates)
        var reportHelper = ReportHelper()
        var report = reportHelper.reportCreator(transactionList)

        val fileOutputStream: FileOutputStream = this.openFileOutput("report.json", Context.MODE_PRIVATE)
        fileOutputStream.write(report.toString().toByteArray())

        startActivity(Intent(this, IntroRouterActivity::class.java))
    }

    private fun trasactionListWHCreator(messages: Messages, templates: List<Template>): MutableList<Transaction> {

        var transactionHeper = TransactionHelper()
        var trasactionListWH = mutableListOf<Transaction>()

        for(template:Template in templates) {
            for (message: Message in messages.getMessageData()) {

                val transactionID = template.transactionIdPattern
                var trasaction = transactionHeper.extractFieldsFromMessage(
                    message.body,
                    template.debitMessageRegex,
                    transactionID
                )
                if(trasaction.id=="Success") {
                    trasactionListWH.add(trasaction)
                }
            }
        }
        return trasactionListWH
    }



}
