/**
 * This class represents the main activity of the Wallnut application.
 * It extends the AppCompatActivity class from the Android framework.
 */
package com.example.wallnut.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wallnut.report.GenerateReportView

class MainPageActivity : AppCompatActivity() {
    /**
     * Called for home screen
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initializes and displays the GenerateReportView for this activity.
        GenerateReportView(this)
    }
}
