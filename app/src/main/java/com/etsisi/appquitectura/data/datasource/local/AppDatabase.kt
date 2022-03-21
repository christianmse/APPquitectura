package com.etsisi.appquitectura.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.etsisi.appquitectura.application.BaseApplication
import com.etsisi.appquitectura.data.datasource.local.dao.QuestionsDAO
import com.etsisi.appquitectura.data.model.entities.QuestionEntity
import com.etsisi.appquitectura.utils.Constants.DATABASE_NAME

@Database(entities = [QuestionEntity::class], version = BaseApplication.appDatabaseVersion, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun questionsDao(): QuestionsDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    }
                ).build()
        }
    }
}