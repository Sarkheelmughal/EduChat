package com.example.educhat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.educhat.Functional.Home
import com.example.educhat.Functional.Profile
import com.example.educhat.Login.StartScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        FirebaseApp.initializeApp(this)
        val currentUsers = FirebaseAuth.getInstance().currentUser
        Handler().postDelayed({
            //start activity
            if(currentUsers != null){
//                startActivity(Intent(this@SplashScreen,
//                    Home::class.java))

                checkProfile()
            }
            else{

                startActivity(Intent(this@SplashScreen,
                    StartScreen::class.java))
                finish()
            }
            //end activity

        },1800)
    }


    private fun checkProfile() {
        val sharedPreference =  getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
//        var editor = sharedPreference.edit()
//        editor.putString("profileComplete","yes")
//        editor.commit()
        val news= sharedPreference.getString("profileComplete","null")

        if (news=="null"){
            val intent=Intent(this@SplashScreen, Profile::class.java)


            startActivity(intent)
            finish()

        }
        else {
            startActivity(Intent(this@SplashScreen, Home::class.java))

            finish()

        }

    }


}
