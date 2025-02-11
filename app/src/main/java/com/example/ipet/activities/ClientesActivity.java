package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClientesActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextSenha;
    private Button loginButton;
    private TextView clientecadastro, esqueceuSenhaTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        inicializarComponentes();
        verificarUsuarioLogado();
        configurarListeners();
    }

    /**
     * Inicializa os componentes da interface.
     */
    private void inicializarComponentes() {
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.edt_email2);
        editTextSenha = findViewById(R.id.edt_senha2);
        loginButton = findViewById(R.id.entrarButtoncliente);
        clientecadastro = findViewById(R.id.textView5);
        esqueceuSenhaTextView = findViewById(R.id.esqueceuSenhaTextView);
    }

    /**
     * Verifica se o usuário já está logado ou logou recentemente.
     */
    private void verificarUsuarioLogado() {
        FirebaseUser usuarioAtual = mAuth.getCurrentUser();
        if (usuarioAtual != null) {
            // Se o usuário já está autenticado, redireciona para a MainActivity
            redirecionarParaMainActivity();
        }
    }

    /**
     * Configura os eventos de clique dos botões e links da interface.
     */
    private void configurarListeners() {
        loginButton.setOnClickListener(v -> realizarLogin());
        clientecadastro.setOnClickListener(v -> abrirTelaCadastro());
        esqueceuSenhaTextView.setOnClickListener(v -> recuperarSenha());

        // Timer para redirecionamento automático após 90 minutos
        new Handler(Looper.getMainLooper()).postDelayed(this::redirecionarParaMainActivity, 5400000);
    }

    /**
     * Realiza o login do usuário no Firebase.
     */
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
                        redirecionarParaMainActivity();
                    } else {
                        exibirMensagem("Falha ao fazer login. Verifique seu email e senha.");
                    }
                });
    }

    /**
     * Redireciona o usuário para a tela de cadastro.
     */
    private void abrirTelaCadastro() {
        startActivity(new Intent(ClientesActivity.this, CadastroActivity.class));
    }

    /**
     * Envia um email para redefinição de senha.
     */
    private void recuperarSenha() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            exibirMensagem("Digite seu email para redefinir a senha");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        exibirMensagem("Email de redefinição de senha enviado.");
                    } else {
                        exibirMensagem("Falha ao enviar email de redefinição de senha.");
                    }
                });
    }

    /**
     * Redireciona para a MainActivity e finaliza a tela de login.
     */
    private void redirecionarParaMainActivity() {
        startActivity(new Intent(ClientesActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Exibe um Toast com uma mensagem.
     *
     * @param mensagem Mensagem a ser exibida.
     */
    private void exibirMensagem(String mensagem) {
        Toast.makeText(ClientesActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
