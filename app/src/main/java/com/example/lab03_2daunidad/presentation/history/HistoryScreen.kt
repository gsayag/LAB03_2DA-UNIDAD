package com.example.lab03_2daunidad.presentation.history

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Maintenance
import com.example.lab03_2daunidad.domain.model.MaintenanceStatus
import com.example.lab03_2daunidad.domain.util.calculateStatusInfo
import com.example.lab03_2daunidad.domain.util.toMileageText
import com.example.lab03_2daunidad.domain.util.toSolesText

private val AutoCareBlue = Color(0xFF195D6F)
private val AutoCareBackground = Color(0xFFF7F9FA)
private val AutoCareText = Color(0xFF172F38)
private val AutoCareSecondaryText = Color(0xFF6D7D84)

private val WarningBackground = Color(0xFFFFE9E5)
private val WarningText = Color(0xFFC13B34)

private val UpcomingBackground = Color(0xFFFFF0CF)
private val UpcomingText = Color(0xFF936000)

private val OkBackground = Color(0xFFE4F4EA)
private val OkText = Color(0xFF26734D)

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
            .background(AutoCareBackground)
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text(
                text = "← Volver",
                color = AutoCareBlue,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = "Historial",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = AutoCareText
        )

        Text(
            text = "Mantenimientos realizados a tu vehículo",
            style = MaterialTheme.typography.bodyLarge,
            color = AutoCareSecondaryText,
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 18.dp
            )
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
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
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

            HistoryFilterChip(
                text = "Todos",
                selected =
                    selectedFilter == HistoryFilter.ALL,
                onClick = {
                    selectedFilter =
                        HistoryFilter.ALL
                }
            )

            HistoryFilterChip(
                text = "Vencidos",
                selected =
                    selectedFilter == HistoryFilter.OVERDUE,
                onClick = {
                    selectedFilter =
                        HistoryFilter.OVERDUE
                }
            )

            HistoryFilterChip(
                text = "Próximos",
                selected =
                    selectedFilter == HistoryFilter.UPCOMING,
                onClick = {
                    selectedFilter =
                        HistoryFilter.UPCOMING
                }
            )

            HistoryFilterChip(
                text = "Al día",
                selected =
                    selectedFilter == HistoryFilter.OK,
                onClick = {
                    selectedFilter =
                        HistoryFilter.OK
                }
            )
        }

        Text(
            text =
                if (filteredMaintenances.size == 1) {
                    "1 mantenimiento"
                } else {
                    "${filteredMaintenances.size} mantenimientos"
                },
            style = MaterialTheme.typography.bodyMedium,
            color = AutoCareSecondaryText,
            modifier = Modifier.padding(
                top = 14.dp,
                bottom = 10.dp
            )
        )

        if (filteredMaintenances.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text =
                        "No se encontraron mantenimientos.",
                    modifier = Modifier.padding(20.dp),
                    color = AutoCareSecondaryText
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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

            shape = RoundedCornerShape(28.dp),

            title = {

                Text(
                    text =
                        "¿Eliminar mantenimiento?",
                    color = AutoCareText,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text =
                        "Se eliminará \"${maintenance.type}\" del historial. " +
                                "Esta acción no se puede deshacer.",
                    color = AutoCareSecondaryText
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
                        color = WarningText,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        maintenanceToDelete = null
                    }
                ) {

                    Text(
                        text = "Cancelar",
                        color = AutoCareBlue,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        )
    }
}

@Composable
private fun HistoryFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    FilterChip(
        selected = selected,
        onClick = onClick,

        label = {

            Text(
                text = text,
                fontWeight =
                    if (selected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    }
            )
        },

        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor =
                Color(0xFFE4F0F3),

            selectedLabelColor =
                AutoCareBlue,

            labelColor =
                AutoCareSecondaryText
        )
    )
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

    val statusText: String
    val badgeBackground: Color
    val badgeText: Color

    when (status) {

        MaintenanceStatus.OVERDUE -> {

            statusText = "Vencido"
            badgeBackground = WarningBackground
            badgeText = WarningText
        }

        MaintenanceStatus.UPCOMING -> {

            statusText = "Próximo"
            badgeBackground = UpcomingBackground
            badgeText = UpcomingText
        }

        MaintenanceStatus.OK -> {

            statusText = "Al día"
            badgeBackground = OkBackground
            badgeText = OkText
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text = maintenance.type,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AutoCareText
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = badgeBackground
                ) {

                    Text(
                        text = statusText,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 5.dp
                        ),
                        color = badgeText,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = statusInfo.message,
                color =
                    if (
                        status ==
                        MaintenanceStatus.OVERDUE
                    ) {
                        WarningText
                    } else {
                        AutoCareSecondaryText
                    }
            )

            Text(
                text = "Fecha: ${maintenance.date}",
                color = AutoCareSecondaryText
            )

            Text(
                text =
                    "Kilometraje: ${maintenance.mileage.toMileageText()}",
                color = AutoCareSecondaryText
            )

            Text(
                text =
                    "Costo: ${maintenance.cost.toSolesText()}",
                color = AutoCareSecondaryText
            )

            maintenance.nextMileage?.let {
                    nextMileage ->

                Text(
                    text =
                        "Próximo kilometraje: ${nextMileage.toMileageText()}",
                    fontWeight =
                        FontWeight.SemiBold,
                    color = AutoCareText
                )
            }

            if (
                maintenance.nextDate.isNotBlank()
            ) {

                Text(
                    text =
                        "Próxima fecha: ${maintenance.nextDate}",
                    color = AutoCareSecondaryText
                )
            }

            if (
                maintenance.notes.isNotBlank()
            ) {

                Text(
                    text = maintenance.notes,
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = AutoCareSecondaryText
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

                    Text(
                        text = "Editar",
                        color = AutoCareBlue
                    )
                }

                TextButton(
                    onClick = onDelete
                ) {

                    Text(
                        text = "Eliminar",
                        color = WarningText
                    )
                }
            }
        }
    }
}