package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private Button btncadastro;
    private EditText edt_nome;
    private EditText edt_email;
    private EditText edt_senha;
    private EditText edt_senhaconfirme;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        //VERIFICAÇÃO / AUTENTICAÇÃO

        btncadastro = findViewById(R.id.cadastroButtoncliente);
        edt_email = findViewById(R.id.edt_email);
        edt_nome = findViewById(R.id.edt_nome);
        edt_senha = findViewById(R.id.edt_senha);
        edt_senhaconfirme = findViewById(R.id.edt_senhaconfirme);

        btncadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edt_email.getText().toString();
                final String senha = edt_senha.getText().toString();
                String senhaConfirme = edt_senhaconfirme.getText().toString();

                // Verifique se os campos de senha e confirmação de senha coincidem
                if (!senha.equals(senhaConfirme)) {
                    // As senhas não coincidem, mostre uma mensagem de erro
                    Toast.makeText(CadastroActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                //  instância do FirebaseAuth
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                // Criar a conta de usuário usando o Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registro bem-sucedido
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(CadastroActivity.this, "Registro bem-sucedido", Toast.LENGTH_SHORT).show();

                                    //  redirecionar o usuário para a tela de login ou qualquer outra tela apropriada aqui
                                    Intent intent = new Intent(CadastroActivity.this, ClientesActivity.class);
                                    startActivity(intent);
                                    finish(); // Encerrar a atividade atual, se necessário
                                } else {
                                    // O registro falhou, obter o código de erro
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthException e) {
                                        String errorCode = e.getErrorCode();

                                        switch (errorCode) {
                                            case "ERROR_INVALID_EMAIL":
                                                Toast.makeText(CadastroActivity.this, "Endereço de e-mail inválido", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(CadastroActivity.this, "O endereço de e-mail já está em uso", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "ERROR_WEAK_PASSWORD":
                                                Toast.makeText(CadastroActivity.this, "A senha é muito fraca", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(CadastroActivity.this, "Ocorreu um erro durante o registro", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });
    }
}