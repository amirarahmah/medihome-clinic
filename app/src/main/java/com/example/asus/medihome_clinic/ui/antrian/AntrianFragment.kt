package com.example.asus.medihome_clinic.ui.antrian


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.model.Reservation
import com.example.asus.medihome_clinic.ui.reservasi.ReservasiAdapter
import com.example.asus.medihome_clinic.util.PreferenceHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_antrian.*


class AntrianFragment : Fragment() {

    lateinit var mAdapter: AntrianAdapter
    lateinit var pesananList: ArrayList<Reservation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_antrian, container, false)
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
                            if (pesanan?.confirmed == "true") {
                                pesananList.add(pesanan)
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
        mAdapter = AntrianAdapter(pesananList, context!!)
        recyclerView.adapter = mAdapter
    }

}
