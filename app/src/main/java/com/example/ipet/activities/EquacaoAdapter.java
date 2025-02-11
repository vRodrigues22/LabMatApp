package com.example.ipet.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ipet.R;
import java.util.List;

public class EquacaoAdapter extends RecyclerView.Adapter<EquacaoAdapter.EquacaoViewHolder> {

    private List<Equacao> equacoesList;

    public EquacaoAdapter(EquacaoviewActivity equacaoviewActivity, List<Equacao> equacoesList) {
        this.equacoesList = equacoesList;
    }

    @NonNull
    @Override
    public EquacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_equacoes, parent, false);
        return new EquacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquacaoViewHolder holder, int position) {
        Equacao equacao = equacoesList.get(position);
        holder.textEquacao.setText(equacao.getEquacao());
        holder.textResposta.setText(equacao.getResposta());
        holder.textDica.setText(equacao.getDica());
    }

    @Override
    public int getItemCount() {
        return equacoesList.size();
    }

    public static class EquacaoViewHolder extends RecyclerView.ViewHolder {

        TextView textEquacao, textResposta, textDica;

        public EquacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textEquacao = itemView.findViewById(R.id.textEquacao);
            textResposta = itemView.findViewById(R.id.textResposta);
            textDica = itemView.findViewById(R.id.textDica);
        }
    }

    // Classe interna para representar cada equação
    public static class Equacao {
        private String equacao;
        private String resposta;
        private String dica;

        public Equacao(String equacao, String resposta, String dica) {
            this.equacao = equacao;
            this.resposta = resposta;
            this.dica = dica;
        }

        public String getEquacao() {
            return equacao;
        }

        public String getResposta() {
            return resposta;
        }

        public String getDica() {
            return dica;
        }
    }
}
