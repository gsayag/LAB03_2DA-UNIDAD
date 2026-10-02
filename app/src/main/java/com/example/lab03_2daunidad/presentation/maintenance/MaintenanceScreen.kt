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
import androidx.compose.runtime.remember
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
    maintenance: Maintenance? = null,
    onSave: (Maintenance) -> Unit,
    onBack: () -> Unit
) {

    var type by remember(maintenance?.id) {
        mutableStateOf(maintenance?.type ?: "")
    }

    var date by remember(maintenance?.id) {
        mutableStateOf(maintenance?.date ?: "")
    }

    var mileage by remember(maintenance?.id) {
        mutableStateOf(
            maintenance?.mileage?.toString()
                ?: currentMileage.toString()
        )
    }

    var cost by remember(maintenance?.id) {
        mutableStateOf(
            maintenance?.cost?.toString() ?: ""
        )
    }

    var nextMileage by remember(maintenance?.id) {
        mutableStateOf(
            maintenance?.nextMileage?.toString() ?: ""
        )
    }

    var nextDate by remember(maintenance?.id) {
        mutableStateOf(
            maintenance?.nextDate ?: ""
        )
    }

    var notes by remember(maintenance?.id) {
        mutableStateOf(
            maintenance?.notes ?: ""
        )
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
            text = if (maintenance == null) {
                "Nuevo mantenimiento"
            } else {
                "Editar mantenimiento"
            },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (maintenance == null) {
                "Registra un servicio realizado a tu vehículo."
            } else {
                "Modifica los datos del mantenimiento seleccionado."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = {
                Text("Tipo de mantenimiento")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
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
                mileage = value.filter { it.isDigit() }
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
                cost = value.filter {
                    it.isDigit() || it == '.'
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
                nextMileage = value.filter { it.isDigit() }
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
                            id = maintenance?.id ?: 0L,
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

            Text(
                text = if (maintenance == null) {
                    "Guardar mantenimiento"
                } else {
                    "Guardar cambios"
                }
            )
        }
    }
}