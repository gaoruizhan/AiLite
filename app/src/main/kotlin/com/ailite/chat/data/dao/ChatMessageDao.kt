package com.ailite.chat.data.dao

import androidx.room.*
import com.ailite.chat.data.entity.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE accountId = :accountId ORDER BY timestamp DESC")
    fun getMessagesByAccount(accountId: Long): Flow<List<ChatMessage>>

    @Insert
    suspend fun insert(message: ChatMessage)

    @Delete
    suspend fun delete(message: ChatMessage)

    @Query("DELETE FROM chat_messages WHERE accountId = :accountId")
    suspend fun deleteByAccount(accountId: Long)

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAll()
}
