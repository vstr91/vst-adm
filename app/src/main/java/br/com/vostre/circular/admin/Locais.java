package br.com.vostre.circular.admin;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import br.com.vostre.circular.admin.adapter.Lista;
import br.com.vostre.circular.admin.model.Estado;
import br.com.vostre.circular.admin.model.Local;
import br.com.vostre.circular.admin.model.dao.EstadoDBHelper;
import br.com.vostre.circular.admin.model.dao.LocalDBHelper;

public class Locais extends BaseActivity {

    ListView listViewLocais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);

        listViewLocais = (ListView) findViewById(R.id.listViewPaises);
        LocalDBHelper localDBHelper = new LocalDBHelper(getApplicationContext());

        List<Local> locais = localDBHelper.listarTodos(getApplicationContext());

        Lista adapter = new Lista(Locais.this, locais);
        listViewLocais.setAdapter(adapter);

    }
}
