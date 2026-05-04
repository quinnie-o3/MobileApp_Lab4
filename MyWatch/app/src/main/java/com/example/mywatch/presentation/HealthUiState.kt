package com.example.mywatch.presentation

import java.util.Locale

enum class WorkoutPhase {
    Idle,
    Running,
    Paused,
}

data class HealthUiState(
    val workoutPhase: WorkoutPhase = WorkoutPhase.Idle,
    val elapsedSeconds: Int = 0,
    val heartRate: Int? = null,
    val calories: Int = 0,
    val distanceKm: Double = 0.0,
    val completedSessions: Int = 0,
) {
    val timerText: String
        get() = String.format(Locale.US, "%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60)

    val heartRateText: String
        get() = heartRate?.let { "$it bpm" } ?: "-- bpm"

    val caloriesText: String
        get() = "$calories cal"

    val distanceText: String
        get() = String.format(Locale.US, "%.2f km", distanceKm)

    val startLabel: String
        get() = if (workoutPhase == WorkoutPhase.Paused) "RESUME" else "START"

    val canStart: Boolean
        get() = workoutPhase != WorkoutPhase.Running

    val canPause: Boolean
        get() = workoutPhase == WorkoutPhase.Running

    val canEnd: Boolean
        get() = elapsedSeconds > 0
}
