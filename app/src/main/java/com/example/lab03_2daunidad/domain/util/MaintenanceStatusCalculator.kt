package com.example.lab03_2daunidad.domain.util

import com.example.lab03_2daunidad.domain.model.Maintenance
import com.example.lab03_2daunidad.domain.model.MaintenanceStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

private val dateFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun Maintenance.calculateStatus(
    currentMileage: Int,
    today: LocalDate = LocalDate.now()
): MaintenanceStatus {

    val mileageDifference =
        nextMileage?.minus(currentMileage)

    val nextMaintenanceDate =
        parseDate(nextDate)

    val daysDifference =
        nextMaintenanceDate?.let {
            ChronoUnit.DAYS.between(
                today,
                it
            )
        }

    if (
        mileageDifference != null &&
        mileageDifference <= 0
    ) {
        return MaintenanceStatus.OVERDUE
    }

    if (
        daysDifference != null &&
        daysDifference < 0
    ) {
        return MaintenanceStatus.OVERDUE
    }

    if (
        mileageDifference != null &&
        mileageDifference <= 1000
    ) {
        return MaintenanceStatus.UPCOMING
    }

    if (
        daysDifference != null &&
        daysDifference <= 30
    ) {
        return MaintenanceStatus.UPCOMING
    }

    return MaintenanceStatus.OK
}

private fun parseDate(
    value: String
): LocalDate? {

    if (value.isBlank()) {
        return null
    }

    return try {

        LocalDate.parse(
            value,
            dateFormatter
        )

    } catch (_: DateTimeParseException) {

        null
    }
}