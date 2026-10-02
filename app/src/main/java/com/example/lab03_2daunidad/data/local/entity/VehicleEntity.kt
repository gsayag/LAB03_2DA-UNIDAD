package com.example.lab03_2daunidad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicles"
)
data class VehicleEntity(

    @PrimaryKey
    val id: Long = 1L,

    val brand: String,

    val model: String,

    val year: Int,

    val plate: String,

    val currentMileage: Int
)