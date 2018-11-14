package com.example.asus.medihome_clinic.ui.reservasi

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asus.medihome_clinic.R
import com.example.asus.medihome_clinic.model.Reservation
import kotlinx.android.synthetic.main.item_reservasi.view.*

class ReservasiAdapter(val listReservasi : ArrayList<Reservation>, val mContext: Context,
                       val clickListener: (Reservation, Int) -> Unit,
                       val clickListener2: (Reservation, Int) -> Unit)
    : RecyclerView.Adapter<ReservasiAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reservasi, parent, false)
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

        if(reservation.confirmed == "true"){
            holder.buttonContainer.visibility = View.GONE
            holder.statusTv.visibility = View.VISIBLE
            holder.telahDikonfirmasiTv.visibility = View.VISIBLE
        }else{
            holder.buttonContainer.visibility = View.VISIBLE
            holder.statusTv.visibility = View.GONE
            holder.telahDikonfirmasiTv.visibility = View.GONE
        }

        holder.pesananContainer.setOnClickListener {
            val intent = Intent(mContext, ReservasiDetailActivity::class.java)
            intent.putExtra("idReservation", reservation.idReservation)
            mContext.startActivity(intent)
        }

        holder.buttonTolak.setOnClickListener {
            clickListener(reservation, position)
        }

        holder.buttonTerima.setOnClickListener {
            clickListener2(reservation, position)
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
        val buttonContainer = itemView.button_container
        val buttonTolak = itemView.tolak_btn
        val buttonTerima = itemView.terima_btn
        val telahDikonfirmasiTv = itemView.telah_dikonfirmasi_tv
        val statusTv = itemView.status_tv

    }

}