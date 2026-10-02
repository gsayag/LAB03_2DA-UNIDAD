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
import com.example.lab03_2daunidad.presentation.history.HistoryScreen
import com.example.lab03_2daunidad.presentation.home.HomeScreen
import com.example.lab03_2daunidad.presentation.maintenance.MaintenanceScreen
import com.example.lab03_2daunidad.presentation.vehicle.VehicleScreen
import kotlinx.coroutines.launch

private const val HOME_ROUTE = "home"
private const val VEHICLE_ROUTE = "vehicle"
private const val MAINTENANCE_ROUTE = "maintenance"
private const val HISTORY_ROUTE = "history"
private const val EDIT_MAINTENANCE_ROUTE = "editMaintenance"

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

    val maintenanceDao = remember(database) {
        database.maintenanceDao()
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

    val maintenanceEntities by maintenanceDao
        .observeMaintenances(vehicle.id)
        .collectAsState(initial = emptyList())

    val maintenances = maintenanceEntities.map {
        it.toDomain()
    }

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {

        composable(HOME_ROUTE) {

            HomeScreen(
                vehicle = vehicle,
                maintenances = maintenances,

                onVehicleClick = {
                    navController.navigate(VEHICLE_ROUTE)
                },

                onAddMaintenanceClick = {
                    navController.navigate(MAINTENANCE_ROUTE)
                },

                onHistoryClick = {
                    navController.navigate(HISTORY_ROUTE)
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

        composable(MAINTENANCE_ROUTE) {

            MaintenanceScreen(
                vehicleId = vehicle.id,
                currentMileage = vehicle.currentMileage,

                onSave = { maintenance ->

                    coroutineScope.launch {

                        maintenanceDao.insertMaintenance(
                            maintenance.toEntity()
                        )

                        navController.popBackStack()
                    }
                },

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(HISTORY_ROUTE) {

            HistoryScreen(
                maintenances = maintenances,
                currentMileage = vehicle.currentMileage,

                onEdit = { maintenance ->

                    navController.navigate(
                        "$EDIT_MAINTENANCE_ROUTE/${maintenance.id}"
                    )
                },

                onDelete = { maintenance ->

                    coroutineScope.launch {

                        maintenanceDao.deleteMaintenance(
                            maintenance.toEntity()
                        )
                    }
                },

                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            "$EDIT_MAINTENANCE_ROUTE/{maintenanceId}"
        ) { backStackEntry ->

            val maintenanceId =
                backStackEntry.arguments
                    ?.getString("maintenanceId")
                    ?.toLongOrNull()

            val maintenance =
                maintenances.firstOrNull {
                    it.id == maintenanceId
                }

            if (maintenance != null) {

                MaintenanceScreen(
                    vehicleId = vehicle.id,
                    currentMileage = vehicle.currentMileage,
                    maintenance = maintenance,

                    onSave = { updatedMaintenance ->

                        coroutineScope.launch {

                            maintenanceDao.updateMaintenance(
                                updatedMaintenance.toEntity()
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
}