/**
 * IntroRouterActivity is an Android activity class responsible for routing users to different activities
 * based on a configuration value obtained from the ManageConfig instance. It extends the AppCompatActivity class.
 *
 * When this activity is created, it checks the route value from ManageConfig and starts the corresponding
 * activity using an explicit Intent. After routing the user to the appropriate activity, this activity is
 * finished to ensure it doesn't remain in the back stack.
 */
package com.example.wallnut.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wallnut.config.ManageConfig

class IntroRouterActivity : AppCompatActivity() {
    /**
     * Override of the onCreate method to perform the routing logic based on the configuration value.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a ManageConfig instance to access configuration values.
        val manageConfig = ManageConfig(this)
        when (manageConfig.getRoute()) {
            1 ->                 // If route is 1, start the OnboardingActivity.
                startActivity(Intent(this, OnboardingActivity::class.java))

            2 ->                 // If route is 2, start the SMSSlider activity.
                startActivity(Intent(this, SMSSlider::class.java))

            else ->                 // For any other route value, start the MainPageActivity as the default.
                startActivity(Intent(this, MainPageActivity::class.java))
        }

        // Finish this activity to prevent it from remaining in the back stack.
        finish()
    }
}