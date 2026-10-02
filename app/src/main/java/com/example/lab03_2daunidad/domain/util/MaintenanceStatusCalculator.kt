package com.example.lab03_2daunidad.domain.util

import com.example.lab03_2daunidad.domain.model.Maintenance
import com.example.lab03_2daunidad.domain.model.MaintenanceStatus
import com.example.lab03_2daunidad.domain.model.MaintenanceStatusInfo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import com.example.lab03_2daunidad.domain.util.toMileageText

private val dateFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun Maintenance.calculateStatus(
    currentMileage: Int,
    today: LocalDate = LocalDate.now()
): MaintenanceStatus {

    return calculateStatusInfo(
        currentMileage = currentMileage,
        today = today
    ).status
}

fun Maintenance.calculateStatusInfo(
    currentMileage: Int,
    today: LocalDate = LocalDate.now()
): MaintenanceStatusInfo {

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

    // Vencido por kilometraje
    if (
        mileageDifference != null &&
        mileageDifference <= 0
    ) {

        return MaintenanceStatusInfo(
            status = MaintenanceStatus.OVERDUE,
            message =
                "Vencido por ${(-mileageDifference).toMileageText()}"
        )
    }

    // Vencido por fecha
    if (
        daysDifference != null &&
        daysDifference < 0
    ) {

        return MaintenanceStatusInfo(
            status = MaintenanceStatus.OVERDUE,
            message = "Vencido por fecha hace ${-daysDifference} día(s)"
        )
    }

    // Próximo por kilometraje
    if (
        mileageDifference != null &&
        mileageDifference <= 1000
    ) {

        return MaintenanceStatusInfo(
            status = MaintenanceStatus.UPCOMING,
            message =
                "Faltan ${mileageDifference.toMileageText()}"
        )
    }

    // Próximo por fecha
    if (
        daysDifference != null &&
        daysDifference <= 30
    ) {

        return MaintenanceStatusInfo(
            status = MaintenanceStatus.UPCOMING,
            message = "Faltan $daysDifference día(s)"
        )
    }

    // Al día por kilometraje
    if (
        mileageDifference != null &&
        mileageDifference > 1000
    ) {

        return MaintenanceStatusInfo(
            status = MaintenanceStatus.OK,
            message = "Faltan $mileageDifference km"
        )
    }

    // Al día por fecha
    if (
        daysDifference != null &&
        daysDifference > 30
    ) {

        return MaintenanceStatusInfo(
            status = MaintenanceStatus.OK,
            message = "Faltan $daysDifference día(s)"
        )
    }

    return MaintenanceStatusInfo(
        status = MaintenanceStatus.OK,
        message = "Sin próximo mantenimiento definido"
    )
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