# AiLite - Simple AI Chat Aggregator

A lightweight Android application that aggregates multiple AI chat APIs (OpenAI, Google Gemini, Grok) into a single, easy-to-use interface.

## ✨ Features

- **Multi-API Support**
  - OpenAI (GPT-3.5, GPT-4, etc.)
  - Google Gemini
  - Grok (X.AI)

- **Multi-Account Management**
  - Add multiple accounts for different APIs
  - Switch between accounts seamlessly
  - Store API keys securely locally

- **Chat History**
  - Local SQLite database
  - View conversation history
  - Clear history when needed

- **Simple & Minimal UI**
  - Built with Jetpack Compose
  - Material Design 3
  - Easy to use

## 🚀 Quick Start

### Installation

1. Go to [Releases](https://github.com/gaoruizhan/AiLite/releases)
2. Download the latest `app-debug.apk`
3. Enable "Unknown Sources" in your Android device settings
4. Install the APK

### First Use

1. Open AiLite
2. Click "+ Account" to add your first API account
3. Fill in:
   - **Account Name**: Give it a name (e.g., "My OpenAI")
   - **Provider**: Select OpenAI, Gemini, or Grok
   - **API Key**: Paste your API key from the provider
   - **Model**: Enter the model name (e.g., `gpt-3.5-turbo` for OpenAI)
4. Click "Save"
5. Select the account and start chatting!

## 📋 API Key Setup

### OpenAI
1. Go to https://platform.openai.com/api-keys
2. Create a new API key
3. Copy and paste into AiLite
4. Model examples: `gpt-3.5-turbo`, `gpt-4`

### Google Gemini
1. Go to https://makersuite.google.com/app/apikey
2. Create a new API key
3. Copy and paste into AiLite
4. Model: `gemini-pro`

### Grok (X.AI)
1. Go to https://console.x.ai/
2. Create a new API key
3. Copy and paste into AiLite
4. Model: `grok-beta`

## 🏗️ Project Structure

```
app/src/main/
├── kotlin/com/ailite/chat/
│   ├── MainActivity.kt          # Main UI (Compose)
│   ├── api/
│   │   └── ApiService.kt        # API integration for all providers
│   └── data/
│       ├── AppDatabase.kt       # Room database setup
│       ├── dao/
│       │   ├── AccountDao.kt    # Account database operations
│       │   └── ChatMessageDao.kt # Message database operations
│       └── entity/
│           ├── Account.kt       # Account data model
│           └── ChatMessage.kt   # Message data model
└── res/
    ├── values/strings.xml       # UI strings
    ├── values/colors.xml        # Color definitions
    └── values/themes.xml        # Theme definitions
```

## 🔧 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material Design 3
- **Database**: Room (SQLite)
- **Networking**: OkHttp + Gson
- **Async**: Coroutines
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)

## 🔄 Auto-Build Process

This project uses **GitHub Actions** for automatic compilation:

1. Every time you push code to the `main` branch, GitHub automatically:
   - Compiles the Android project
   - Generates the APK file
   - Creates a release with the APK
   - Archives the APK for 30 days

2. Download the latest APK from [Releases](https://github.com/gaoruizhan/AiLite/releases)

## 📝 License

MIT License - Feel free to use and modify!

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests

## ⚠️ Disclaimer

- Keep your API keys private and secure
- Monitor your API usage to avoid unexpected charges
- This app stores API keys locally on your device
- Be responsible with third-party API usage

## 📧 Support

For issues or questions, please open an issue on GitHub.

---

**Made with ❤️ by gaoruizhan**
