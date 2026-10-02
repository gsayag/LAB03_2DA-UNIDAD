package com.example.lab03_2daunidad.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Maintenance
import com.example.lab03_2daunidad.domain.model.MaintenanceStatus
import com.example.lab03_2daunidad.domain.model.Vehicle
import com.example.lab03_2daunidad.domain.util.calculateStatus
import com.example.lab03_2daunidad.ui.theme.LAB032DAUNIDADTheme
import com.example.lab03_2daunidad.domain.util.calculateStatusInfo

@Composable
fun HomeScreen(
    vehicle: Vehicle,
    maintenances: List<Maintenance>,
    onVehicleClick: () -> Unit,
    onAddMaintenanceClick: () -> Unit,
    onHistoryClick: () -> Unit
) {

    val maintenancesWithStatus =
        maintenances.map { maintenance ->

            maintenance to maintenance.calculateStatus(
                currentMileage = vehicle.currentMileage
            )
        }

    val overdueCount =
        maintenancesWithStatus.count {
            it.second == MaintenanceStatus.OVERDUE
        }

    val upcomingCount =
        maintenancesWithStatus.count {
            it.second == MaintenanceStatus.UPCOMING
        }

    val upcomingMaintenances =
        maintenancesWithStatus
            .filter {
                it.first.nextMileage != null ||
                        it.first.nextDate.isNotBlank()
            }
            .sortedBy { pair ->

                when (pair.second) {
                    MaintenanceStatus.OVERDUE -> 0
                    MaintenanceStatus.UPCOMING -> 1
                    MaintenanceStatus.OK -> 2
                }
            }
            .take(3)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "AutoCare",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Controla el mantenimiento de tu vehículo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VehicleCard(
            vehicle = vehicle,
            onVehicleClick = onVehicleClick
        )

        Button(
            onClick = onAddMaintenanceClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Registrar mantenimiento")
        }

        Button(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver historial")
        }

        Text(
            text = "Estado general",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        GeneralStatusCard(
            overdueCount = overdueCount,
            upcomingCount = upcomingCount
        )

        Text(
            text = "Próximos mantenimientos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (upcomingMaintenances.isEmpty()) {

            Text(
                text = "No hay próximos mantenimientos registrados.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        } else {

            upcomingMaintenances.forEach {
                    (maintenance, status) ->

                MaintenanceCard(
                    maintenance = maintenance,
                    status = status,
                    currentMileage =
                        vehicle.currentMileage
                )
            }
        }
    }
}

@Composable
private fun VehicleCard(
    vehicle: Vehicle,
    onVehicleClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Mi vehículo",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "${vehicle.brand} ${vehicle.model}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    "${vehicle.year} • ${vehicle.plate}"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Kilometraje actual"
            )

            Text(
                text =
                    "${vehicle.currentMileage} km",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onVehicleClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestionar vehículo")
            }
        }
    }
}

@Composable
private fun GeneralStatusCard(
    overdueCount: Int,
    upcomingCount: Int
) {

    val title: String
    val description: String
    val symbol: String
    val containerColor: Color

    when {

        overdueCount > 0 -> {

            title = "Atención requerida"

            description =
                "$overdueCount mantenimiento(s) vencido(s)"

            symbol = "!"

            containerColor =
                MaterialTheme.colorScheme.errorContainer
        }

        upcomingCount > 0 -> {

            title = "Próximos mantenimientos"

            description =
                "$upcomingCount mantenimiento(s) próximo(s)"

            symbol = "!"

            containerColor =
                MaterialTheme.colorScheme.tertiaryContainer
        }

        else -> {

            title = "Vehículo al día"

            description =
                "No tienes mantenimientos vencidos"

            symbol = "✓"

            containerColor =
                MaterialTheme.colorScheme.secondaryContainer
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description
                )
            }

            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun MaintenanceCard(
    maintenance: Maintenance,
    status: MaintenanceStatus,
    currentMileage: Int
) {

    val statusInfo =
        maintenance.calculateStatusInfo(
            currentMileage = currentMileage
        )

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
                Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = maintenance.type,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = statusText,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = statusInfo.message,
                style = MaterialTheme.typography.bodyMedium
            )

            if (
                maintenance.nextDate.isNotBlank()
            ) {

                Text(
                    text =
                        "Próxima fecha: ${maintenance.nextDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            maintenance.nextMileage?.let {

                Text(
                    text = "Próximo kilometraje: $it km",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {

    LAB032DAUNIDADTheme {

        HomeScreen(
            vehicle = Vehicle(
                brand = "Toyota",
                model = "Corolla",
                year = 2020,
                plate = "ABC-123",
                currentMileage = 56000
            ),
            maintenances = listOf(
                Maintenance(
                    id = 1,
                    type = "Cambio de aceite",
                    nextMileage = 57000,
                    nextDate = "15/10/2026"
                )
            ),
            onVehicleClick = {},
            onAddMaintenanceClick = {},
            onHistoryClick = {}
        )
    }
}