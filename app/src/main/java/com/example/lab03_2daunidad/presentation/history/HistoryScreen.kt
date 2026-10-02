package com.example.lab03_2daunidad.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Maintenance

@Composable
fun HistoryScreen(
    maintenances: List<Maintenance>,
    onDelete: (Maintenance) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("Volver")
        }

        Text(
            text = "Historial",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Mantenimientos realizados a tu vehículo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (maintenances.isEmpty()) {

            Text(
                text = "Todavía no tienes mantenimientos registrados.",
                modifier = Modifier.padding(top = 32.dp),
                style = MaterialTheme.typography.bodyLarge
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = maintenances,
                    key = { maintenance ->
                        maintenance.id
                    }
                ) { maintenance ->

                    MaintenanceHistoryCard(
                        maintenance = maintenance,
                        onDelete = {
                            onDelete(maintenance)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MaintenanceHistoryCard(
    maintenance: Maintenance,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = maintenance.type,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Fecha: ${maintenance.date}"
            )

            Text(
                text = "Kilometraje: ${maintenance.mileage} km"
            )

            Text(
                text = "Costo: S/ %.2f".format(maintenance.cost)
            )

            if (maintenance.nextMileage != null) {
                Text(
                    text = "Próximo: ${maintenance.nextMileage} km"
                )
            }

            if (maintenance.nextDate.isNotBlank()) {
                Text(
                    text = "Próxima fecha: ${maintenance.nextDate}"
                )
            }

            if (maintenance.notes.isNotBlank()) {
                Text(
                    text = maintenance.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onDelete
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}