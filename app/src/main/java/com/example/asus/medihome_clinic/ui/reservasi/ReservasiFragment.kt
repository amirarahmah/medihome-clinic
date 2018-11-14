package com.example.asus.medihome_clinic.ui.reservasi


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.model.Reservation
import com.example.asus.medihome_clinic.model.User
import com.example.asus.medihome_clinic.model.api.Data
import com.example.asus.medihome_clinic.model.api.NotifReservasi
import com.example.asus.medihome_clinic.model.api.NotifResponse
import com.example.asus.medihome_clinic.model.api.NotifService
import com.example.asus.medihome_clinic.util.Constant
import com.example.asus.medihome_clinic.util.PreferenceHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_reservasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservasiFragment : Fragment(), AlasanMenolakDialog.AlasanMenolakDialogEvent {

    var alasanMenolak = ""
    var userId = ""
    var namaKlinik = ""
    var idReservasi = ""

    lateinit var mAdapter: ReservasiAdapter
    lateinit var pesananList: ArrayList<Reservation>

    override fun onAlasanSelected(alasan: String, idReservasi: String) {
        alasanMenolak = alasan
        val reservasinRef = FirebaseDatabase.getInstance().reference.child("reservasi")
        reservasinRef.child(idReservasi).child("confirmed").setValue("false")
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        sendNotifToUser("tolak")
                        Toast.makeText(context, "Reservasi ditolak", Toast.LENGTH_SHORT).show()
                    }
                }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservasi, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pesananList = arrayListOf()

        setupRecyclerView()
        progressBar?.let { it.visibility = View.VISIBLE }

        val reservasinRef = FirebaseDatabase.getInstance().reference.child("reservasi")
        val prefs = PreferenceHelper.defaultPrefs(context!!)

        val idKlinik = prefs.getString("idKlinik", "")

        reservasinRef.orderByChild("idKlinik").equalTo(idKlinik!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        progressBar?.let { it.visibility = View.INVISIBLE }
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        progressBar?.let { it.visibility = View.INVISIBLE }
                        pesananList.clear()
                        for (data in p0.children) {
                            val pesanan = data.getValue(Reservation::class.java)
                            if (pesanan?.confirmed != "false") {
                                pesananList.add(pesanan!!)
                            }
                        }

                        if (pesananList.size > 0) {
                            tidak_ada_reservasi_tv?.visibility = View.GONE
                        } else {
                            tidak_ada_reservasi_tv?.visibility = View.VISIBLE
                        }

                        mAdapter.notifyDataSetChanged()
                    }
                })

    }


    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        mAdapter = ReservasiAdapter(pesananList, context!!,
                { reservation, i -> onTolakButtonClicked(reservation, i) },
                { reservation, i -> onTerimaButtonClicked(reservation, i) })
        recyclerView.adapter = mAdapter
    }

    private fun onTolakButtonClicked(reservasi: Reservation, i: Int) {
        namaKlinik = reservasi.namaKlinik
        userId = reservasi.idUser
        idReservasi = reservasi.idReservation
        val alasanMenolakDialog = AlasanMenolakDialog()
        val args = Bundle()
        args.putString("idReservasi", reservasi.idReservation)
        alasanMenolakDialog.arguments = args
        alasanMenolakDialog.setAlasanMenolakDialogEvent(this@ReservasiFragment)
        alasanMenolakDialog.show(childFragmentManager,
                "alasan_menolak_dialog_fragment")
    }

    private fun onTerimaButtonClicked(reservasi: Reservation, i: Int) {
        namaKlinik = reservasi.namaKlinik
        userId = reservasi.idUser
        idReservasi = reservasi.idReservation
        val userRef = FirebaseDatabase
                .getInstance()
                .reference
                .child("reservasi")
                .child(reservasi.idReservation)


        userRef.child("confirmed").setValue("true").addOnCompleteListener {
            if (it.isSuccessful) {
                sendNotifToUser("terima")
                mAdapter.notifyItemChanged(i)
            }
        }
    }

    private fun sendNotifToUser(status: String) {
        val userRef = FirebaseDatabase
                .getInstance()
                .reference
                .child("users")
                .child(userId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                val token = user?.token
                Log.d("ReservasiFragment", "token $token")

                lateinit var notifData: Data
                if (status == "terima") {
                    notifData = Data(idReservasi,"Reservasi yang Anda ajukan diterima. " +
                            "Lakukan pembayaran untuk melanjukan",
                            namaKlinik)
                } else {
                    notifData = Data(idReservasi,"Reservasi yang Anda ajukan diterima. " +
                            "Lakukan pembayaran untuk melanjukan",
                            namaKlinik)
                }


                    val notif = NotifReservasi(notifData, token!!)

                    NotifService.create().sendNotifToClinic(notif, "key=${Constant.CLOUD_MESSAGE_KEY}")
                            .enqueue(object : Callback<NotifResponse> {
                                override fun onFailure(call: Call<NotifResponse>, t: Throwable) {

                                }

                                override fun onResponse(call: Call<NotifResponse>, response: Response<NotifResponse>) {

                                }

                            })


            }
        })

    }

}
