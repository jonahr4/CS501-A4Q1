package com.example.q1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(millis))
}

fun eventColor(event: String): Color = when (event) {
    "onCreate"  -> Color(0xFF42A5F5) // blue
    "onStart"   -> Color(0xFF66BB6A) // green
    "onResume"  -> Color(0xFF388E3C) // darker green
    "onPause"   -> Color(0xFFFFEE58) // yellow
    "onStop"    -> Color(0xFFFFA726) // orange
    "onDestroy" -> Color(0xFFE57373) // red
    else        -> Color(0xFF90A4AE) // gray-ish default
}