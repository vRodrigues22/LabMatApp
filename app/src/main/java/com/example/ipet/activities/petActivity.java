package com.example.ipet.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.example.ipet.crud.Pet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class petActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextRaca;
    private Button addButton;
    private Button deleteButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        editTextName = findViewById(R.id.editTextName);
        editTextRaca = findViewById(R.id.editTextRaca);
        addButton = findViewById(R.id.buttonSave);
        deleteButton = findViewById(R.id.buttonDeletePet);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final String user_email = currentUser.getEmail();
            final CollectionReference petsRef = db.collection("pets");

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editTextName.getText().toString().trim();
                    String raca = editTextRaca.getText().toString().trim();

                    if (!name.isEmpty() && !raca.isEmpty()) {
                        Pet pet = new Pet(name, raca);

                        petsRef.add(pet)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make(v, "Pet adicionado com sucesso.", Snackbar.LENGTH_SHORT).show();
                                            editTextName.setText("");
                                            editTextRaca.setText("");
                                        } else {
                                            Snackbar.make(v, "Erro ao adicionar o pet.", Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Snackbar.make(v, "Preencha todos os campos.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implemente a lógica para excluir um pet aqui
                    // Certifique-se de obter o ID do pet que deseja excluir
                }
            });
        }
    }
}