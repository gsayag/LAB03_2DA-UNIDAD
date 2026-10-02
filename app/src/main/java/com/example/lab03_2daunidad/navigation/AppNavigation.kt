package com.example.lab03_2daunidad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab03_2daunidad.domain.model.Vehicle
import com.example.lab03_2daunidad.presentation.home.HomeScreen
import com.example.lab03_2daunidad.presentation.vehicle.VehicleScreen

private const val HOME_ROUTE = "home"
private const val VEHICLE_ROUTE = "vehicle"

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    var vehicle by remember {

        mutableStateOf(
            Vehicle(
                brand = "Toyota",
                model = "Corolla",
                year = 2020,
                plate = "ABC-123",
                currentMileage = 45240
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {

        composable(HOME_ROUTE) {

            HomeScreen(
                vehicle = vehicle,
                onVehicleClick = {

                    navController.navigate(VEHICLE_ROUTE)
                }
            )
        }

        composable(VEHICLE_ROUTE) {

            VehicleScreen(
                vehicle = vehicle,
                onSave = { updatedVehicle ->

                    vehicle = updatedVehicle

                    navController.popBackStack()
                },
                onBack = {

                    navController.popBackStack()
                }
            )
        }
    }
}