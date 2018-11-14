package com.example.asus.medihome_clinic.model

data class Reservation(var idReservation : String = "", var idKlinik : String = "",
                       var idUser : String = "", var namaKlinik : String = "",
                       var alamatKlinik : String = "", var layanan : String = "",
                       var dokter : String = "", var harga : String = "",
                       var tanggal : String = "", var pukul : String = "",
                       var namaPasien : String = "", var emailPasien : String = "",
                       var noTelpPasien : String = "", var jenisKelamin : String = "",
                       var tanggalLahir : String = "", var noRekam : String = "",
                       var pesan : String = "", var confirmed : String = "",
                       var payment : String = "")