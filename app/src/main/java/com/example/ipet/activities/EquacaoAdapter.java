package com.example.ipet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EquacaoAdapter extends RecyclerView.Adapter<EquacaoAdapter.EquacaoViewHolder> {

    private Context context;
    private List<Equacao> equacoesList;
    private OnEquacaoClickListener onEquacaoClickListener;

    public EquacaoAdapter(Context context, List<Equacao> equacoesList, OnEquacaoClickListener onEquacaoClickListener) {
        this.context = context;
        this.equacoesList = equacoesList;
        this.onEquacaoClickListener = onEquacaoClickListener;
    }

    @Override
    public EquacaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla o layout do item para a RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_equacao_adapter, parent, false);
        return new EquacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquacaoViewHolder holder, int position) {
        // Obtém a equação da lista de equações
        Equacao equacao = equacoesList.get(position);

        // Vincula os dados da equação nos TextViews
        holder.textEquacao.setText(equacao.getEquacao());
        holder.textResposta.setText(equacao.getResposta());
        holder.textDica.setText(equacao.getDica());

        // Lógica para os botões de editar e excluir
        holder.btnEdit.setOnClickListener(v -> {
            onEquacaoClickListener.onEditClick(equacao);  // Lógica de edição
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Mostra um AlertDialog de confirmação antes de excluir
            confirmarExclusao(equacao, position);
        });
    }

    @Override
    public int getItemCount() {
        // Retorna o número de itens na lista de equações
        return equacoesList.size();
    }

    // Metodo para mostrar um AlertDialog pedindo confirmação de exclusão
    private void confirmarExclusao(Equacao equacao, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmar Exclusão")
                .setMessage("Você tem certeza de que deseja excluir esta equação?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluirEquacao(equacao, position);  // Excluir equação após confirmação
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    // Metodo para excluir a equação
    private void excluirEquacao(Equacao equacao, int position) {
        // Supondo que a exclusão seja realizada no Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("equacoes").document(equacao.getId())  // Usando o ID da equação para referenciá-la
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Excluir a equação localmente da lista e notificar o RecyclerView
                    equacoesList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Equação excluída com sucesso", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Caso ocorra erro ao excluir, exibe uma mensagem
                    Toast.makeText(context, "Erro ao excluir a equação: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Interface para os cliques nos botões de editar e excluir
    public interface OnEquacaoClickListener {
        void onEditClick(Equacao equacao);  // Lógica de edição

        void onDeleteClick(Equacao equacao);  // Lógica de exclusão
    }

    // ViewHolder para armazenar as referências dos TextViews e botões
    public class EquacaoViewHolder extends RecyclerView.ViewHolder {

        TextView textEquacao, textResposta, textDica, btnEdit, btnDelete;

        public EquacaoViewHolder(View itemView) {
            super(itemView);
            textEquacao = itemView.findViewById(R.id.textEquacao);
            textResposta = itemView.findViewById(R.id.textResposta);
            textDica = itemView.findViewById(R.id.textDica);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
