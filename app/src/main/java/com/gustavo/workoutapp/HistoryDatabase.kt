package com.gustavo.workoutapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase:RoomDatabase() {

    abstract fun historydao():HistoryDao

    companion object{
        @Volatile
        private var INSTANCE: HistoryDatabase?=null

        fun getIntance(context: Context):HistoryDatabase{
            synchronized(this){
                var instace = INSTANCE
                if(instace==null){
                    instace = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instace
                }
                return instace
            }
        }

    }

}