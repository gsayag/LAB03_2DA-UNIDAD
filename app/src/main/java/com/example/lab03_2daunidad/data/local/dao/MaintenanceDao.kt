package com.example.lab03_2daunidad.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lab03_2daunidad.data.local.entity.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {

    @Query(
        "SELECT * FROM maintenance_records " +
                "WHERE vehicleId = :vehicleId " +
                "ORDER BY id DESC"
    )
    fun observeMaintenances(
        vehicleId: Long
    ): Flow<List<MaintenanceEntity>>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertMaintenance(
        maintenance: MaintenanceEntity
    ): Long

    @Update
    suspend fun updateMaintenance(
        maintenance: MaintenanceEntity
    )

    @Delete
    suspend fun deleteMaintenance(
        maintenance: MaintenanceEntity
    )
}