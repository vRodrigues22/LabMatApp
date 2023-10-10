package com.example.ipet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginParceiroActivity extends AppCompatActivity {

   private Button btnentrarloginparceiro;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_parceiro);


        btnentrarloginparceiro = findViewById(R.id.Loginparceria);


       //direcionar para tela inicial
        btnentrarloginparceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Direcionar para a atividade InicialParciroActivity
                Intent intent = new Intent(LoginParceiroActivity.this, InicialParceiroActivity.class);
                startActivity(intent);
            }
        });





    }
}