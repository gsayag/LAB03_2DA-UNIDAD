package com.example.lab03_2daunidad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab03_2daunidad.data.local.AutoCareDatabase
import com.example.lab03_2daunidad.data.mapper.toDomain
import com.example.lab03_2daunidad.data.mapper.toEntity
import com.example.lab03_2daunidad.domain.model.Vehicle
import com.example.lab03_2daunidad.presentation.home.HomeScreen
import com.example.lab03_2daunidad.presentation.vehicle.VehicleScreen
import kotlinx.coroutines.launch

private const val HOME_ROUTE = "home"
private const val VEHICLE_ROUTE = "vehicle"

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current

    val database = remember(context) {
        AutoCareDatabase.getDatabase(context)
    }

    val vehicleDao = remember(database) {
        database.vehicleDao()
    }

    val coroutineScope = rememberCoroutineScope()

    val vehicleEntity by vehicleDao
        .observeVehicle()
        .collectAsState(initial = null)

    val vehicle = vehicleEntity?.toDomain()
        ?: Vehicle(
            id = 1L,
            brand = "Toyota",
            model = "Corolla",
            year = 2020,
            plate = "ABC-123",
            currentMileage = 45240
        )

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {

        composable(HOME_ROUTE) {

            HomeScreen(
                vehicle = vehicle,
                onVehicleClick = {

                    navController.navigate(
                        VEHICLE_ROUTE
                    )
                }
            )
        }

        composable(VEHICLE_ROUTE) {

            VehicleScreen(
                vehicle = vehicle,

                onSave = { updatedVehicle ->

                    coroutineScope.launch {

                        vehicleDao.saveVehicle(
                            updatedVehicle.toEntity()
                        )

                        navController.popBackStack()
                    }
                },

                onBack = {

                    navController.popBackStack()
                }
            )
        }
    }
}