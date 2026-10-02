package com.example.lab03_2daunidad.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "maintenance_records"
)
data class MaintenanceEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val vehicleId: Long,

    val type: String,

    val date: String,

    val mileage: Int,

    val cost: Double,

    val nextMileage: Int?,

    val nextDate: String,

    val notes: String
)