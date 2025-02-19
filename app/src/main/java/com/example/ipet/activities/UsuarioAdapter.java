package com.example.ipet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> usuarios;
    private Context context;
    private FirebaseFirestore db;

    public UsuarioAdapter(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        holder.nomeTextView.setText(usuario.getNome());
        holder.emailTextView.setText(usuario.getEmail());
        holder.dataNascimentoTextView.setText(usuario.getDataNascimento());
        holder.pontuacaoTextView.setText(String.valueOf(usuario.getPontuacao()));
        holder.contasResolvidasTextView.setText(String.valueOf(usuario.getContasResolvidas()));
        holder.tempo1TextView.setText(String.valueOf(usuario.getTempo1()));
        holder.tempo2TextView.setText(String.valueOf(usuario.getTempo2()));
        holder.tempo3TextView.setText(String.valueOf(usuario.getTempo3()));

        // Configurando o clique para excluir o usuário
        holder.btnDelete1.setOnClickListener(v -> confirmarExclusao(usuario.getId(), position));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    // Método para confirmar exclusão com um AlertDialog
    private void confirmarExclusao(String userId, int position) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza de que deseja excluir este usuário? Esta ação não pode ser desfeita.")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirUsuario(userId, position);  // Excluir usuário após confirmação
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    // Método para excluir o usuário
    private void excluirUsuario(String userId, int position) {
        db.collection("usuarios").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Excluir o usuário localmente (da lista)
                    usuarios.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Usuário excluído com sucesso", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Caso ocorra erro ao excluir, exibe uma mensagem
                    Toast.makeText(context, "Erro ao excluir o usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        public TextView nomeTextView, emailTextView, dataNascimentoTextView, pontuacaoTextView, contasResolvidasTextView,
                tempo1TextView, tempo2TextView, tempo3TextView, btnDelete1;

        public UsuarioViewHolder(View view) {
            super(view);

            nomeTextView = view.findViewById(R.id.nomeTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            dataNascimentoTextView = view.findViewById(R.id.dataNascimentoTextView);
            pontuacaoTextView = view.findViewById(R.id.pontuacaoTextView);
            contasResolvidasTextView = view.findViewById(R.id.contasResolvidasTextView);
            tempo1TextView = view.findViewById(R.id.tempo1TextView);
            tempo2TextView = view.findViewById(R.id.tempo2TextView);
            tempo3TextView = view.findViewById(R.id.tempo3TextView);
            btnDelete1 = view.findViewById(R.id.btnDelete1);  // Atualizando para o ID correto
        }
    }
}
