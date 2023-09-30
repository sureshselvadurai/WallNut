package com.example.wallnut.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.example.wallnut.R
import com.example.wallnut.helper.OnboardingHelper

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var onboardingHelper: OnboardingHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboardingactivity)

        viewPager = findViewById(R.id.viewPager)
        val nextButton = findViewById<Button>(R.id.buttonNext)
        val previousButton = findViewById<Button>(R.id.Previousbutton)

        onboardingHelper = OnboardingHelper(this, viewPager, nextButton,resources,previousButton)
        onboardingHelper.setupOnboarding()
    }
}
