package com.example.ipet.activities;

public class Equacao {
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


/*
import android.os.Parcelable
import br.com.hellodev.task.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Int = 0
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}
 */