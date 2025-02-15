package com.example.ipet.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EquacaoviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<Equacao> equacoesList;
    private EquacaoAdapter equacaoAdapter;
    private ProgressBar progressBar;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacaoview);

        // Inicializa os componentes
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rvTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);

        // Cria a lista de equações e o Adapter
        equacoesList = new ArrayList<>();
        equacaoAdapter = new EquacaoAdapter(this, equacoesList);
        recyclerView.setAdapter(equacaoAdapter);

        // Carregar as equações do Firestore
        carregarEquacoes();
    }

    private void carregarEquacoes() {
        // Exibe o progresso enquanto as equações estão sendo carregadas
        progressBar.setVisibility(View.VISIBLE);
        textInfo.setVisibility(View.VISIBLE);

        db.collection("equacoes")  // A coleção "equacoes" no Firestore
                .get()  // Obtém todos os documentos na coleção
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);  // Esconde o ProgressBar quando terminar
                    textInfo.setVisibility(View.GONE);  // Esconde a mensagem de "Carregando...".

                    if (task.isSuccessful()) {
                        equacoesList.clear();  // Limpa a lista antes de adicionar novas equações
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String equacao = document.getString("equacao");
                            String resposta = document.getString("resposta");
                            String dica = document.getString("dica");

                            // Cria um objeto Equacao e adiciona à lista
                            equacoesList.add(new Equacao(equacao, resposta, dica));
                        }
                        equacaoAdapter.notifyDataSetChanged();  // Atualiza o RecyclerView
                    } else {
                        Toast.makeText(EquacaoviewActivity.this, "Erro ao carregar as equações", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
