package br.com.vostre.circular.admin.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.circular.admin.model.dao.BairroDBHelper;

/**
 * Created by Almir on 14/04/2014.
 */
public class Bairro extends ClasseBase {

    private Local local;
    private String nome;

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, Context context) throws JSONException {

        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            JSONObject bairroObject =  dados.getJSONObject(i);
            Bairro umBairro = new Bairro();
            umBairro.setIdRemoto(bairroObject.getInt("id"));
            umBairro.setNome(bairroObject.getString("nome"));
            umBairro.setStatus(bairroObject.getInt("status"));

            Local umLocal = new Local();
            umLocal.setId(bairroObject.getInt("local"));

            //umLocal = localDBHelper.carregar(getBaseContext(), umLocal);
            umBairro.setLocal(umLocal);

            bairroDBHelper.salvarOuAtualizar(context, umBairro);

            //paises.get(i).nome;
        }

        bairroDBHelper.deletarInativos(context);

    }

}
