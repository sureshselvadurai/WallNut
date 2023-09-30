package com.example.wallnut.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wallnut.model.Messages
import com.example.wallnut.R


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

    private fun readSMSData(){
        val messageManager = Messages

        val cursor =
            this.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)

        cursor?.use {
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)

            while (it.moveToNext()) {
                val address = it.getString(addressIndex)
                val body = it.getString(bodyIndex)
                val date = it.getLong(dateIndex)
                messageManager.addMessage(address, body, date)
                System.out.println(body);

            }
        }
    }
    private fun isSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSmsPermission(callback: () -> Unit) {
        onSmsPermissionGrantedCallback = callback
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_SMS),
            PackageManager.PERMISSION_GRANTED
        )
    }

    fun readSMS(view: View){

        if (!isSmsPermissionGranted()) {
            requestSmsPermission {
                // Permission granted callback
                readSMSData()
                startActivity(Intent(this, IntroRouterActivity::class.java))
            }
        } else {
            // Permission already granted
            readSMSData()
            startActivity(Intent(this, IntroRouterActivity::class.java))
        }
    }
}
