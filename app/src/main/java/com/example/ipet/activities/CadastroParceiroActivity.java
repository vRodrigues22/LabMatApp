package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.InicialParceiroActivity;
import com.example.ipet.R;


public class CadastroParceiroActivity extends AppCompatActivity {

    private Button btncadastroparceiro;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_parceiro);




        btncadastroparceiro=findViewById(R.id.buttonCadastroparceiro);



        btncadastroparceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent intent = new Intent(CadastroParceiroActivity.this, InicialParceiroActivity.class);
        startActivity(intent);
        finish();


            }
        });






    }
}