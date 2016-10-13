package br.com.vostre.circular.admin;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import br.com.vostre.circular.admin.adapter.Lista;
import br.com.vostre.circular.admin.model.Estado;
import br.com.vostre.circular.admin.model.Pais;
import br.com.vostre.circular.admin.model.dao.EstadoDBHelper;
import br.com.vostre.circular.admin.model.dao.PaisDBHelper;

public class Estados extends BaseActivity {

    ListView listViewEstados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);

        listViewEstados = (ListView) findViewById(R.id.listViewPaises);
        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(getApplicationContext());

        List<Estado> estados = estadoDBHelper.listarTodos(getApplicationContext());

        Lista adapter = new Lista(Estados.this, estados);
        listViewEstados.setAdapter(adapter);

    }
}
