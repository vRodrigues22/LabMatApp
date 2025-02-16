package com.example.ipet.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ipet.R;

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
            onEquacaoClickListener.onDeleteClick(equacao);  // Lógica de exclusão
        });
    }

    @Override
    public int getItemCount() {
        // Retorna o número de itens na lista de equações
        return equacoesList.size();
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
