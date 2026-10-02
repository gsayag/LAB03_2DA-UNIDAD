package com.example.lab03_2daunidad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab03_2daunidad.data.local.dao.VehicleDao
import com.example.lab03_2daunidad.data.local.entity.VehicleEntity

@Database(
    entities = [
        VehicleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AutoCareDatabase : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao

    companion object {

        @Volatile
        private var INSTANCE: AutoCareDatabase? = null

        fun getDatabase(
            context: Context
        ): AutoCareDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoCareDatabase::class.java,
                    "autocare_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}