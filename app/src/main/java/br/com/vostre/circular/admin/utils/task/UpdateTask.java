package br.com.vostre.circular.admin.utils.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import br.com.vostre.circular.admin.model.Empresa;
import br.com.vostre.circular.admin.model.Horario;
import br.com.vostre.circular.admin.model.HorarioItinerario;
import br.com.vostre.circular.admin.model.Itinerario;
import br.com.vostre.circular.admin.model.ParadaItinerario;
import br.com.vostre.circular.admin.model.SecaoItinerario;
import br.com.vostre.circular.admin.utils.listener.UpdateTaskListener;
import br.com.vostre.circular.admin.model.Bairro;
//import br.com.vostre.circular.admin.model.Empresa;
import br.com.vostre.circular.admin.model.Estado;
//import br.com.vostre.circular.admin.model.Horario;
//import br.com.vostre.circular.admin.model.HorarioItinerario;
//import br.com.vostre.circular.admin.model.Itinerario;
import br.com.vostre.circular.admin.model.Local;
import br.com.vostre.circular.admin.model.Pais;
import br.com.vostre.circular.admin.model.Parada;
//import br.com.vostre.circular.admin.model.ParadaItinerario;
//import br.com.vostre.circular.admin.model.SecaoItinerario;
import br.com.vostre.circular.admin.model.dao.CircularDBHelper;
import br.com.vostre.circular.admin.model.dao.PaisDBHelper;

/**
 * Created by Almir on 02/01/2015.
 */
public class UpdateTask extends AsyncTask<String, String, Boolean> {

    JSONObject jObj;
    Context ctx;
    int index = 0;
    UpdateTaskListener listener;

    public UpdateTask(JSONObject obj, Context context){
        this.jObj = obj;
        this.ctx = context;
    }

    public void setOnResultsListener(UpdateTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onUpdateTaskResultsSucceeded(aBoolean);
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try{

            // Arrays recebidos pela API
            JSONArray paises = jObj.getJSONArray("paises");
            JSONArray horarios = jObj.getJSONArray("horarios");
            JSONArray estados = jObj.getJSONArray("estados");
            JSONArray locais = jObj.getJSONArray("locais");
            JSONArray bairros = jObj.getJSONArray("bairros");
            JSONArray paradas = jObj.getJSONArray("paradas");
            JSONArray empresas = jObj.getJSONArray("empresas");
            JSONArray itinerarios = jObj.getJSONArray("itinerarios");
            JSONArray paradasItinerarios = jObj.getJSONArray("paradasItinerarios");
            JSONArray horariosItinerarios = jObj.getJSONArray("horariosItinerarios");
            JSONArray secoesItinerarios = jObj.getJSONArray("secoesItinerarios");

            // Objetos que contem os metodos de atualizacao
            Pais pais = new Pais();
            Horario horario = new Horario();
            Estado estado = new Estado();
            Local local = new Local();
            Bairro bairro = new Bairro();
            Parada parada = new Parada();
            Empresa empresa = new Empresa();
            Itinerario itinerario = new Itinerario();
            ParadaItinerario paradaItinerario = new ParadaItinerario();
            HorarioItinerario horarioItinerario = new HorarioItinerario();
            SecaoItinerario secaoItinerario = new SecaoItinerario();

            CircularDBHelper circularDBHelper = new CircularDBHelper(ctx);

            int qtdPaises = paises.length();

            if(qtdPaises > 0){
                PaisDBHelper paisDBHelper = new PaisDBHelper(ctx);

                // Atualizando paises
                pais.atualizarDados(paises, qtdPaises, ctx);

            }

            int qtdHorarios = horarios.length();

            if(qtdHorarios > 0){

                // Atualizando horarios
                horario.atualizarDados(horarios, qtdHorarios, ctx);

            }

            int qtdEstados = estados.length();

            if(qtdEstados > 0){

                // Atualizando estados
                estado.atualizarDados(estados, qtdEstados, ctx);
            }

            int qtdLocais = locais.length();

            if(qtdLocais > 0){

                // Atualizando locais
                local.atualizarDados(locais, qtdLocais, ctx);
            }

            int qtdBairros = bairros.length();

            if(qtdBairros > 0){

                // Atualizando bairros
                bairro.atualizarDados(bairros, qtdBairros, ctx);
            }

            int qtdParadas = paradas.length();

            if(qtdParadas > 0){

                // Atualizando paradas
                parada.atualizarDados(paradas, qtdParadas, ctx);
            }

            int qtdEmpresas = empresas.length();

            if(qtdEmpresas > 0){

                // Atualizando empresas
                empresa.atualizarDados(empresas, qtdEmpresas, ctx);
            }

            int qtdItinerarios = itinerarios.length();

            if(qtdItinerarios > 0){

                // Atualizando itinerarios
                itinerario.atualizarDados(itinerarios, qtdItinerarios, ctx);
            }

            int qtdParadasItinerarios = paradasItinerarios.length();

            if(qtdParadasItinerarios > 0){

                // Atualizando paradas-itinerario
                paradaItinerario.atualizarDados(paradasItinerarios, qtdParadasItinerarios, ctx);
            }

            int qtdHorariosItinerarios = horariosItinerarios.length();

            if(qtdHorariosItinerarios > 0){

                // Atualiza horarios-itinerario
                horarioItinerario.atualizarDados(horariosItinerarios, qtdHorariosItinerarios, ctx);
            }

            // Atualiza secoes-itinerario

            int qtdSecoesItinerarios = secoesItinerarios.length();

            if(qtdSecoesItinerarios > 0){

                // Atualiza secoes-itinerario
                secaoItinerario.atualizarDados(secoesItinerarios, qtdSecoesItinerarios, ctx);
            }

            /*
            File dbFile =
                    new File(Environment.getDataDirectory() + "/data/br.com.vostre.circular/databases/circular-old.db");

            File exportDir = new File(Environment.getExternalStorageDirectory()+"/db", "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, dbFile.getName());

            try {
                file.createNewFile();
                //ctx.copyFile(dbFile, file);
                //return true;
            } catch (IOException e) {
                Log.e("mypck", e.getMessage(), e);
                return false;
            }
            */
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
