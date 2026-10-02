package com.example.lab03_2daunidad.domain.model

data class Maintenance(
    val id: Long = 0L,
    val vehicleId: Long = 1L,
    val type: String = "",
    val date: String = "",
    val mileage: Int = 0,
    val cost: Double = 0.0,
    val nextMileage: Int? = null,
    val nextDate: String = "",
    val notes: String = ""
)