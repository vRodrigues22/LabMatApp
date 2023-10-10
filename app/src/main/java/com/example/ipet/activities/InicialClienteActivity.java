package com.example.ipet.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.ipet.R;
import com.example.ipet.fragments.NotificationFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class InicialClienteActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 9;
    private ImageButton btnperfil;
    private ImageButton imageButtonagenda;

    private ImageButton imageButtonmeuspets;
    private ImageButton notificationButton;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial_cliente);

        // Inicializar a FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Verificar se a permissão de localização já foi concedida.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // A permissão já foi concedida. Você pode iniciar o rastreamento de GPS aqui.
            startLocationTracking();
        } else {
            // A permissão ainda não foi concedida. Solicite-a ao usuário.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }

        imageButtonagenda = findViewById(R.id.imageButtonagenda);
        btnperfil = findViewById(R.id.imageButtonperfil);
       // imageButtonmeuspets=findViewById(R.id.imageButtonmeuspets);

        // direcionar para a agenda
        imageButtonagenda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent Intent = new Intent(InicialClienteActivity.this, AgendaActivity.class);
            }
        });


        //direcionar para perfil
        btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicialClienteActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);
            }
        });

        imageButtonmeuspets = findViewById(R.id.imageButtonmeuspets);
        //direcionar para perfil
        imageButtonmeuspets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicialClienteActivity.this, petActivity.class);
                startActivity(intent);
            }
        });



        // Inicialize a view do botão de notificações
        notificationButton = findViewById(R.id.notificationButton);

        // Defina o listener para o botão de notificação
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chame o método para exibir a notificação
                showNotification();

                // Substituir o conteúdo do fragmento com o Fragment de notificação
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new NotificationFragment());
                fragmentTransaction.addToBackStack(null);  // Isso permite voltar ao fragmento anterior
                fragmentTransaction.commit();
            }
        });

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "lembrete_channel";
            CharSequence channelName = "Lembretes";
            String channelDescription = "Canal para lembretes diários";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            // Registre o canal na NotificationManager
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    // Método para exibir a notificação
    private void showNotification() {
        String channelId = "lembrete_channel";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification) // Ícone da notificação
                .setContentTitle("Título da Notificação")
                .setContentText("Conteúdo da Notificação")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager.class);
        }
        notificationManager.notify(0, builder.build());
    }


    private void startLocationTracking() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // A localização foi obtida com sucesso.
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Faça algo com os valores de latitude e longitude.
                    // Por exemplo, exiba-os em um Toast:
                    Toast.makeText(InicialClienteActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                } else {
                    // Não foi possível obter a localização.
                    Toast.makeText(InicialClienteActivity.this, "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Lida com a resposta do usuário à solicitação de permissão de localização
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            // Verifique se a permissão foi concedida.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, você pode iniciar o rastreamento de GPS aqui.
                startLocationTracking();
            } else {
                // Permissão negada pelo usuário. Lide com isso adequadamente, por exemplo, mostrando uma mensagem.
                Toast.makeText(this, "A permissão de localização foi negada.", Toast.LENGTH_SHORT).show();
            }
        }


    }

}



