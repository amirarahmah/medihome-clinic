package com.example.asus.medihome_clinic.ui.reservasi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.model.Reservation
import com.example.asus.medihome_clinic.model.User
import com.example.asus.medihome_clinic.util.PreferenceHelper
import com.example.asus.medihome_clinic.util.PreferenceHelper.set
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_reservasi_detail.*

class ReservasiDetailActivity : AppCompatActivity() {

    var idReservation : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservasi_detail)

        supportActionBar?.title = "Detail Reservasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idReservation = intent?.extras?.getString("idReservation")

        val reservasinRef = FirebaseDatabase.getInstance().reference.child("reservasi")

        reservasinRef.child(idReservation!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val reservation = p0.getValue(Reservation::class.java)
                        updateUI(reservation)
                    }
                })

    }

    private fun updateUI(reservation: Reservation?) {
        nama_layanan.text = reservation?.layanan
        nama_dokter.text = reservation?.dokter
        harga_layanan.text = reservation?.harga
        tanggal.text = reservation?.tanggal
        pukul.text = reservation?.pukul
        id_reservasi_tv.text = reservation?.idReservation

        //data pasien
        nama_lengkap_pasien.text = "Nama Lengkap : ${reservation?.namaPasien}"
        tanggal_lahir_pasien.text = "Tanggal Lahir : ${reservation?.tanggalLahir}"
        kelamin_pasien.text = "Kelamin : ${reservation?.jenisKelamin}"
        email_pasien.text = "Email : ${reservation?.emailPasien}"
        no_telepon_pasien.text = "No. telepon : ${reservation?.noTelpPasien}"
        no_rekam_pasien.text = "No Rekam Medis : ${reservation?.noRekam}"

        pesan_tv.text = reservation?.pesan

        getDataPemesan(reservation?.idUser)
    }

    private fun getDataPemesan(idUser: String?) {
        val userRef = FirebaseDatabase.getInstance().reference.child("users")

        userRef.child(idUser!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                nama_lengkap_pemesan.text = "Nama Lengkap : ${user?.nama}"
                email_pemesan.text = "Alamat Email : ${user?.email}"
                no_telepon_pemesan.text = "Nomor Telepon : ${user?.nomorTelpon}"
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }
}
