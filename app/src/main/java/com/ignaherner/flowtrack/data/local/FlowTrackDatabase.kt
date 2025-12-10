package com.ignaherner.flowtrack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TransactionEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FlowTrackDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object{
        @Volatile
        private var INSTANCE: FlowTrackDatabase? = null

        fun getInstance(context: Context): FlowTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlowTrackDatabase::class.java,
                    "flowtrack_db"
                )

                // En produccion: manejar migraciones
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}