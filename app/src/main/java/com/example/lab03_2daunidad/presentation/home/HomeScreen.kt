package com.example.lab03_2daunidad.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Maintenance
import com.example.lab03_2daunidad.domain.model.MaintenanceStatus
import com.example.lab03_2daunidad.domain.model.Vehicle
import com.example.lab03_2daunidad.domain.util.calculateStatus
import com.example.lab03_2daunidad.domain.util.calculateStatusInfo
import com.example.lab03_2daunidad.domain.util.toMileageText
import com.example.lab03_2daunidad.ui.theme.LAB032DAUNIDADTheme

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
            .background(AutoCareBackground)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "AutoCare",
                    style =
                        MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = AutoCareText
                )

                Text(
                    text =
                        "Controla el mantenimiento de tu vehículo",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color = AutoCareSecondaryText
                )
            }

            Surface(
                shape = CircleShape,
                color = Color(0xFFE5F0F3),
                modifier = Modifier.size(48.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "AC",
                        fontWeight = FontWeight.Bold,
                        color = AutoCareBlue
                    )
                }
            }
        }

        VehicleCard(
            vehicle = vehicle,
            onVehicleClick = onVehicleClick
        )

        Button(
            onClick = onAddMaintenanceClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AutoCareBlue
            )
        ) {

            Text(
                text = "Registrar mantenimiento",
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {

            Text(
                text = "Ver historial",
                fontWeight = FontWeight.SemiBold,
                color = AutoCareBlue
            )
        }

        Text(
            text = "Estado general",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AutoCareText
        )

        GeneralStatusCard(
            overdueCount = overdueCount,
            upcomingCount = upcomingCount
        )

        Text(
            text = "Próximos mantenimientos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AutoCareText
        )

        if (upcomingMaintenances.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text =
                        "No hay próximos mantenimientos registrados.",
                    modifier = Modifier.padding(20.dp),
                    color = AutoCareSecondaryText
                )
            }

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

        Spacer(
            modifier = Modifier.height(12.dp)
        )
    }
}

@Composable
private fun VehicleCard(
    vehicle: Vehicle,
    onVehicleClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFE5F0F3),
                    modifier = Modifier.size(52.dp)
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = "🚗",
                            style =
                                MaterialTheme.typography.titleLarge
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {

                    Text(
                        text =
                            "${vehicle.brand} ${vehicle.model}",
                        style =
                            MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AutoCareText
                    )

                    Text(
                        text =
                            "${vehicle.year} • ${vehicle.plate}",
                        color = AutoCareSecondaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Kilometraje actual",
                color = AutoCareSecondaryText
            )

            Text(
                text =
                    vehicle.currentMileage.toMileageText(),
                style =
                    MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AutoCareText
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(
                onClick = onVehicleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AutoCareBlue
                )
            ) {

                Text(
                    text = "Gestionar vehículo",
                    fontWeight = FontWeight.SemiBold
                )
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
    val textColor: Color

    when {

        overdueCount > 0 -> {

            title = "Atención requerida"

            description =
                if (overdueCount == 1) {
                    "1 mantenimiento vencido"
                } else {
                    "$overdueCount mantenimientos vencidos"
                }

            symbol = "!"

            containerColor = WarningBackground
            textColor = WarningText
        }

        upcomingCount > 0 -> {

            title = "Próximos mantenimientos"

            description =
                if (upcomingCount == 1) {
                    "1 mantenimiento próximo"
                } else {
                    "$upcomingCount mantenimientos próximos"
                }

            symbol = "!"

            containerColor = UpcomingBackground
            textColor = UpcomingText
        }

        else -> {

            title = "Vehículo al día"

            description =
                "No tienes mantenimientos vencidos"

            symbol = "✓"

            containerColor = OkBackground
            textColor = OkText
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = Color.Transparent
            ) {

                Box(
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = symbol,
                        fontWeight =
                            FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
            ) {

                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    text = description,
                    color = textColor
                )
            }
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

    val statusText: String
    val badgeBackground: Color
    val badgeTextColor: Color

    when (status) {

        MaintenanceStatus.OVERDUE -> {

            statusText = "Vencido"
            badgeBackground = WarningBackground
            badgeTextColor = WarningText
        }

        MaintenanceStatus.UPCOMING -> {

            statusText = "Próximo"
            badgeBackground = UpcomingBackground
            badgeTextColor = UpcomingText
        }

        MaintenanceStatus.OK -> {

            statusText = "Al día"
            badgeBackground = OkBackground
            badgeTextColor = OkText
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
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = maintenance.type,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AutoCareText
                )

                Surface(
                    color = badgeBackground,
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Text(
                        text = statusText,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        fontWeight =
                            FontWeight.SemiBold,
                        color = badgeTextColor
                    )
                }
            }

            Text(
                text = statusInfo.message,
                color = AutoCareSecondaryText
            )

            if (
                maintenance.nextDate.isNotBlank()
            ) {

                Text(
                    text =
                        "Próxima fecha: ${maintenance.nextDate}",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color = badgeTextColor
                )
            }

            maintenance.nextMileage?.let {

                Text(
                    text =
                        "Próximo kilometraje: ${it.toMileageText()}",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color = badgeTextColor
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
                ),
                Maintenance(
                    id = 2,
                    type = "Revisión de llantas",
                    nextMileage = 58000,
                    nextDate = "12/09/2026"
                )
            ),
            onVehicleClick = {},
            onAddMaintenanceClick = {},
            onHistoryClick = {}
        )
    }
}