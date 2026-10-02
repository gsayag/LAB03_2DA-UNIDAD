package com.example.lab03_2daunidad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab03_2daunidad.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicles LIMIT 1")
    fun observeVehicle(): Flow<VehicleEntity?>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun saveVehicle(
        vehicle: VehicleEntity
    )

    @Query("DELETE FROM vehicles")
    suspend fun deleteVehicle()
}