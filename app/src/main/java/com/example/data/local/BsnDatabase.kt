package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsLinkEntity::class], version = 1, exportSchema = false)
abstract class BsnDatabase : RoomDatabase() {
  abstract fun newsLinkDao(): NewsLinkDao

  companion object {
    @Volatile private var INSTANCE: BsnDatabase? = null

    fun getInstance(context: Context): BsnDatabase {
      return INSTANCE
        ?: synchronized(this) {
          INSTANCE
            ?: Room.databaseBuilder(
                context.applicationContext,
                BsnDatabase::class.java,
                "bsn_news_24_db",
              )
              .fallbackToDestructiveMigration(dropAllTables = true)
              .build()
              .also { INSTANCE = it }
        }
    }
  }
}
