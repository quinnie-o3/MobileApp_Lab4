package com.example.mywatch.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import com.example.mywatch.presentation.theme.MyWatchTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWatchTheme {
                HealthTrackingApp()
            }
        }
    }
}

private const val PREFS_NAME = "health_demo_preferences"
private const val KEY_LAST_DURATION_SECONDS = "lastDurationSeconds"
private const val KEY_LAST_CALORIES = "lastCalories"
private const val KEY_LAST_DISTANCE = "lastDistance"
private const val KEY_COMPLETED_SESSIONS = "completedSessions"

private val AccentOrange = Color(0xFFFF9F43)
private val AccentOrangeMuted = Color(0xFF6A4320)
private val BackgroundGray = Color(0xFF2C2C2C)
private val DisabledGray = Color(0xFF4A4A4A)

@Composable
private fun HealthTrackingApp() {
    val context = LocalContext.current
    val preferences = remember(context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    var uiState by remember {
        mutableStateOf(
            HealthUiState(
                completedSessions = preferences.getInt(KEY_COMPLETED_SESSIONS, 0),
            ),
        )
    }

    LaunchedEffect(uiState.workoutPhase) {
        while (uiState.workoutPhase == WorkoutPhase.Running) {
            delay(1_000)
            uiState = advanceWorkout(uiState)
        }
    }

    HealthTrackingScreen(
        uiState = uiState,
        onStart = {
            if (uiState.canStart) {
                uiState =
                    uiState.copy(
                        workoutPhase = WorkoutPhase.Running,
                        heartRate = uiState.heartRate ?: simulateHeartRate(null, uiState.elapsedSeconds),
                    )
            }
        },
        onPause = {
            if (uiState.canPause) {
                uiState = uiState.copy(workoutPhase = WorkoutPhase.Paused)
            }
        },
        onEnd = {
            if (uiState.canEnd) {
                val updatedSessions = uiState.completedSessions + 1
                saveSession(preferences, uiState, updatedSessions)
                uiState = HealthUiState(completedSessions = updatedSessions)
            }
        },
    )
}

@Composable
private fun HealthTrackingScreen(
    uiState: HealthUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onEnd: () -> Unit,
) {
    AppScaffold {
        val listState = rememberTransformingLazyColumnState()
        ScreenScaffold(
            scrollState = listState,
            edgeButton = {
                EdgeButton(
                    onClick = {
                        if (uiState.canEnd) {
                            onEnd()
                        }
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = if (uiState.canEnd) AccentOrange else DisabledGray,
                            contentColor = if (uiState.canEnd) Color.Black else Color.White,
                        ),
                ) {
                    Text(
                        text = "END",
                        color = if (uiState.canEnd) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
        ) { contentPadding ->
            TransformingLazyColumn(
                contentPadding = contentPadding,
                state = listState,
            ) {
                item {
                    CenteredText(
                        text = "Health Measure",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        top = 8.dp,
                        bottom = 6.dp,
                    )
                }
                item {
                    CenteredText(
                        text = uiState.timerText,
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        top = 2.dp,
                        bottom = 8.dp,
                    )
                }
                item { MetricLine(label = "Heart", value = uiState.heartRateText) }
                item { MetricLine(label = "Calories", value = uiState.caloriesText) }
                item { MetricLine(label = "Distance", value = uiState.distanceText) }
                item { MetricLine(label = "Sessions", value = uiState.completedSessions.toString()) }
                item {
                    ControlButtons(
                        uiState = uiState,
                        onStart = onStart,
                        onPause = onPause,
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    fontWeight: FontWeight,
    top: androidx.compose.ui.unit.Dp,
    bottom: androidx.compose.ui.unit.Dp,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = top, bottom = bottom),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
        )
    }
}

@Composable
private fun MetricLine(label: String, value: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$label: $value",
            color = AccentOrange,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun ControlButtons(
    uiState: HealthUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    if (uiState.canStart) {
                        onStart()
                    }
                },
                modifier = Modifier.weight(1f),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (uiState.canStart) AccentOrange else AccentOrangeMuted,
                        contentColor = Color.Black,
                    ),
            ) {
                Text(
                    text = uiState.startLabel,
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (uiState.canPause) {
                        onPause()
                    }
                },
                modifier = Modifier.weight(1f),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = if (uiState.canPause) BackgroundGray else DisabledGray,
                        contentColor = Color.White,
                    ),
            ) {
                Text(
                    text = "PAUSE",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private fun advanceWorkout(state: HealthUiState): HealthUiState {
    val nextElapsedSeconds = state.elapsedSeconds + 1
    val nextHeartRate =
        if (nextElapsedSeconds == 1 || nextElapsedSeconds % 3 == 0) {
            simulateHeartRate(state.heartRate, nextElapsedSeconds)
        } else {
            state.heartRate ?: simulateHeartRate(null, nextElapsedSeconds)
        }

    return state.copy(
        elapsedSeconds = nextElapsedSeconds,
        heartRate = nextHeartRate,
        calories = (nextElapsedSeconds * 0.08f).roundToInt(),
        distanceKm = nextElapsedSeconds * 0.002,
    )
}

private fun saveSession(
    preferences: SharedPreferences,
    state: HealthUiState,
    completedSessions: Int,
) {
    preferences.edit()
        .putInt(KEY_LAST_DURATION_SECONDS, state.elapsedSeconds)
        .putInt(KEY_LAST_CALORIES, state.calories)
        .putFloat(KEY_LAST_DISTANCE, state.distanceKm.toFloat())
        .putInt(KEY_COMPLETED_SESSIONS, completedSessions)
        .apply()
}

// TODO: Replace simulated health data with Wear OS Health Services.
private fun simulateHeartRate(currentHeartRate: Int?, elapsedSeconds: Int): Int {
    val baseline = 82 + (elapsedSeconds / 45).coerceAtMost(18)
    val anchor = currentHeartRate ?: baseline + Random.nextInt(0, 6)
    val jitter = Random.nextInt(-4, 5)
    return (anchor + jitter).coerceIn(70, 150)
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    MyWatchTheme {
        HealthTrackingScreen(
            uiState =
                HealthUiState(
                    workoutPhase = WorkoutPhase.Running,
                    elapsedSeconds = 85,
                    heartRate = 93,
                    calories = 7,
                    distanceKm = 0.17,
                    completedSessions = 2,
                ),
            onStart = {},
            onPause = {},
            onEnd = {},
        )
    }
}
