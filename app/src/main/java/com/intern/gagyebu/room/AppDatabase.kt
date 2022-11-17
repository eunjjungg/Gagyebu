package com.intern.gagyebu.room

import android.content.Context
import androidx.room.*

@Database(
    entities = [ItemEntity::class],
    version = 1,
    exportSchema = true

)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}