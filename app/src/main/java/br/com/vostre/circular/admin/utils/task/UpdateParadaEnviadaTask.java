package br.com.vostre.circular.admin.utils.task;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.model.dao.ParadaDBHelper;
import br.com.vostre.circular.admin.utils.listener.UpdateMessageTaskListener;

/**
 * Created by Almir on 30/08/2015.
 */
public class UpdateParadaEnviadaTask extends AsyncTask<String, String, Boolean> {

    Context ctx;
    JSONObject jObj;
    UpdateMessageTaskListener listener;

    public UpdateParadaEnviadaTask(JSONObject obj, Context context){
        this.jObj = obj;
        this.ctx = context;
    }

    public void setOnResultsListener(UpdateMessageTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try{

            // Arrays recebidos pela API
            JSONArray paradas = jObj.getJSONArray("processadas");

            // Objetos que contem os metodos de atualizacao
            Parada parada = new Parada();

            ParadaDBHelper paradaColetaDBHelper = new ParadaDBHelper(ctx);

            int qtdParadas = paradas.length();

            if(qtdParadas > 0){

                for(int i = 0; i < qtdParadas; i++){
                    Integer paradaId =  paradas.getInt(i);
                    Parada umaParada = new Parada();

                    umaParada.setId(paradaId);
                    umaParada = paradaColetaDBHelper.carregar(ctx, umaParada);
                    umaParada.setStatus(4);
                    paradaColetaDBHelper.salvarOuAtualizar(ctx, umaParada);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //atualiza icone de mensagens
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onUpdateMessageTaskResultsSucceeded(aBoolean);
    }
}
