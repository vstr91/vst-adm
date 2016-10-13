package br.com.vostre.circular.admin.utils.service;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import br.com.vostre.circular.admin.MainActivity;
import br.com.vostre.circular.admin.R;
import br.com.vostre.circular.admin.model.dao.ParadaDBHelper;
import br.com.vostre.circular.admin.model.dao.ParametroDBHelper;
import br.com.vostre.circular.admin.utils.Constants;
import br.com.vostre.circular.admin.utils.Crypt;
import br.com.vostre.circular.admin.utils.HttpUtils;
import br.com.vostre.circular.admin.utils.enums.TipoToken;
import br.com.vostre.circular.admin.utils.listener.BackGroudTaskListener;
import br.com.vostre.circular.admin.utils.listener.MessageTaskListener;
import br.com.vostre.circular.admin.utils.listener.ServerUtilsListener;
import br.com.vostre.circular.admin.utils.listener.TokenTaskListener;
import br.com.vostre.circular.admin.utils.listener.UpdateMessageTaskListener;
import br.com.vostre.circular.admin.utils.listener.UpdateTaskListener;
import br.com.vostre.circular.admin.utils.task.BackGroundTask;
import br.com.vostre.circular.admin.utils.task.ServerUtils;
import br.com.vostre.circular.admin.utils.task.TokenTask;
import br.com.vostre.circular.admin.utils.task.UpdateTask;

public class RecebeDadosService extends Service implements ServerUtilsListener, TokenTaskListener,
        BackGroudTaskListener, UpdateTaskListener {

    //boolean exists = false;
    Map<String, Object> map = null;
    BackGroundTask bgt = null;
    JSONObject jObj = null;
    Context ctx = null;
    String dataUltimoAcesso = null;

    public RecebeDadosService() {
    }

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
                boolean servidorDisponivel = false;

                testarDisponibilidadeServidor(Constants.SERVIDOR_TESTE, 80);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, TimeUnit.MINUTES.toMillis(Constants.TEMPO_RECEBIMENTO_ATUALIZACAO));

    }

    public void testarDisponibilidadeServidor(String ip, int porta){

        ServerUtils serverUtils = new ServerUtils(getApplicationContext(), true);
        serverUtils.setOnResultsListener(this);

        serverUtils.execute(new String[]{ip, String.valueOf(porta)});

    }

    public static String getDataUltimoAcesso(Context context){
        SharedPreferences sp = context.getSharedPreferences("br.com.vostre.circular", Context.MODE_PRIVATE);
        DateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        DateFormat dateFormatWeb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);

        String ultimaData = parametroDBHelper.carregarUltimoAcesso(context);

        try {

            if(!ultimaData.equals("-")){
                data = dateFormat.parse(ultimaData.replace(",", "").replace("%20", " "));
            }

        } catch(ParseException ex){
            ex.printStackTrace();
        }

        if(null != data){
            return dateFormatWeb.format(data).replace(" ", "%20");
        } else{
            return "-";
        }

    }

    public void setDataUltimoAcesso(Context context, String dataUltimoAcesso){
        ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);
        parametroDBHelper.gravarUltimoAcesso(context, dataUltimoAcesso);
    }

    private AlertDialog criaAlert(String titulo, String mensagem, boolean botaoOk){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensagem);
        alertDialog.setCancelable(false);

        if(botaoOk){
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }

        return alertDialog.create();
    }

    @Override
    public void onServerUtilsResultsSucceeded(boolean result) {

        if(result){

            String urlToken = Constants.URLTOKEN;

            TokenTask tokenTask = new TokenTask(urlToken, getApplicationContext(), true, TipoToken.DADOS.getTipo());
            tokenTask.setOnTokenTaskResultsListener(this);
            tokenTask.execute();

        } else{

            AlertDialog alertDialog = criaAlert("Servidor Indisponível", "Não foi possível se conectar ao servidor. " +
                    "Por favor, verifique sua conexão com a Internet e tente novamente.", true);
            alertDialog.show();

        }

    }

    @Override
    public void onTokenTaskResultsSucceeded(String token) {
        Crypt crypt = new Crypt();

        String tokenCriptografado = null;
        try {
            tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));

            dataUltimoAcesso = getDataUltimoAcesso(this.getBaseContext());
            dataUltimoAcesso = dataUltimoAcesso.equals("") ? "-" : dataUltimoAcesso;

            String url = Constants.URLSERVIDOR+tokenCriptografado+"/"+dataUltimoAcesso;
            //String url = Constants.URLSERVIDOR+tokenCriptografado+"/-";

            ctx = getApplicationContext();

            bgt = new BackGroundTask(url, "GET", getApplicationContext(), true);
            bgt.setOnResultsListener(this);
            bgt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateTaskResultsSucceeded(boolean result) {
        boolean sucesso = result;

        if(sucesso){
            setDataUltimoAcesso(this.getBaseContext(), dataUltimoAcesso);
            this.stopSelf();
        }

    }

    @Override
    public void onBackGroundTaskResultsSucceeded(Map<String, Object> result) {
        map = result;

        jObj = (JSONObject) map.get("json");
        dataUltimoAcesso = (String) map.get("data");

        int registros = 0;
        int status = 0;

        if(jObj != null){
            try {
                JSONArray metadados = jObj.getJSONArray("meta");
                JSONObject objMetadados = metadados.getJSONObject(0);

                registros = objMetadados.getInt("registros");
                status = objMetadados.getInt("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(registros > 0){
                UpdateTask updateTask = new UpdateTask(jObj, ctx);
                updateTask.setOnResultsListener(this);
                updateTask.execute();
            } else{
                setDataUltimoAcesso(this.getBaseContext(), dataUltimoAcesso);
            }

        } else{
            AlertDialog alertDialog = criaAlert("Erro ao Receber Dados", "Não foi possível receber os dados com sucesso. " +
                    "Por favor tente novamente.", true);
            alertDialog.show();
        }
    }
}
