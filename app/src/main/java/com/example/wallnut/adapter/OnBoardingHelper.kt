package com.example.wallnut.adapter

import OnboardingPagerAdapter
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.View
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.wallnut.R
import com.example.wallnut.activity.IntroRouterActivity
import com.example.wallnut.model.OnboardingItem
import java.util.Properties

/**
 * A helper class for managing onboarding screens using a ViewPager in an Android application.
 *
 * @param context The application context.
 * @param viewPager The ViewPager used for displaying onboarding screens.
 * @param nextButton The refreshButton used to navigate to the next onboarding screen.
 * @param resources The application's resources, used for loading onboarding data.
 * @param previousButton The refreshButton used to navigate to the previous onboarding screen.
 */
class OnboardingHelper(
    private val context: Context,
    private val viewPager: ViewPager,
    private val nextButton: Button,
    private val resources: Resources,
    private val previousButton: Button
) {

    /**
     * Loads onboarding items from a properties file and creates a list of [OnboardingItem] objects.
     *
     * @return A list of [OnboardingItem] objects.
     */
    private fun loadOnboardingItems(): List<OnboardingItem> {

        val onboardingItems = mutableListOf<OnboardingItem>()

        // Load properties file
        val properties = Properties()
        val rawResource = resources.openRawResource(R.raw.properties)
        properties.load(rawResource)

        // Read properties and create OnboardingItem objects
        for (i in 1.rangeTo(3)) {
            val imageResId = resources.getIdentifier(
                /* name = */ properties.getProperty("onboarding_item$i" + "_image"),
                /* defType = */ "drawable",
                /* defPackage = */ context.packageName
            )
            val title = properties.getProperty("onboarding_item$i" + "_title", "Title")
            val description = properties.getProperty("onboarding_item$i" + "_description", "Description")

            onboardingItems.add(OnboardingItem(imageResId, title, description))
        }

        return onboardingItems
    }

    /**
     * Sets up the onboarding experience, including ViewPager, buttons, and page change listeners.
     */
    fun setupOnboarding() {
        val onboardingItems = loadOnboardingItems()
        previousButton.visibility = View.GONE

        val adapter = OnboardingPagerAdapter(context, onboardingItems)
        viewPager.adapter = adapter

        // Set a listener for page change events
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                // Check if the last page is reached and update refreshButton text accordingly
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        nextButton.setOnClickListener {
            // Handle next refreshButton click
            val currentPosition = viewPager.currentItem
            if (currentPosition < onboardingItems.size - 1) {
                viewPager.currentItem = currentPosition + 1
            } else {
                val intent = Intent(context, IntroRouterActivity::class.java)
                context.startActivity(intent)
            }
            if (currentPosition >= 0) {
                previousButton.visibility = View.VISIBLE
            }
        }

        previousButton.setOnClickListener {
            // Handle previous refreshButton click
            val currentPosition = viewPager.currentItem
            viewPager.currentItem = currentPosition - 1
            if (currentPosition <= 1) {
                previousButton.visibility = View.GONE
            }
        }
    }
}
