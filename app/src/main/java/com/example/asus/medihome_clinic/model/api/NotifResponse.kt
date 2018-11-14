package com.example.asus.medihome_clinic.model.api

data class NotifResponse(
        val canonical_ids: Int,
        val failure: Int,
        val multicast_id: Long,
        val results: List<Result>,
        val success: Int
)