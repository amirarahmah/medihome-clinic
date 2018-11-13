package com.example.asus.medihome_clinic.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.example.asus.medihome_clinic.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val splashInterval = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_screen)

        val user = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed(object : Runnable {

            override fun run() {

                if(user != null) {
                    val i = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(i)
                }else{
                    val i = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(i)
                }
                this.finish()
            }

            private fun finish() {

            }
        }, splashInterval.toLong())

    }

    override fun onPause() {

        super.onPause()
        finish()
    }
}
