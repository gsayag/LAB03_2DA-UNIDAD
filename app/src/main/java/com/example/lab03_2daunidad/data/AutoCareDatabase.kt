package com.example.lab03_2daunidad.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lab03_2daunidad.data.local.dao.MaintenanceDao
import com.example.lab03_2daunidad.data.local.dao.VehicleDao
import com.example.lab03_2daunidad.data.local.entity.MaintenanceEntity
import com.example.lab03_2daunidad.data.local.entity.VehicleEntity

@Database(
    entities = [
        VehicleEntity::class,
        MaintenanceEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AutoCareDatabase : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao

    abstract fun maintenanceDao(): MaintenanceDao

    companion object {

        @Volatile
        private var INSTANCE: AutoCareDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS maintenance_records (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        vehicleId INTEGER NOT NULL,
                        type TEXT NOT NULL,
                        date TEXT NOT NULL,
                        mileage INTEGER NOT NULL,
                        cost REAL NOT NULL,
                        nextMileage INTEGER,
                        nextDate TEXT NOT NULL,
                        notes TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(
            context: Context
        ): AutoCareDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoCareDatabase::class.java,
                    "autocare_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}