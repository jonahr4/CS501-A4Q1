# Android Lifecycle Logger

This is a simple Android application built with Jetpack Compose that demonstrates and logs the Android Activity lifecycle events. It's a useful tool for developers looking to understand the sequence of lifecycle callbacks and how they behave during various user interactions and device configuration changes.

## Project Overview

The application consists of a single screen that displays a log of the activity's lifecycle events. This helps in visualizing the states an activity goes through, from creation to destruction. [7, 9]

The project follows a basic MVVM (Model-View-ViewModel) architecture. [1, 11]

### Key Components:

*   **`MainActivity.kt`**: The single activity in the app. It hosts the Jetpack Compose UI and is responsible for setting up the `LifeTrackerViewModel`.
*   **`LifeUi.kt`**: Contains the Composable functions that build the user interface. It displays the lifecycle logs provided by the `ViewModel`.
*   **`LifecycleLogger.kt`**: A simple class responsible for logging the lifecycle events to the console (Logcat). This provides a real-time view of the lifecycle state changes in Android Studio. [3]
*   **`LifeTrackerViewModel.kt`**: The ViewModel for the main screen. It holds the UI state (the list of lifecycle event logs) and survives configuration changes, ensuring the log history is not lost on events like screen rotation. [4, 6, 11]

## Features

*   **Lifecycle Event Logging**: Logs each of the primary Android Activity lifecycle events (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`). [7]
*   **On-Screen Display**: Shows the sequence of called lifecycle methods directly on the screen.
*   **State Persistence on Configuration Change**: Uses a `ViewModel` to retain the list of logged events during configuration changes (e.g., screen rotation). [11]
*   **Built with Jetpack Compose**: The UI is built entirely with modern, declarative UI toolkit Jetpack Compose. [1]

## How to Use

1.  **Clone the repository:**
    