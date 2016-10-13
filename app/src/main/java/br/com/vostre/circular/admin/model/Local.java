package br.com.vostre.circular.admin.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.circular.admin.model.dao.LocalDBHelper;

/**
 * Created by Almir on 14/04/2014.
 */
public class Local extends ClasseBase {

    private Estado estado;
    private Local cidade;
    private String nome;

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Local getCidade() {
        return cidade;
    }

    public void setCidade(Local cidade) {
        this.cidade = cidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {

        if(null != this.getCidade()){
            return this.getNome() + " (" + this.getCidade() + ")";
        } else{
            return this.getNome();
        }



        //return this.getNome();

    }

    public void atualizarDados(JSONArray dados, int qtdDados, Context context) throws JSONException {

        LocalDBHelper localDBHelper = new LocalDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            JSONObject localObject =  dados.getJSONObject(i);
            Local umLocal = new Local();
            umLocal.setIdRemoto(localObject.getInt("id"));
            umLocal.setNome(localObject.getString("nome"));
            umLocal.setStatus(localObject.getInt("status"));

            Estado umEstado = new Estado();
            umEstado.setIdRemoto(localObject.getInt("estado"));

            umLocal.setEstado(umEstado);

            Local umaCidade = new Local();
            umaCidade.setIdRemoto(localObject.getInt("cidade"));

            umLocal.setCidade(umaCidade);

            localDBHelper.salvarOuAtualizar(context, umLocal);

        }

        localDBHelper.deletarInativos(context);

    }

}
