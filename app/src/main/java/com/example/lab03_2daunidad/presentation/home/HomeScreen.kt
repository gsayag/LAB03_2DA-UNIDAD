package com.example.lab03_2daunidad.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab03_2daunidad.domain.model.Vehicle
import com.example.lab03_2daunidad.ui.theme.LAB032DAUNIDADTheme

@Composable
fun HomeScreen(
    vehicle: Vehicle,
    onVehicleClick: () -> Unit
) {

    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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

            Text(
                text = "Estado general",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            StatusCard()

            Text(
                text = "Próximos mantenimientos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            MaintenanceCard(
                title = "Cambio de aceite",
                description = "Faltan 1,760 km"
            )

            MaintenanceCard(
                title = "Revisión de frenos",
                description = "Próxima revisión: 15/11/2026"
            )
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
                text = "${vehicle.brand} ${vehicle.model}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${vehicle.year} • ${vehicle.plate}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Kilometraje actual",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${vehicle.currentMileage} km",
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

                Text(
                    text = "Gestionar vehículo"
                )
            }
        }
    }
}

@Composable
private fun StatusCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "Vehículo al día",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "No tienes mantenimientos vencidos",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "✓",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun MaintenanceCard(
    title: String,
    description: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
                currentMileage = 45240
            ),
            onVehicleClick = {}
        )
    }
}