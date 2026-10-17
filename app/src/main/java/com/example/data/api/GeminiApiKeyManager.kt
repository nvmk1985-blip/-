package com.example.data.api

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GeminiApiKeyManager {
    private const val PREFS_NAME = "gemini_api_prefs"
    private const val KEY_CUSTOM_API_KEY = "custom_gemini_api_key"

    private val _apiKeyFlow = MutableStateFlow("")
    val apiKeyFlow: StateFlow<String> = _apiKeyFlow.asStateFlow()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun init(context: Context) {
        val prefs = getPrefs(context)
        val savedKey = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
        val effective = if (savedKey.isNotBlank()) {
            savedKey.trim()
        } else {
            getBuildConfigKey()
        }
        _apiKeyFlow.value = effective
    }

    fun getEffectiveApiKey(context: Context? = null): String {
        if (_apiKeyFlow.value.isNotBlank()) {
            return _apiKeyFlow.value
        }
        if (context != null) {
            val prefs = getPrefs(context)
            val savedKey = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
            if (savedKey.isNotBlank()) {
                _apiKeyFlow.value = savedKey.trim()
                return _apiKeyFlow.value
            }
        }
        val buildKey = getBuildConfigKey()
        if (buildKey.isNotBlank()) {
            _apiKeyFlow.value = buildKey
            return buildKey
        }
        return ""
    }

    fun getCustomSavedKey(context: Context): String {
        return getPrefs(context).getString(KEY_CUSTOM_API_KEY, "") ?: ""
    }

    fun saveCustomApiKey(context: Context, key: String) {
        val trimmed = key.trim()
        getPrefs(context).edit().putString(KEY_CUSTOM_API_KEY, trimmed).apply()
        _apiKeyFlow.value = if (trimmed.isNotBlank()) trimmed else getBuildConfigKey()
    }

    fun clearCustomApiKey(context: Context) {
        getPrefs(context).edit().remove(KEY_CUSTOM_API_KEY).apply()
        _apiKeyFlow.value = getBuildConfigKey()
    }

    fun hasValidApiKey(context: Context? = null): Boolean {
        val key = getEffectiveApiKey(context)
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    private fun getBuildConfigKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY.trim()
            if (key == "MY_GEMINI_API_KEY") "" else key
        } catch (_: Exception) {
            ""
        }
    }
}
