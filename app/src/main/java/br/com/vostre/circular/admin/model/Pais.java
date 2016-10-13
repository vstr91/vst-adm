package br.com.vostre.circular.admin.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.circular.admin.model.dao.PaisDBHelper;

/**
 * Created by Almir on 14/04/2014.
 */
public class Pais extends ClasseBase {

    private String nome;
    private String iso3;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    @Override
    public String toString() {
        return this.getNome()+" - "+this.getStatus();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, Context context) throws JSONException {

        PaisDBHelper paisDBHelper = new PaisDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            JSONObject paisObject =  dados.getJSONObject(i);
            Pais umPais = new Pais();
            umPais.setIdRemoto(paisObject.getInt("id"));
            umPais.setNome(paisObject.getString("nome"));
            umPais.setStatus(paisObject.getInt("status"));
            umPais.setIso3(paisObject.getString("iso3"));

            paisDBHelper.salvarOuAtualizar(context, umPais);

        }

        paisDBHelper.deletarInativos(context);

    }

}
