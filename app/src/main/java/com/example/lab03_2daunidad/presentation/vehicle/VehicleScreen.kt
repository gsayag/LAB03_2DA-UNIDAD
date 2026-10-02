package com.example.lab03_2daunidad.presentation.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Vehicle

@Composable
fun VehicleScreen(
    vehicle: Vehicle,
    onSave: (Vehicle) -> Unit,
    onBack: () -> Unit
) {

    var brand by rememberSaveable {
        mutableStateOf(vehicle.brand)
    }

    var model by rememberSaveable {
        mutableStateOf(vehicle.model)
    }

    var year by rememberSaveable {
        mutableStateOf(
            if (vehicle.year == 0) {
                ""
            } else {
                vehicle.year.toString()
            }
        )
    }

    var plate by rememberSaveable {
        mutableStateOf(vehicle.plate)
    }

    var mileage by rememberSaveable {
        mutableStateOf(
            if (vehicle.currentMileage == 0) {
                ""
            } else {
                vehicle.currentMileage.toString()
            }
        )
    }

    val yearNumber = year.toIntOrNull()
    val mileageNumber = mileage.toIntOrNull()

    val formValid =
        brand.isNotBlank() &&
                model.isNotBlank() &&
                yearNumber != null &&
                plate.isNotBlank() &&
                mileageNumber != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onBack
            ) {
                Text("Volver")
            }
        }

        Text(
            text = "Mi vehículo",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Registra la información principal de tu vehículo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = brand,
            onValueChange = { newValue ->
                brand = newValue
            },
            label = {
                Text("Marca")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = model,
            onValueChange = { newValue ->
                model = newValue
            },
            label = {
                Text("Modelo")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = year,
            onValueChange = { newValue ->
                year = newValue.filter { char ->
                    char.isDigit()
                }
            },
            label = {
                Text("Año")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = plate,
            onValueChange = { newValue ->
                plate = newValue.uppercase()
            },
            label = {
                Text("Placa")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = mileage,
            onValueChange = { newValue ->
                mileage = newValue.filter { char ->
                    char.isDigit()
                }
            },
            label = {
                Text("Kilometraje actual")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                if (yearNumber != null && mileageNumber != null) {
                    onSave(
                        vehicle.copy(
                            brand = brand.trim(),
                            model = model.trim(),
                            year = yearNumber,
                            plate = plate.trim(),
                            currentMileage = mileageNumber
                        )
                    )
                }
            },
            enabled = formValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar vehículo")
        }
    }
}