package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TurmaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsuarioAdapter adapter;
    private List<Usuario> usuarios;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usuarios = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // Recuperando dados da coleção 'usuarios'
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot document : querySnapshot) {
                            Usuario usuario = document.toObject(Usuario.class);
                            usuarios.add(usuario);
                        }

                        // Atualiza o RecyclerView com os dados
                        adapter = new UsuarioAdapter(usuarios, this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(TurmaActivity.this, "Erro ao carregar os dados", Toast.LENGTH_SHORT).show();
                    }
                });

        // Configurando o FloatingActionButton para redirecionar para CadastroActivity
        findViewById(R.id.fabAdd1).setOnClickListener(v -> {
            Intent intent = new Intent(TurmaActivity.this, CadastroActivity.class);  // Substitua 'CadastroActivity' pelo nome correto da sua Activity de cadastro
            startActivity(intent);
        });
    }
}
