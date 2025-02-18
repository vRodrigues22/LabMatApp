package com.example.ipet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ipet.R;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button buttonNivel1, buttonNivel2, buttonNivel3, perfil, daily, toggleThemeButton, btnProgresso;
    private ImageView iniciarsom, pausarsom;
    private MediaPlayer mediaPlayer;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carrega a preferência de tema antes de definir o layout
        loadThemePreference();

        setContentView(R.layout.activity_main);

        // Inicializa os botões e ícones de som
        buttonNivel1 = findViewById(R.id.buttonivel1);
        buttonNivel2 = findViewById(R.id.buttonnivel2);
        buttonNivel3 = findViewById(R.id.buttonnivel3);
        perfil = findViewById(R.id.perfil);
        daily = findViewById(R.id.dailyButton);
        btnProgresso = findViewById(R.id.btnProgresso);
        toggleThemeButton = findViewById(R.id.toggleThemeButton);

        iniciarsom = findViewById(R.id.iniciarsom);
        pausarsom = findViewById(R.id.pausarsom);

        // Inicializa o MediaPlayer com a música
        mediaPlayer = MediaPlayer.create(this, R.raw.audio_lab);

// Configura o ícone para iniciar a música
        iniciarsom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

// Configura o ícone para parar a música
        pausarsom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    // Removido: try { mediaPlayer.prepare(); } catch (IOException e) { e.printStackTrace(); }
                }
            }
        });

        // Configurações dos botões de níveis e outras atividades
        buttonNivel1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LabyrinthActivity.class)));
        buttonNivel2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Labirinto2Activity.class)));
        buttonNivel3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Labirinto3Activity.class)));
        perfil.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PerfilActivity.class)));
        daily.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DailyActivity.class)));

        toggleThemeButton.setOnClickListener(v -> toggleTheme());

        btnProgresso.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProgressoActivity.class)));
    }

    private void toggleTheme() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void saveThemePreference(int mode) {
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme_mode", mode);
        editor.apply();
    }

    private void loadThemePreference() {
        SharedPreferences preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int themeMode = preferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(themeMode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}