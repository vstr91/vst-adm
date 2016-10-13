package br.com.vostre.circular.admin.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.model.dao.ParadaDBHelper;
import br.com.vostre.circular.admin.utils.Constants;
import br.com.vostre.circular.admin.utils.Crypt;
import br.com.vostre.circular.admin.utils.HttpUtils;
import br.com.vostre.circular.admin.utils.enums.TipoToken;
import br.com.vostre.circular.admin.utils.listener.MessageTaskListener;
import br.com.vostre.circular.admin.utils.listener.ServerUtilsListener;
import br.com.vostre.circular.admin.utils.listener.TokenTaskListener;
import br.com.vostre.circular.admin.utils.listener.UpdateMessageTaskListener;
import br.com.vostre.circular.admin.utils.task.MessageTask;
import br.com.vostre.circular.admin.utils.task.ServerUtils;
import br.com.vostre.circular.admin.utils.task.TokenTask;
import br.com.vostre.circular.admin.utils.task.UpdateParadaEnviadaTask;

/**
 * Created by Almir on 30/08/2015.
 */
public class SendParadaService extends Service implements ServerUtilsListener, TokenTaskListener,
        MessageTaskListener, UpdateMessageTaskListener {

    ServerUtils serverUtils;
    int registros = 0;
    static final long TIME_TO_UPDATE = TimeUnit.MINUTES.toMillis(Constants.TEMPO_ATUALIZACAO_ENVIO_PARADAS);
    List<Parada> paradas;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ParadaDBHelper paradaDBHelper = new ParadaDBHelper(getApplicationContext());
                paradas = paradaDBHelper.listarTodosNaoEnviados(getApplicationContext());

                registros = paradas.size();

                if(registros > 0){

                    if(HttpUtils.isOnline(getApplicationContext())){
                        serverUtils = new ServerUtils(SendParadaService.this, true);
                        serverUtils.setOnResultsListener(SendParadaService.this);
                        serverUtils.execute(new String[]{Constants.SERVIDOR_TESTE, String.valueOf(Constants.PORTASERVIDOR)});
                    }

                } else{
                    stopSelf();
                }



            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, TIME_TO_UPDATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(MessageService.this, "Iniciando rodada", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onServerUtilsResultsSucceeded(boolean result) {

        if(result){

            String urlToken = Constants.URLTOKEN;

            TokenTask tokenTask = new TokenTask(urlToken, SendParadaService.this, true, TipoToken.ENVIO_PARADA.getTipo());
            tokenTask.setOnTokenTaskResultsListener(this);
            tokenTask.execute();

        }

    }

    @Override
    public void onTokenTaskResultsSucceeded(String token) {

        Crypt crypt = new Crypt();

        String tokenCriptografado = null;
        try {
            tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));

            String url = Constants.URLSERVIDORENVIAPARADA+tokenCriptografado;
//            String url = Constants.URLSERVIDORMSG+tokenCriptografado+"/-";

            String json = "{\"paradas\":[";
            int cont = 1;

            for(Parada umaParada : paradas){

                if(cont < registros){
                    json = json.concat(umaParada.toJson()+",");
                } else{
                    json = json.concat(umaParada.toJson());
                }

                cont++;

            }

            json = json.concat("]}");
            System.out.println(json);

            Map<String, String> map = new HashMap<>();
            map.put("dados", json);
            map.put("total", String.valueOf(paradas.size()));

            MessageTask mst = new MessageTask(url, "POST", SendParadaService.this, map);
            mst.setOnResultsListener(this);
            mst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessageTaskResultsSucceeded(Map<String, Object> map) {

        JSONObject jObj = (JSONObject) map.get("json");

        int status = 0;

        if(jObj != null){
            try {
                JSONArray metadados = jObj.getJSONArray("meta");
                JSONObject objMetadados = metadados.getJSONObject(0);

                registros = objMetadados.getInt("registros");
                status = objMetadados.getInt("status");

                JSONArray processadas = jObj.getJSONArray("processadas");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            registros = 0;
        }

        if(registros > 0){
            UpdateParadaEnviadaTask updateParadaEnviadaTask = new UpdateParadaEnviadaTask(jObj, this);
            updateParadaEnviadaTask.setOnResultsListener(this);
            updateParadaEnviadaTask.execute();
        }

    }

    @Override
    public void onUpdateMessageTaskResultsSucceeded(boolean result) {

        if(result){

        }

    }

}
