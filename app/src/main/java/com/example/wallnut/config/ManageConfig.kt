package com.example.wallnut.config

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.wallnut.activity.IntroRouterActivity

/**
 * ManageConfig is responsible for managing the application's configuration and routing logic.
 *
 * @param introRouterActivity The activity where this configuration is managed.
 */
class ManageConfig(private val introRouterActivity: IntroRouterActivity) {

    private var configState: SharedPreferences
    private var isFirstTime: Boolean

    /**
     * Initializes the ManageConfig instance.
     */
    init {
        this.isFirstTime = true
        val configState = introRouterActivity.getSharedPreferences("configState", Context.MODE_PRIVATE)
        this.configState = configState!!
        initializeConfig()
    }

    /**
     * Initializes the application configuration.
     */
    private fun initializeConfig() {
        this.isFirstTime = configState.getBoolean("firstTime", true)
    }

    /**
     * Get the route code for navigation.
     *
     * @return The route code for navigation:
     *   - 1 if it's the first time launching the app,
     *   - 2 if SMS permissions are not granted,
     *   - 3 for other cases.
     */
    fun getRoute(): Int {
        return if (isFirstTime) {
            val editor = configState.edit()
            editor?.putBoolean("firstTime", false)
            editor?.apply()
            1
        } else if (!isSmsPermissionGranted(introRouterActivity)) {
            2
        } else {
            3
        }
    }

    private fun isSmsPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
