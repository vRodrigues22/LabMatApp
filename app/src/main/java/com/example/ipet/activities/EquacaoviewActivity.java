package com.example.ipet.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.google.firebase.database.core.view.View;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EquacaoviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<Equacao> equacoesList;
    private EquacaoAdapter equacaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equacaoview);

        // Inicializa os componentes
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rvTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cria a lista de equações e o Adapter
        equacoesList = new ArrayList<>();
        equacaoAdapter = new EquacaoAdapter(this, equacoesList, new EquacaoAdapter.OnEquacaoClickListener() {
            @Override
            public void onEditClick(Equacao equacao) {
                // Redireciona para a tela de edição ao clicar no botão de editar
                Intent intent = new Intent(EquacaoviewActivity.this, EquacaoeditActivity.class);
                intent.putExtra("equacao_id", equacao.getId());  // Passando o ID da equação para a edição
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Equacao equacao) {
                excluirEquacao(equacao);  // Lógica para excluir a equação
            }
        });
        recyclerView.setAdapter(equacaoAdapter);

        // Carregar as equações do Firestore
        carregarEquacoes();
    }

    private void carregarEquacoes() {
        db.collection("equacoes")  // A coleção "equacoes" no Firestore
                .get()  // Obtém todos os documentos na coleção
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        equacoesList.clear();  // Limpa a lista antes de adicionar novas equações
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String equacao = document.getString("equacao");
                            String resposta = document.getString("resposta");
                            String dica = document.getString("dica");
                            String id = document.getId();  // Pega o ID do documento

                            // Cria um objeto Equacao e adiciona à lista
                            equacoesList.add(new Equacao(equacao, resposta, dica, id));
                        }
                        equacaoAdapter.notifyDataSetChanged();  // Atualiza o RecyclerView
                    } else {
                        Toast.makeText(EquacaoviewActivity.this, "Erro ao carregar as equações", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void excluirEquacao(Equacao equacao) {
        // Exclui a equação do Firebase
        db.collection("equacoes").document(equacao.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EquacaoviewActivity.this, "Equação excluída com sucesso", Toast.LENGTH_SHORT).show();
                        carregarEquacoes();  // Recarregar as equações após a exclusão
                    } else {
                        Toast.makeText(EquacaoviewActivity.this, "Erro ao excluir equação", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
