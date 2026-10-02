package com.example.lab03_2daunidad.domain.model

data class Vehicle(
    val id: Long = 0L,
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val plate: String = "",
    val currentMileage: Int = 0
)