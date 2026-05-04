package com.example.mywatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mywatch.databinding.ActivityMainBinding;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int timerValue = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startButton.setOnClickListener(v -> startTimer());
        binding.pauseButton.setOnClickListener(v -> pauseTimer());
        binding.resetButton.setOnClickListener(v -> resetTimer());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("TimerPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timer", timerValue);
        editor.apply();
        pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("TimerPrefs", MODE_PRIVATE);
        timerValue = prefs.getInt("timer", 0);
        updateUI();
    }

    private void startTimer() {
        if (isRunning) return;
        isRunning = true;
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                timerValue++;
                updateUI();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(timerRunnable, 1000);
    }

    private void pauseTimer() {
        if (!isRunning) return;
        isRunning = false;
        handler.removeCallbacks(timerRunnable);
    }

    private void resetTimer() {
        pauseTimer();
        timerValue = 0;
        updateUI();
    }

    private void updateUI() {
        int minutes = timerValue / 60;
        int seconds = timerValue % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.timerTextView.setText(time);
        binding.circularProgressBar.setProgress(timerValue % 100); // Just for visual effect
    }

    public void saveWorkoutSummary(String summary) {
        String filename = "workout_log.txt";
        try (FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(summary.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readWorkoutSummary() {
        String filename = "workout_log.txt";
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fis = openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }
}
