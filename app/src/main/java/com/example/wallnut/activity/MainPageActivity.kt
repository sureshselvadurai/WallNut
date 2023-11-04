package com.example.wallnut.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wallnut.report.GenerateReportView

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GenerateReportView(this)
    }

}