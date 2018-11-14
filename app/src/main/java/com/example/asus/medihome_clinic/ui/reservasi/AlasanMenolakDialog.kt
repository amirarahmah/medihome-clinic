package com.example.asus.medihome_clinic.ui.reservasi

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.asus.medihome_clinic.R
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import kotlinx.android.synthetic.main.alasan_menolak_dialog.*

class AlasanMenolakDialog : DialogFragment() {

    lateinit var mListener : AlasanMenolakDialogEvent

    interface AlasanMenolakDialogEvent {
        fun onAlasanSelected(alasan: String, idReservasi : String)
    }

    fun setAlasanMenolakDialogEvent(listener: AlasanMenolakDialogEvent) {
        this.mListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.alasan_menolak_dialog, container,
                false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idReservasi = arguments?.getString("idReservasi")

        batal_btn.setOnClickListener {
            dialog.dismiss()
        }

        kirim_btn.setOnClickListener {
            val idxAlasan = rg_alasan_menolak.checkedRadioButtonId
            val rbAlasan = view.findViewById<RadioButton>(idxAlasan)

            if(rbAlasan == null){
                Toast.makeText(context, "Pilih alasan menolak" , Toast.LENGTH_SHORT).show()
            }else{
                mListener.onAlasanSelected(rbAlasan.text.toString(), idReservasi!!)
                dialog.dismiss()
            }
        }

    }


}