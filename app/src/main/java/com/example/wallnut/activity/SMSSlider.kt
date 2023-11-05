/**
 * An activity responsible for managing SMS-related functionalities.
 */
package com.example.wallnut.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallnut.R
import com.example.wallnut.databinding.SmsPermissionBinding
import com.example.wallnut.report.BaseReport

class SMSSlider : AppCompatActivity() {
    private lateinit var binding: SmsPermissionBinding
    private lateinit var baseReport: BaseReport

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
     * Handles the result of a permission request.
     *
     * @param requestCode The code that is passed to requestPermissions().
     * @param permissions The requested permissions.
     * @param grantResults The results of the permission requests.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (grantResults[0]) {
            0 -> {
                baseReport.invokeCallback()
            }
            -1 -> {
                // Display a toast message if SMS permission is denied, and exit the app.
                Toast.makeText(this, "SMS permission denied. App will exit.", Toast.LENGTH_SHORT).show()
                finish() // Exit the activity
            }
        }
    }

    /**
     * Generate a report, requesting SMS permissions if needed.
     *
     * @param view The view that triggered the generation of the report.
     */
    fun generateReportSlider(view: View) {
        baseReport = BaseReport(this)
    }
}