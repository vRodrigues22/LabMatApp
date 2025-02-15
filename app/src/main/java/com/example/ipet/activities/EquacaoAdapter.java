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

    public EquacaoAdapter(Context context, List<Equacao> equacoesList) {
        this.context = context;
        this.equacoesList = equacoesList;
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
    }

    @Override
    public int getItemCount() {
        // Retorna o número de itens na lista de equações
        return equacoesList.size();
    }

    // ViewHolder para armazenar as referências dos TextViews
    public class EquacaoViewHolder extends RecyclerView.ViewHolder {

        TextView textEquacao, textResposta, textDica;

        public EquacaoViewHolder(View itemView) {
            super(itemView);
            textEquacao = itemView.findViewById(R.id.textEquacao);
            textResposta = itemView.findViewById(R.id.textResposta);
            textDica = itemView.findViewById(R.id.textDica);
        }
    }
}

