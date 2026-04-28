package com.ailite.chat.api

import com.ailite.chat.data.entity.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import com.google.gson.JsonObject

class ApiService {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun chat(account: Account, message: String): String = withContext(Dispatchers.IO) {
        return@withContext when (account.provider) {
            "openai" -> callOpenAI(account, message)
            "gemini" -> callGemini(account, message)
            "grok" -> callGrok(account, message)
            else -> throw IllegalArgumentException("Unknown provider: ${account.provider}")
        }
    }

    private fun callOpenAI(account: Account, message: String): String {
        val json = """
        {
            "model": "${account.model}",
            "messages": [{"role": "user", "content": "$message"}],
            "max_tokens": 2000
        }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${account.apiKey}")
            .addHeader("Content-Type", "application/json")
            .post(json.toRequestBody())
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: return "Error: Empty response"
        val json = gson.fromJson(body, JsonObject::class.java)
        return json.getAsJsonArray("choices").get(0).asJsonObject
            .get("message").asJsonObject.get("content").asString
    }

    private fun callGemini(account: Account, message: String): String {
        val json = """
        {
            "contents": [{
                "parts": [{"text": "$message"}]
            }]
        }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/${account.model}:generateContent?key=${account.apiKey}")
            .addHeader("Content-Type", "application/json")
            .post(json.toRequestBody())
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: return "Error: Empty response"
        val json = gson.fromJson(body, JsonObject::class.java)
        return json.getAsJsonArray("candidates").get(0).asJsonObject
            .get("content").asJsonObject.getAsJsonArray("parts").get(0).asJsonObject
            .get("text").asString
    }

    private fun callGrok(account: Account, message: String): String {
        val json = """
        {
            "model": "${account.model}",
            "messages": [{"role": "user", "content": "$message"}],
            "stream": false
        }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://api.x.ai/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${account.apiKey}")
            .addHeader("Content-Type", "application/json")
            .post(json.toRequestBody())
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: return "Error: Empty response"
        val json = gson.fromJson(body, JsonObject::class.java)
        return json.getAsJsonArray("choices").get(0).asJsonObject
            .get("message").asJsonObject.get("content").asString
    }
}
