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

public class ClientesActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button loginButton;
    private TextView clientecadastro;
    private TextView esqueceuSenhaTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
        setContentView(R.layout.activity_clientes);

        // Inicialize o Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.edt_email2);
        editTextSenha = findViewById(R.id.edt_senha2);
        loginButton = findViewById(R.id.entrarButtoncliente);
        clientecadastro = findViewById(R.id.textView5);
        esqueceuSenhaTextView = findViewById(R.id.esqueceuSenhaTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha o email e a senha inseridos pelo usuário
                String email = editTextEmail.getText().toString();
                String senha = editTextSenha.getText().toString();

                // Faça a autenticação com o Firebase
                mAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(ClientesActivity.this, new OnCompleteListener<AuthResult>() {

                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login bem-sucedido, redirecione o usuário para a tela InicialClienteActivity
                                    Intent intent = new Intent(ClientesActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // O login falhou, exiba uma mensagem de erro
                                    Toast.makeText(ClientesActivity.this, "Falha ao fazer login. Verifique seu email e senha.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        clientecadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientesActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        esqueceuSenhaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha o email inserido pelo usuário
                String email = editTextEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    // Verifique se o campo de email está vazio
                    Toast.makeText(ClientesActivity.this, "Digite seu email para redefinir a senha", Toast.LENGTH_SHORT).show();
                } else {
                    // Envie um email de redefinição de senha para o email fornecido
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Email de redefinição de senha enviado com sucesso
                                        Toast.makeText(ClientesActivity.this, "Email de redefinição de senha enviado. Verifique sua caixa de entrada.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Ocorreu um erro ao enviar o email de redefinição de senha
                                        Toast.makeText(ClientesActivity.this, "Falha ao enviar email de redefinição de senha. Verifique o email inserido.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent telaTemp = new Intent(ClientesActivity.this, MainActivity.class);

                startActivity(telaTemp);
                finish();
            }
        }, 5400000); // Atraso de 5400000 milissegundos (90 segundos ou 1:30)

    }

    private void enableEdgeToEdge() {
    }
}
