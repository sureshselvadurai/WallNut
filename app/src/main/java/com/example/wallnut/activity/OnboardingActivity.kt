package com.example.wallnut.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.wallnut.R
import com.example.wallnut.adapter.OnboardingHelper

/**
 * An activity responsible for managing the onboarding experience using a ViewPager.
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var onboardingHelper: OnboardingHelper

    /**
     * Called when the activity is first created. Sets up the onboarding experience.
     *
     * @param savedInstanceState If the activity is being re-initialized after a previous
     *     instance was destroyed, this Bundle contains the data it most recently supplied in
     *     onSaveInstanceState.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboardingactivity)

        viewPager = findViewById(R.id.viewPager)
        val nextButton = findViewById<Button>(R.id.buttonNext)
        val previousButton = findViewById<Button>(R.id.Previousbutton)

        onboardingHelper = OnboardingHelper(this, viewPager, nextButton, resources, previousButton)
        onboardingHelper.setupOnboarding()
    }
}
