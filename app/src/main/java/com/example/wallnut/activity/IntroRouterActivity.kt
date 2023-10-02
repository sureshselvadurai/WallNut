package com.example.wallnut.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wallnut.helper.PermissionHelper

class IntroRouterActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configState = this?.getSharedPreferences( "configState",Context.MODE_PRIVATE)
        val isFirstTime = configState?.getBoolean("firstTime",true);

        if(isFirstTime == true){

            val editor = configState?.edit()
            editor?.putBoolean("firstTime",false);
            editor?.commit();
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()

        }else if(!PermissionHelper.isSmsPermissionGranted(this)){
            startActivity(Intent(this, SMSSlider::class.java))
            finish()
        }else{
            startActivity(Intent(this, MainPageActivity::class.java))
            finish()
        }
    }
}