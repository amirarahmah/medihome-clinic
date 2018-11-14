package com.example.asus.medihome_clinic.ui.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.ui.reservasi.ReservasiAdapter
import kotlinx.android.synthetic.main.fragment_reservasi.*


class HomeFragment : Fragment() {

    lateinit var mAdapter : JadwalDokterAdapter
    lateinit var listJadwal : ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listJadwal =  arrayListOf("drg. Haikal Achmad, Sp.BM", "drg. Lilia Putri, Sp.KG",
                "drg. Muhammad Bagus, Sp.Ort")

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        mAdapter = JadwalDokterAdapter(context!!, listJadwal)
        recyclerView.adapter = mAdapter
    }

}
