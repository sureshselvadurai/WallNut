package com.example.wallnut.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class IntroRouterActivity: AppCompatActivity() {


    private fun isSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val OnboardingScreen = this?.getSharedPreferences( "OnboardingScreen",Context.MODE_PRIVATE)
        val isFirstTime = OnboardingScreen?.getBoolean("firstTime",true);

        if(isFirstTime == true){

            val editor = OnboardingScreen?.edit()
            editor?.putBoolean("firstTime",false);
            editor?.commit();
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()

        }else if(!isSmsPermissionGranted()){
            startActivity(Intent(this, SMSSlider::class.java))
            finish()
        }else{
            startActivity(Intent(this, MainPageActivity::class.java))
            finish()
        }
    }
}