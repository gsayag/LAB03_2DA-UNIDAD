package com.example.lab03_2daunidad.domain.util

import java.util.Locale

fun Int.toMileageText(): String {
    return String.format(
        Locale.US,
        "%,d km",
        this
    )
}

fun Double.toSolesText(): String {
    return String.format(
        Locale.US,
        "S/ %,.2f",
        this
    )
}