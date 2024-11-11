package com.example.ipet.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    private EditText edtNome, edtEmail, edtSenha;
    private TextView txtPontos;
    private Button btnAtualizar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private DocumentReference userRef;
    private Button btnexcluirperfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        edtNome = findViewById(R.id.nome);
        edtEmail = findViewById(R.id.email);
        edtSenha = findViewById(R.id.senha);
        txtPontos = findViewById(R.id.textView6);  // Campo para exibir os pontos do usuário
        btnAtualizar = findViewById(R.id.update);
        btnexcluirperfil = findViewById(R.id.btnExcluirPerfil);

        // Carregar dados do usuário
        carregarDadosUsuario();

        // Atualizar dados do usuário
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarDadosUsuario();
            }
        });

        btnexcluirperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                mostrarDialogoConfirmacao();
            }
        });
    }

    private void carregarDadosUsuario() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("usuarios").document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    edtNome.setText(document.getString("nome"));
                                    edtEmail.setText(document.getString("email"));
                                    txtPontos.setText("Pontos: " + document.getLong("pontuacao"));
                                } else {
                                    Toast.makeText(PerfilActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PerfilActivity.this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void atualizarDadosUsuario() {
        String novoNome = edtNome.getText().toString();
        String novaSenha = edtSenha.getText().toString();

        if (novoNome.isEmpty()) {
            Toast.makeText(this, "Nome não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Atualizar Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("nome", novoNome);

        db.collection("usuarios").document(currentUser.getUid())
                .update(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PerfilActivity.this, "Nome atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PerfilActivity.this, "Erro ao atualizar nome", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Atualizar senha (se fornecida)
        if (!novaSenha.isEmpty()) {
            currentUser.updatePassword(novaSenha)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PerfilActivity.this, "Senha atualizada com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PerfilActivity.this, "Erro ao atualizar senha", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void excluirPerfil() {
        // 1. Excluir dados do Firestore
        db.collection("usuarios").document(currentUser.getUid())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 2. Excluir conta do Firebase Authentication
                            currentUser.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(PerfilActivity.this, "Perfil excluído com sucesso", Toast.LENGTH_SHORT).show();
                                                // 3. Encerrar a activity e voltar para a tela de login
                                                finish();
                                                // Você pode adicionar aqui a intenção para voltar à tela de login
                                                // startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
                                            } else {
                                                Toast.makeText(PerfilActivity.this, "Erro ao excluir perfil", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(PerfilActivity.this, "Erro ao excluir dados do perfil", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void mostrarDialogoConfirmacao() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir sua conta? Esta ação é irreversível.")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirPerfil(); // Chama excluirPerfil() se o usuário confirmar
                    }
                })
                .setNegativeButton("Não", null) // Não faz nada se o usuário cancelar
                .show();
    }

}
