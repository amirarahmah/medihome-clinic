package com.example.asus.medihome_clinic.ui


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.asus.medihome_clinic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lmntrx.android.library.livin.missme.ProgressDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Silahkan Menunggu..")
        progressDialog.setCancelable(false)

        login_btn.setOnClickListener {
            loginUser()
        }


    }


    private fun loginUser() {

        val email = email_et.text.toString().trim()
        val password = password_et.text.toString().trim()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressDialog.show()
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            getDataUser()
                        } else {
                            progressDialog.dismiss()
                            val errorCode = (task.exception as FirebaseAuthException).errorCode

                            when (errorCode) {

                                "ERROR_USER_NOT_FOUND" ->
                                    Toast.makeText(this, "Akun belum terdaftar",
                                            Toast.LENGTH_LONG).show()
                                else ->
                                    Toast.makeText(this, ""+
                                            (task.exception as FirebaseAuthException).message,
                                            Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        } else if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Mohon masukan email dan password", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Mohon masukan email", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Mohon masukan password", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getDataUser() {

        val userRef = FirebaseDatabase.getInstance().reference.child("userclinic")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userRef.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                progressDialog.dismiss()
                if(p0.exists()){
                    navigateToMainActivity()
                }else{
                    mAuth.signOut()
                    Toast.makeText(this@LoginActivity, "Akun belum terdaftar sebagai klinik", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        currentUser?.let {
            navigateToMainActivity()
        }
    }

}
