package com.example.asus.medihome_clinic.ui.home

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.asus.medihome_clinic.R

class JadwalDokterAdapter(var mContext: Context,
                    var listJadwal: ArrayList<String>)
    : RecyclerView.Adapter<JadwalDokterAdapter.MyViewHolder>() {

    private var lastCheckedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jadwal_dokter, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listJadwal.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val jadwal = listJadwal[position]
        holder.jadwalTv.text = jadwal

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jadwalTv = itemView.findViewById<TextView>(R.id.nama_dokter)


    }

}