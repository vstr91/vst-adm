package br.com.vostre.circular.admin.utils.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import br.com.vostre.circular.admin.utils.Crypt;
import br.com.vostre.circular.admin.utils.HttpUtils;
import br.com.vostre.circular.admin.utils.listener.TokenTaskListener;

/**
 * Created by Almir on 14/01/2015.
 */
public class TokenTask extends AsyncTask<String, String, String> {

    String url = null;
    Context context = null;
    ProgressDialog progressDialog;
    static InputStream is = null;
    TokenTaskListener listener;
    boolean isBackground;
    String json = null;

    public TokenTask(String url, Context context, boolean isBackground, String tipo){
        Crypt crypt = new Crypt();

        this.url = url+"?tipo="+tipo;
        this.context = context;
        this.isBackground = isBackground;
    }

    public void setOnTokenTaskResultsListener(TokenTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

        if(!isBackground){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Solicitando Permissão de Acesso...");
            progressDialog.setCancelable(false);
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }

    }

    @Override
    protected void onPostExecute(String response) {

        if(!isBackground){
            if(null != progressDialog){
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

        listener.onTokenTaskResultsSucceeded(response);

    }

    @Override
    protected String doInBackground(String... strings) {

        try{
            HttpURLConnection conn = HttpUtils.sendGetRequest(url);

            String[] resposta = HttpUtils.readMultipleLinesRespone();

            HttpUtils.disconnect();

            json = resposta[0];

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(null != json){
            return json;
        } else{
            return null;
        }

    }

}
