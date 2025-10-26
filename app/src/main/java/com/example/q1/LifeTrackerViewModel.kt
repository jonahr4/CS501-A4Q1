package com.example.q1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class LifeLogEntry(
    val event: String,
    val timestampMillis: Long = System.currentTimeMillis()
)

class LifeTrackerViewModel : ViewModel() {
    // Observable list; Compose will recompose when it changes
    private val _logs = mutableStateListOf<LifeLogEntry>()
    val logs: List<LifeLogEntry> get() = _logs

    fun log(event: String) {
        // Newest first
        _logs.add(0, LifeLogEntry(event))
    }
}
