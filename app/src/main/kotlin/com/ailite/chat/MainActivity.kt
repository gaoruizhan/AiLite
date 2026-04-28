package com.ailite.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.ailite.chat.data.AppDatabase
import com.ailite.chat.data.entity.Account
import com.ailite.chat.data.entity.ChatMessage
import com.ailite.chat.api.ApiService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)

        setContent {
            ChatScreen(database, apiService, lifecycleScope)
        }
    }
}

@Composable
fun ChatScreen(
    database: AppDatabase,
    apiService: ApiService,
    lifecycleScope: androidx.lifecycle.LifecycleScope
) {
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }
    var showAccountDialog by remember { mutableStateOf(false) }
    var newAccountProvider by remember { mutableStateOf("openai") }
    var newAccountApiKey by remember { mutableStateOf("") }
    var newAccountModel by remember { mutableStateOf("gpt-3.5-turbo") }
    var newAccountName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        database.accountDao().getAllAccounts().collect { accountList ->
            accounts = accountList
            selectedAccount = accountList.firstOrNull { it.isActive } ?: accountList.firstOrNull()
        }
        database.chatMessageDao().getAllMessages().collect { messageList ->
            messages = messageList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AiLite",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = { showAccountDialog = true }) {
                Text("+ Account")
            }
        }

        // Account selector
        if (accounts.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                accounts.forEach { account ->
                    FilterChip(
                        selected = selectedAccount?.id == account.id,
                        onClick = {
                            selectedAccount = account
                            lifecycleScope.launch {
                                database.accountDao().deactivateAll()
                                database.accountDao().update(account.copy(isActive = true))
                            }
                        },
                        label = { Text(account.name) }
                    )
                }
            }
        }

        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        // Input area
        if (selectedAccount != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type message...") },
                    enabled = !isLoading
                )
                Button(
                    onClick = {
                        if (inputText.isNotBlank() && selectedAccount != null) {
                            lifecycleScope.launch {
                                isLoading = true
                                try {
                                    database.chatMessageDao().insert(
                                        ChatMessage(
                                            accountId = selectedAccount!!.id,
                                            role = "user",
                                            content = inputText,
                                            model = selectedAccount!!.model
                                        )
                                    )
                                    val response = apiService.chat(selectedAccount!!, inputText)
                                    database.chatMessageDao().insert(
                                        ChatMessage(
                                            accountId = selectedAccount!!.id,
                                            role = "assistant",
                                            content = response,
                                            model = selectedAccount!!.model
                                        )
                                    )
                                    inputText = ""
                                } catch (e: Exception) {
                                    database.chatMessageDao().insert(
                                        ChatMessage(
                                            accountId = selectedAccount!!.id,
                                            role = "assistant",
                                            content = "Error: ${e.message}",
                                            model = selectedAccount!!.model
                                        )
                                    )
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    enabled = !isLoading && inputText.isNotBlank()
                ) {
                    Text(if (isLoading) "Loading..." else "Send")
                }
            }
        } else {
            Text(
                text = "Please add an account first",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    // Account dialog
    if (showAccountDialog) {
        AlertDialog(
            onDismissRequest = { showAccountDialog = false },
            title = { Text("Add Account") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = newAccountName,
                        onValueChange = { newAccountName = it },
                        label = { Text("Account Name") }
                    )
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = newAccountProvider,
                            onValueChange = { newAccountProvider = it },
                            label = { Text("Provider") },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("openai", "gemini", "grok").forEach { provider ->
                                DropdownMenuItem(
                                    text = { Text(provider) },
                                    onClick = {
                                        newAccountProvider = provider
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    TextField(
                        value = newAccountApiKey,
                        onValueChange = { newAccountApiKey = it },
                        label = { Text("API Key") }
                    )
                    TextField(
                        value = newAccountModel,
                        onValueChange = { newAccountModel = it },
                        label = { Text("Model") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAccountName.isNotBlank() && newAccountApiKey.isNotBlank()) {
                            lifecycleScope.launch {
                                database.accountDao().insert(
                                    Account(
                                        provider = newAccountProvider,
                                        apiKey = newAccountApiKey,
                                        model = newAccountModel,
                                        name = newAccountName
                                    )
                                )
                                newAccountName = ""
                                newAccountApiKey = ""
                                newAccountModel = "gpt-3.5-turbo"
                                newAccountProvider = "openai"
                                showAccountDialog = false
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.role == "user") Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 300.dp),
            color = if (message.role == "user") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = if (message.role == "user") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
