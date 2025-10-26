package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Q1: LifeTracker – A Lifecycle-Aware Activity Logger
 * - ViewModel keeps logs across config changes (rotation).
 * - LifecycleEventObserver captures transitions in real time.
 * - LazyColumn displays the log; Snackbar toggled via a Switch.
 * - Each entry has timestamp + color code.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Use plain MaterialTheme to avoid custom theme dependency issues
            MaterialTheme {
                val vm: LifeTrackerViewModel = viewModel()

                // Observe lifecycle the Compose way
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        vm.record(event)
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                LifeTrackerScreen(vm)
            }
        }
    }
}

data class LifeEvent(
    val label: String,
    val timestamp: Long,
    val color: Color
)

class LifeTrackerViewModel : ViewModel() {

    private val _logs = mutableStateListOf<LifeEvent>()
    val logs: List<LifeEvent> get() = _logs

    private val _snackbarEnabled = MutableStateFlow(true)
    val snackbarEnabled = _snackbarEnabled.asStateFlow()

    fun setSnackbarEnabled(enabled: Boolean) { _snackbarEnabled.value = enabled }

    fun record(event: Lifecycle.Event) {
        val label = when (event) {
            Lifecycle.Event.ON_CREATE -> "onCreate"
            Lifecycle.Event.ON_START -> "onStart"
            Lifecycle.Event.ON_RESUME -> "onResume"
            Lifecycle.Event.ON_PAUSE -> "onPause"
            Lifecycle.Event.ON_STOP -> "onStop"
            Lifecycle.Event.ON_DESTROY -> "onDestroy"
            Lifecycle.Event.ON_ANY -> "onAny"
        }
        _logs.add(
            LifeEvent(
                label = label,
                timestamp = System.currentTimeMillis(),
                color = when (label) {
                    "onCreate" -> Color(0xFF4CAF50)
                    "onStart" -> Color(0xFF2196F3)
                    "onResume" -> Color(0xFF9C27B0)
                    "onPause" -> Color(0xFFFF9800)
                    "onStop" -> Color(0xFFF44336)
                    "onDestroy" -> Color(0xFF795548)
                    else -> Color.Gray
                }
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeTrackerScreen(vm: LifeTrackerViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val logs = vm.logs
    val snackbarEnabled by vm.snackbarEnabled.collectAsState()

    // Show snackbar on each new event if enabled
    LaunchedEffect(logs.size, snackbarEnabled) {
        if (snackbarEnabled && logs.isNotEmpty()) {
            snackbarHostState.showSnackbar("Lifecycle: ${logs.last().label}")
        }
    }

    Scaffold(
        topBar = {
            // No icons, just a simple TopAppBar to avoid icon deps
            TopAppBar(
                title = { Text("LifeTracker") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Snackbars")
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = snackbarEnabled,
                            onCheckedChange = { vm.setSnackbarEnabled(it) }
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CurrentStateHeader(logs)
            Divider()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(logs) { index, item ->
                    LifeEventRow(index + 1, item)
                }
                item {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Repo: https://github.com/yourname/LifeTracker",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentStateHeader(logs: List<LifeEvent>) {
    val last = logs.lastOrNull()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Current state:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.width(8.dp))
        if (last != null) {
            ColorDot(last.color)
            Spacer(Modifier.width(8.dp))
            Text(text = last.label, style = MaterialTheme.typography.titleMedium)
        } else {
            Text(text = "—", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun LifeEventRow(index: Int, event: LifeEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ColorDot(event.color)
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = "$index. ${event.label}",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatTime(event.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ColorDot(color: Color) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(MaterialTheme.shapes.small)
            .background(color)
    )
}

private fun formatTime(millis: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    return sdf.format(Date(millis))
}