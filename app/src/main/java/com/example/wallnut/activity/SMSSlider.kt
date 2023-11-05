package com.example.wallnut.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.wallnut.R
import com.example.wallnut.databinding.SmsPermissionBinding
import com.example.wallnut.report.BaseReport

/**
 * An activity responsible for managing SMS-related functionalities.
 */
class SMSSlider : AppCompatActivity() {

    private lateinit var binding: SmsPermissionBinding

    /**
     * Called when the activity is first created. Initializes the activity's layout.
     *
     * @param savedInstanceState If the activity is being re-initialized after a previous
     *     instance was destroyed, this Bundle contains the data it most recently supplied in
     *     onSaveInstanceState.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sms_permission)
        binding = SmsPermissionBinding.inflate(layoutInflater)

    }

    /**
     * Generate a report, requesting SMS permissions if needed.
     */
    fun generateReportSlider(view: View) {
        BaseReport(this)
    }
}
