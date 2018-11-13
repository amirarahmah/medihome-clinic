package com.example.asus.medihome_clinic.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.util.PreferenceHelper
import com.example.asus.medihome_clinic.util.PreferenceHelper.get
import com.example.asus.medihome_clinic.util.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateFragment(HomeFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        sendTokenToServer()

    }

    private fun sendTokenToServer() {
        Log.d("MainActivity", "send token to server")
        val prefs = PreferenceHelper.defaultPrefs(this)

        if (!prefs["tokenSended", false]!!) {
            val token: String? = prefs["firebaseToken"]
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            val userRef = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("userclinic")
                    .child(userId!!)


            userRef.child("token").setValue(token).addOnCompleteListener {
                if (it.isSuccessful) {
                    prefs["tokenSended"] = true
                }
            }

        }

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar?.title = "MediHome for Clinic"
                updateFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_reservasi -> {
                supportActionBar?.title = "Reservasi"
                updateFragment(ReservasiFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_antrian -> {
                supportActionBar?.title = "Antrian"
                updateFragment(AntrianFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                supportActionBar?.title = "Profil"
                updateFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun updateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit()
    }

    private var exit: Boolean? = false
    override fun onBackPressed() {
        if (exit!!) {
            finishAffinity() // finish all parent activities
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, (3 * 1000).toLong())
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

}
