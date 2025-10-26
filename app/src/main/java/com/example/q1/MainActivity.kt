package com.example.q1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.q1.ui.theme.Q1Theme

class MainActivity : ComponentActivity() {

    // Activity-scoped VM instance
    private val vm: LifeTrackerViewModel by viewModels()
    private lateinit var lifecycleLogger: LifecycleLogger


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Manually log onCreate
        vm.log("onCreate")


        // Register the lifecycle observer for subsequent events
        lifecycleLogger = LifecycleLogger(vm::log)
        lifecycle.addObserver(lifecycleLogger)

        setContent {
            // We’ll render the list in the next step using LazyColumn
            LifeTrackerScreen(vm)
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(lifecycleLogger)
        super.onDestroy()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeTrackerScreen(vm: LifeTrackerViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbars by rememberSaveable { mutableStateOf(true) }
    val logs = vm.logs

    LaunchedEffect(logs.firstOrNull()) {
        val newest = logs.firstOrNull() ?: return@LaunchedEffect
        if (showSnackbars) {
            snackbarHostState.showSnackbar("Lifecycle: ${newest.event} @ ${formatTime(newest.timestampMillis)}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LifeTracker") },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Snackbars")
                        Spacer(Modifier.width(8.dp))
                        Switch(checked = showSnackbars, onCheckedChange = { showSnackbars = it })
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            items(logs) { entry ->
                LifeLogRow(entry)
                Divider()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun LifeLogRow(entry: LifeLogEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(eventColor(entry.event))
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(text = entry.event, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = formatTime(entry.timestampMillis),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}