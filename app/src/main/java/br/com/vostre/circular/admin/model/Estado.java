package br.com.vostre.circular.admin.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.circular.admin.model.dao.EstadoDBHelper;

/**
 * Created by Almir on 14/04/2014.
 */
public class Estado extends ClasseBase {

    private Pais pais;
    private String nome;
    private String sigla;

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public String toString() {
        return this.getSigla();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, Context context) throws JSONException{

        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            JSONObject estadoObject =  dados.getJSONObject(i);
            Estado umEstado = new Estado();
            umEstado.setIdRemoto(estadoObject.getInt("id"));
            umEstado.setNome(estadoObject.getString("nome"));
            umEstado.setStatus(estadoObject.getInt("status"));
            umEstado.setSigla(estadoObject.getString("sigla"));

            Pais umPais = new Pais();
            umPais.setId(estadoObject.getInt("pais"));

            umEstado.setPais(umPais);

            estadoDBHelper.salvarOuAtualizar(context, umEstado);

            estadoDBHelper.deletarInativos(context);

        }

    }

}
