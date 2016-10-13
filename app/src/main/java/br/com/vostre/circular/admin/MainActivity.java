package br.com.vostre.circular.admin;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.com.vostre.circular.admin.model.Estado;
import br.com.vostre.circular.admin.utils.ServiceUtils;
import br.com.vostre.circular.admin.utils.service.RecebeDadosService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnItinerarios;
    Button btnParadas;
    Button btnMensagens;
    Button btnEmpresas;
    Button btnEstados;
    Button btnLocais;
    Button btnBairros;
    Button btnPaises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnItinerarios = (Button) findViewById(R.id.btnItinerarios);
        btnParadas = (Button) findViewById(R.id.btnParadas);
        btnMensagens = (Button) findViewById(R.id.btnMensagens);
        btnEmpresas = (Button) findViewById(R.id.btnEmpresas);
        btnEstados = (Button) findViewById(R.id.btnEstados);
        btnLocais = (Button) findViewById(R.id.btnLocais);
        btnBairros = (Button) findViewById(R.id.btnBairros);
        btnPaises = (Button) findViewById(R.id.btnPaises);

        btnItinerarios.setOnClickListener(this);
        btnParadas.setOnClickListener(this);
        btnMensagens.setOnClickListener(this);
        btnEmpresas.setOnClickListener(this);
        btnEstados.setOnClickListener(this);
        btnLocais.setOnClickListener(this);
        btnBairros.setOnClickListener(this);
        btnPaises.setOnClickListener(this);

        //iniciaServicoAtualizacao();

    }

    private void iniciaServicoAtualizacao(){

        final ServiceUtils serviceUtils = new ServiceUtils();
        Intent serviceIntent = new Intent(getBaseContext(), RecebeDadosService.class);

        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if(!serviceUtils.isMyServiceRunning(RecebeDadosService.class, manager)){
            stopService(serviceIntent);
            startService(serviceIntent);
        } else{
        }

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){
            case R.id.btnItinerarios:
                break;
            case R.id.btnParadas:
                intent = new Intent(this, Paradas.class);
                startActivity(intent);
                break;
            case R.id.btnMensagens:
                break;
            case R.id.btnEmpresas:
                break;
            case R.id.btnEstados:
                intent = new Intent(this, Estados.class);
                startActivity(intent);
                break;
            case R.id.btnLocais:
                intent = new Intent(this, Locais.class);
                startActivity(intent);
                break;
            case R.id.btnBairros:
                break;
            case R.id.btnPaises:
                intent = new Intent(this, Paises.class);
                startActivity(intent);
                break;
        }

    }
}
