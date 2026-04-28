package com.ailite.chat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val provider: String, // "openai", "gemini", "grok"
    val apiKey: String,
    val model: String,
    val name: String,
    val isActive: Boolean = false
)
