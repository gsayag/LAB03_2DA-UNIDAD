package com.example.lab03_2daunidad.presentation.history

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Maintenance
import com.example.lab03_2daunidad.domain.model.MaintenanceStatus
import com.example.lab03_2daunidad.domain.util.calculateStatusInfo
import com.example.lab03_2daunidad.domain.util.toMileageText
import com.example.lab03_2daunidad.domain.util.toSolesText

private enum class HistoryFilter {
    ALL,
    OVERDUE,
    UPCOMING,
    OK
}

@Composable
fun HistoryScreen(
    maintenances: List<Maintenance>,
    currentMileage: Int,
    onEdit: (Maintenance) -> Unit,
    onDelete: (Maintenance) -> Unit,
    onBack: () -> Unit
) {

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var selectedFilter by rememberSaveable {
        mutableStateOf(HistoryFilter.ALL)
    }

    var maintenanceToDelete by remember {
        mutableStateOf<Maintenance?>(null)
    }

    val filteredMaintenances =
        maintenances.filter { maintenance ->

            val matchesSearch =
                maintenance.type.contains(
                    searchQuery,
                    ignoreCase = true
                ) ||
                        maintenance.notes.contains(
                            searchQuery,
                            ignoreCase = true
                        )

            val status =
                maintenance.calculateStatusInfo(
                    currentMileage = currentMileage
                ).status

            val matchesFilter =
                when (selectedFilter) {

                    HistoryFilter.ALL ->
                        true

                    HistoryFilter.OVERDUE ->
                        status == MaintenanceStatus.OVERDUE

                    HistoryFilter.UPCOMING ->
                        status == MaintenanceStatus.UPCOMING

                    HistoryFilter.OK ->
                        status == MaintenanceStatus.OK
                }

            matchesSearch && matchesFilter
        }

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

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            label = {
                Text("Buscar mantenimiento")
            },
            placeholder = {
                Text("Ej. aceite, frenos, llantas")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .horizontalScroll(
                    rememberScrollState()
                ),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(
                selected =
                    selectedFilter == HistoryFilter.ALL,
                onClick = {
                    selectedFilter =
                        HistoryFilter.ALL
                },
                label = {
                    Text("Todos")
                }
            )

            FilterChip(
                selected =
                    selectedFilter == HistoryFilter.OVERDUE,
                onClick = {
                    selectedFilter =
                        HistoryFilter.OVERDUE
                },
                label = {
                    Text("Vencidos")
                }
            )

            FilterChip(
                selected =
                    selectedFilter == HistoryFilter.UPCOMING,
                onClick = {
                    selectedFilter =
                        HistoryFilter.UPCOMING
                },
                label = {
                    Text("Próximos")
                }
            )

            FilterChip(
                selected =
                    selectedFilter == HistoryFilter.OK,
                onClick = {
                    selectedFilter =
                        HistoryFilter.OK
                },
                label = {
                    Text("Al día")
                }
            )
        }

        if (filteredMaintenances.isEmpty()) {

            Text(
                text = "No se encontraron mantenimientos.",
                modifier = Modifier.padding(top = 32.dp),
                style = MaterialTheme.typography.bodyLarge
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = filteredMaintenances,
                    key = {
                        it.id
                    }
                ) { maintenance ->

                    MaintenanceHistoryCard(
                        maintenance = maintenance,
                        currentMileage = currentMileage,

                        onEdit = {
                            onEdit(maintenance)
                        },

                        onDelete = {
                            maintenanceToDelete =
                                maintenance
                        }
                    )
                }
            }
        }
    }

    maintenanceToDelete?.let { maintenance ->

        AlertDialog(
            onDismissRequest = {
                maintenanceToDelete = null
            },

            title = {
                Text(
                    text = "¿Eliminar mantenimiento?"
                )
            },

            text = {
                Text(
                    text = "Se eliminará \"${maintenance.type}\" del historial. Esta acción no se puede deshacer."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        onDelete(maintenance)

                        maintenanceToDelete = null
                    }
                ) {

                    Text(
                        text = "Eliminar",
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        maintenanceToDelete = null
                    }
                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun MaintenanceHistoryCard(
    maintenance: Maintenance,
    currentMileage: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val statusInfo =
        maintenance.calculateStatusInfo(
            currentMileage = currentMileage
        )

    val status = statusInfo.status

    val statusText =
        when (status) {

            MaintenanceStatus.OVERDUE ->
                "🔴 Vencido"

            MaintenanceStatus.UPCOMING ->
                "🟡 Próximo"

            MaintenanceStatus.OK ->
                "🟢 Al día"
        }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = maintenance.type,
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = statusText,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = statusInfo.message,
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text =
                    "Fecha: ${maintenance.date}"
            )

            Text(
                text = "Kilometraje: ${maintenance.mileage.toMileageText()}"
            )

            Text(
                text = "Costo: ${maintenance.cost.toSolesText()}"
            )

            maintenance.nextMileage?.let { nextMileage ->

                Text(
                    text = "Próximo kilometraje: ${nextMileage.toMileageText()}"
                )
            }

            if (maintenance.nextDate.isNotBlank()) {

                Text(
                    text =
                        "Próxima fecha: ${maintenance.nextDate}"
                )
            }

            if (maintenance.notes.isNotBlank()) {

                Text(
                    text = maintenance.notes,
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.End
            ) {

                TextButton(
                    onClick = onEdit
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = onDelete
                ) {

                    Text(
                        text = "Eliminar",
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}