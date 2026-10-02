package com.example.lab03_2daunidad.data.mapper

import com.example.lab03_2daunidad.data.local.entity.VehicleEntity
import com.example.lab03_2daunidad.domain.model.Vehicle

fun VehicleEntity.toDomain(): Vehicle {

    return Vehicle(
        id = id,
        brand = brand,
        model = model,
        year = year,
        plate = plate,
        currentMileage = currentMileage
    )
}

fun Vehicle.toEntity(): VehicleEntity {

    return VehicleEntity(
        id = if (id == 0L) 1L else id,
        brand = brand,
        model = model,
        year = year,
        plate = plate,
        currentMileage = currentMileage
    )
}