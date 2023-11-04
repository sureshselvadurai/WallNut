package com.example.wallnut.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wallnut.R
import com.example.wallnut.databinding.MainPageBinding

/**
 * An activity responsible for managing SMS-related functionalities.
 */
class SMSSlider : AppCompatActivity() {

    private lateinit var binding: MainPageBinding

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
        binding = MainPageBinding.inflate(layoutInflater)
    }
}
