package com.example.ipet.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtSenha;
    private Button btnAtualizar, btnExcluirPerfil, btnSairPerfil;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inicializarComponentes();
        carregarDadosUsuario();
        configurarListeners();
    }

    /**
     * Inicializa os componentes da interface.
     */
    private void inicializarComponentes() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        edtNome = findViewById(R.id.nome);
        edtEmail = findViewById(R.id.email);
        edtSenha = findViewById(R.id.senha);
        btnAtualizar = findViewById(R.id.update);
        btnExcluirPerfil = findViewById(R.id.btnExcluirPerfil);
        btnSairPerfil = findViewById(R.id.btnSairPerfil);
        profileImage = findViewById(R.id.profileimage);
    }

    /**
     * Carrega os dados do usuário do Firestore.
     */
    private void carregarDadosUsuario() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("usuarios").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                edtNome.setText(document.getString("nome"));
                                edtEmail.setText(document.getString("email"));
                            } else {
                                exibirMensagem("Usuário não encontrado.");
                            }
                        } else {
                            exibirMensagem("Erro ao carregar dados.");
                        }
                    });
        }
    }

    /**
     * Configura os eventos de clique dos botões.
     */
    private void configurarListeners() {
        btnAtualizar.setOnClickListener(v -> atualizarDadosUsuario());
        btnExcluirPerfil.setOnClickListener(v -> mostrarDialogoConfirmacaoExclusao());
        btnSairPerfil.setOnClickListener(v -> mostrarDialogoConfirmacaoLogout());
    }

    /**
     * Atualiza os dados do usuário no Firestore e Firebase Authentication.
     */
    private void atualizarDadosUsuario() {
        String novoNome = edtNome.getText().toString().trim();
        String novaSenha = edtSenha.getText().toString().trim();

        if (novoNome.isEmpty()) {
            exibirMensagem("Nome não pode estar vazio.");
            return;
        }

        // Atualiza Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("nome", novoNome);

        db.collection("usuarios").document(currentUser.getUid())
                .update(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        exibirMensagem("Nome atualizado com sucesso.");
                    } else {
                        exibirMensagem("Erro ao atualizar nome.");
                    }
                });

        // Atualiza a senha se fornecida
        if (!novaSenha.isEmpty()) {
            currentUser.updatePassword(novaSenha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            exibirMensagem("Senha atualizada com sucesso.");
                        } else {
                            exibirMensagem("Erro ao atualizar senha.");
                        }
                    });
        }
    }

    /**
     * Exclui o perfil do usuário do Firestore e Firebase Authentication.
     */
    private void excluirPerfil() {
        db.collection("usuarios").document(currentUser.getUid())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        exibirMensagem("Perfil excluído com sucesso.");
                                        redirecionarParaLogin();
                                    } else {
                                        exibirMensagem("Erro ao excluir perfil.");
                                    }
                                });
                    } else {
                        exibirMensagem("Erro ao excluir dados do perfil.");
                    }
                });
    }

    /**
     * Exibe um diálogo para confirmar a exclusão do perfil.
     */
    private void mostrarDialogoConfirmacaoExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir sua conta? Esta ação é irreversível.")
                .setPositiveButton("Sim", (dialog, which) -> excluirPerfil())
                .setNegativeButton("Não", null)
                .show();
    }

    /**
     * Exibe um diálogo para confirmar o logout do usuário.
     */
    private void mostrarDialogoConfirmacaoLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Logout")
                .setMessage("Tem certeza que deseja sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    mAuth.signOut();
                    redirecionarParaLogin();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    /**
     * Redireciona o usuário para a tela de login.
     */
    private void redirecionarParaLogin() {
        Intent intent = new Intent(PerfilActivity.this, ClientesActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Exibe um Toast com uma mensagem.
     *
     * @param mensagem Mensagem a ser exibida.
     */
    private void exibirMensagem(String mensagem) {
        Toast.makeText(PerfilActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
