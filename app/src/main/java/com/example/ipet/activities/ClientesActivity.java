package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientesActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextSenha;
    private Button loginButton;
    private TextView clientecadastro, esqueceuSenhaTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        inicializarComponentes();
        verificarUsuarioLogado();
        configurarListeners();
    }

    private void inicializarComponentes() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.edt_email2);
        editTextSenha = findViewById(R.id.edt_senha2);
        loginButton = findViewById(R.id.entrarButtoncliente);
        clientecadastro = findViewById(R.id.textView5); // TextView "Cadastre-se"
        esqueceuSenhaTextView = findViewById(R.id.esqueceuSenhaTextView);  // TextView "Esqueceu a senha?"
    }

    private void verificarUsuarioLogado() {
        FirebaseUser usuarioAtual = mAuth.getCurrentUser();
        if (usuarioAtual != null) {
            // Se o usuário está logado, verifica se é super usuário
            verificarSeSuperUsuario(usuarioAtual);
        }
    }

    private void verificarSeSuperUsuario(FirebaseUser usuario) {
        db.collection("usuarios").document(usuario.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.getBoolean("isSuperUser") != null && document.getBoolean("isSuperUser")) {
                            // O usuário é o super usuário
                            redirecionarParaHomeProfessorActivity(); // Redireciona para HomeProfessorActivity
                        } else {
                            // O usuário não é o super usuário
                            redirecionarParaMainActivity(); // Redireciona para MainActivity
                        }
                    }
                });
    }

    private void realizarLogin() {
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            exibirMensagem("Preencha todos os campos!");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        verificarSeSuperUsuario(mAuth.getCurrentUser());
                    } else {
                        exibirMensagem("Falha ao fazer login. Verifique seu email e senha.");
                    }
                });
    }

    private void redirecionarParaHomeProfessorActivity() {
        Intent intent = new Intent(ClientesActivity.this, HomeProfessorActivity.class);  // Redireciona para HomeProfessorActivity
        startActivity(intent);
        finish();
    }

    private void redirecionarParaMainActivity() {
        Intent intent = new Intent(ClientesActivity.this, MainActivity.class);  // Redireciona para MainActivity
        startActivity(intent);
        finish();
    }

    private void exibirMensagem(String mensagem) {
        Toast.makeText(ClientesActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }

    // Configura os listeners de clique
    private void configurarListeners() {
        // Listener para o botão de login
        loginButton.setOnClickListener(v -> realizarLogin());

        // Listener para o link "Cadastre-se"
        clientecadastro.setOnClickListener(v -> abrirTelaCadastro());

        // Listener para o link "Esqueceu a senha"
        esqueceuSenhaTextView.setOnClickListener(v -> mostrarDialogoRedefinirSenha());
    }

    // Abre a tela de cadastro
    private void abrirTelaCadastro() {
        Intent intent = new Intent(ClientesActivity.this, CadastroActivity.class);  // Redireciona para CadastroActivity
        startActivity(intent);
    }

    // Metodo para mostrar o diálogo de redefinição de senha
    private void mostrarDialogoRedefinirSenha() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            exibirMensagem("Por favor, insira seu email.");
            return;
        }

        // Enviar email de redefinição de senha
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        exibirMensagem("Email de redefinição de senha enviado.");
                    } else {
                        exibirMensagem("Erro ao enviar o email. Verifique o endereço de e-mail.");
                    }
                });
    }
}
