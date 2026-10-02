package com.example.lab03_2daunidad.presentation.maintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.lab03_2daunidad.domain.model.Maintenance

@Composable
fun MaintenanceScreen(
    vehicleId: Long,
    currentMileage: Int,
    onSave: (Maintenance) -> Unit,
    onBack: () -> Unit
) {

    var type by rememberSaveable {
        mutableStateOf("")
    }

    var date by rememberSaveable {
        mutableStateOf("")
    }

    var mileage by rememberSaveable {
        mutableStateOf(currentMileage.toString())
    }

    var cost by rememberSaveable {
        mutableStateOf("")
    }

    var nextMileage by rememberSaveable {
        mutableStateOf("")
    }

    var nextDate by rememberSaveable {
        mutableStateOf("")
    }

    var notes by rememberSaveable {
        mutableStateOf("")
    }

    val mileageNumber = mileage.toIntOrNull()
    val costNumber = cost.toDoubleOrNull()

    val formValid =
        type.isNotBlank() &&
                date.isNotBlank() &&
                mileageNumber != null &&
                costNumber != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("Volver")
        }

        Text(
            text = "Nuevo mantenimiento",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Registra un servicio realizado a tu vehículo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = type,
            onValueChange = {
                type = it
            },
            label = {
                Text("Tipo de mantenimiento")
            },
            placeholder = {
                Text("Ej. Cambio de aceite")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
            },
            label = {
                Text("Fecha")
            },
            placeholder = {
                Text("DD/MM/AAAA")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = mileage,
            onValueChange = { value ->
                mileage = value.filter { char ->
                    char.isDigit()
                }
            },
            label = {
                Text("Kilometraje del servicio")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = cost,
            onValueChange = { value ->

                cost = value.filter { char ->
                    char.isDigit() || char == '.'
                }
            },
            label = {
                Text("Costo")
            },
            prefix = {
                Text("S/ ")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(
            text = "Próximo mantenimiento",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = nextMileage,
            onValueChange = { value ->

                nextMileage = value.filter { char ->
                    char.isDigit()
                }
            },
            label = {
                Text("Próximo kilometraje")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = nextDate,
            onValueChange = {
                nextDate = it
            },
            label = {
                Text("Próxima fecha")
            },
            placeholder = {
                Text("DD/MM/AAAA")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = notes,
            onValueChange = {
                notes = it
            },
            label = {
                Text("Observaciones")
            },
            placeholder = {
                Text("Detalles adicionales del servicio")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {

                if (
                    mileageNumber != null &&
                    costNumber != null
                ) {

                    onSave(
                        Maintenance(
                            vehicleId = vehicleId,
                            type = type.trim(),
                            date = date.trim(),
                            mileage = mileageNumber,
                            cost = costNumber,
                            nextMileage = nextMileage.toIntOrNull(),
                            nextDate = nextDate.trim(),
                            notes = notes.trim()
                        )
                    )
                }
            },
            enabled = formValid,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Guardar mantenimiento")
        }
    }
}