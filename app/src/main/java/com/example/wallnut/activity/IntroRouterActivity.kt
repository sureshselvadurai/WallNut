package com.example.wallnut.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class IntroRouterActivity: AppCompatActivity() {


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

        }else{
            startActivity(Intent(this, MainPageActivity::class.java))
            finish()
        }

//        if (AppPreferences.isFirstTimeUser(this)) {
//            startActivity(Intent(this, OnboardingActivity::class.java))
//        } else {
//            startActivity(Intent(this, MainPageActivity::class.java))
//        }
        finish()
    }
}