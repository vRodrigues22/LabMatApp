package com.example.ipet.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EquacaoviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<EquacaoAdapter.Equacao> equacoesList;
    private EquacaoAdapter equacaoAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacaoview);

        // Inicializa os componentes
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewEquacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cria uma lista de equações e o adapter
        equacoesList = new ArrayList<>();
        equacaoAdapter = new EquacaoAdapter(this, equacoesList);
        recyclerView.setAdapter(equacaoAdapter);

        // Carregar as equações do Firestore
        carregarEquacoes();
    }

    // Método para carregar as equações do Firestore
    private void carregarEquacoes() {
        db.collection("equacoes")
                .get()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        equacoesList.clear(); // Limpa a lista antes de adicionar novas equações
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String equacao = document.getString("equacao");
                            String resposta = document.getString("resposta");
                            String dica = document.getString("dica");

                            // Adiciona a equação à lista
                            equacoesList.add(new EquacaoAdapter.Equacao(equacao, resposta, dica));
                        }
                        equacaoAdapter.notifyDataSetChanged(); // Atualiza o RecyclerView
                    } else {
                        Toast.makeText(EquacaoviewActivity.this, "Erro ao carregar as equações", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
