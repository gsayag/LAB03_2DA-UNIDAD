package com.example.lab03_2daunidad.data.mapper

import com.example.lab03_2daunidad.data.local.entity.MaintenanceEntity
import com.example.lab03_2daunidad.domain.model.Maintenance

fun MaintenanceEntity.toDomain(): Maintenance {

    return Maintenance(
        id = id,
        vehicleId = vehicleId,
        type = type,
        date = date,
        mileage = mileage,
        cost = cost,
        nextMileage = nextMileage,
        nextDate = nextDate,
        notes = notes
    )
}

fun Maintenance.toEntity(): MaintenanceEntity {

    return MaintenanceEntity(
        id = id,
        vehicleId = vehicleId,
        type = type,
        date = date,
        mileage = mileage,
        cost = cost,
        nextMileage = nextMileage,
        nextDate = nextDate,
        notes = notes
    )
}