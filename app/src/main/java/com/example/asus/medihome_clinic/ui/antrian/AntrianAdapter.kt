package com.example.asus.medihome_clinic.ui.antrian

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.model.Reservation
import com.example.asus.medihome_clinic.ui.reservasi.ReservasiDetailActivity
import kotlinx.android.synthetic.main.item_reservasi.view.*

class AntrianAdapter(val listReservasi : ArrayList<Reservation>, val mContext: Context)
    : RecyclerView.Adapter<AntrianAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_antrian, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listReservasi.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reservation = listReservasi[position]

        holder.namaLayananTv.text = reservation.layanan
        holder.namaDokterTv.text = reservation.dokter
        holder.hargaTv.text = reservation.harga
        holder.idReservasiTv.text = reservation.idReservation
        holder.namaPasienTv.text = reservation.namaPasien
        holder.tanggalTv.text = reservation.tanggal
        holder.pukulTv.text = reservation.pukul

        if(reservation.payment == "paid"){
            holder.statusTv.text = "Pembayaran Berhasil"
            holder.statusTv.background = ContextCompat.getDrawable(mContext, R.drawable.oval_background)
        }else{
            holder.statusTv.text = "Menunggu Pembayaran"
            holder.statusTv.background = ContextCompat.getDrawable(mContext, R.drawable.oval_background_yellow)
        }

        holder.pesananContainer.setOnClickListener {
            val intent = Intent(mContext, DetailAntrianActivity::class.java)
            intent.putExtra("idReservation", reservation.idReservation)
            mContext.startActivity(intent)
        }


    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pesananContainer = itemView.pesanan_clinic_container
        val namaLayananTv = itemView.nama_layanan
        val namaDokterTv = itemView.nama_dokter
        val hargaTv = itemView.harga_layanan
        val idReservasiTv = itemView.id_reservasi_tv
        val namaPasienTv = itemView.nama_pasien_tv
        val tanggalTv = itemView.tanggal
        val pukulTv = itemView.pukul
        val statusTv = itemView.status_tv

    }

}