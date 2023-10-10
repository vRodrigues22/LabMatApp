package com.example.ipet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.activities.CadastroParceiroActivity;

public class ParceirosActivity extends AppCompatActivity {
  private Button btnentrarparceiro;

 private  Button    btncadastroparceiro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parceiros);



      //declaração
        btnentrarparceiro= findViewById(R.id.entrarparceiro);
        btncadastroparceiro= findViewById(R.id.cadastroparceiro);



        //direcionar para login
        btnentrarparceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParceirosActivity.this,LoginParceiroActivity.class);
                startActivity(intent);
            }
        });


        //direcionar para cadastro
        btncadastroparceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParceirosActivity.this, CadastroParceiroActivity.class);
                startActivity(intent);
            }
        });

    }
}


