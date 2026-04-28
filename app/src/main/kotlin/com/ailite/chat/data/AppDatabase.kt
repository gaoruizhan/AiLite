package com.ailite.chat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ailite.chat.data.dao.AccountDao
import com.ailite.chat.data.dao.ChatMessageDao
import com.ailite.chat.data.entity.Account
import com.ailite.chat.data.entity.ChatMessage

@Database(
    entities = [ChatMessage::class, Account::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun accountDao(): AccountDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ailite_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
