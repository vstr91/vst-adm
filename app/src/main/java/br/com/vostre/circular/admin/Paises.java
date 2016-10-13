package br.com.vostre.circular.admin;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.vostre.circular.admin.adapter.Lista;
import br.com.vostre.circular.admin.model.ClasseBase;
import br.com.vostre.circular.admin.model.Pais;
import br.com.vostre.circular.admin.model.dao.PaisDBHelper;
import br.com.vostre.circular.admin.utils.SnackbarHelper;
import br.com.vostre.circular.admin.utils.listener.DialogDismissListener;
import br.com.vostre.circular.admin.utils.modal.CadastraPaisDialog;

public class Paises extends BaseActivity implements DialogDismissListener {

    ListView listViewPaises;
    Lista adapter;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);

        listViewPaises = (ListView) findViewById(R.id.listViewPaises);
        rootView = (View) findViewById(R.id.root);
        PaisDBHelper paisDBHelper = new PaisDBHelper(getApplicationContext());

        List<Pais> paises = paisDBHelper.listarTodos(getApplicationContext());

        adapter = new Lista(Paises.this, paises);
        listViewPaises.setAdapter(adapter);

        registerForContextMenu(listViewPaises);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, 0, Menu.NONE, "Editar");
        menu.add(Menu.NONE, 1, Menu.NONE, "Excluir");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Pais pais = (Pais) adapter.getItem(info.position);

        switch(item.getItemId()){
                case 0:

                CadastraPaisDialog esmalteDialog = new CadastraPaisDialog();
                esmalteDialog.setDialogDismissListener(this);
                esmalteDialog.setPais(pais);
                esmalteDialog.show(getSupportFragmentManager(), "esmalteDialog");
                    break;
                case 1:

                    if(pais.getStatus() != 3){
                        Toast.makeText(getApplicationContext(), "Registro já se encontra no servidor e não pode mais ser excluído.", Toast.LENGTH_LONG).show();
                    } else{
                        new AlertDialog.Builder(this)
                                .setTitle("Confirmar Exclusão")
                                .setMessage("Deseja realmente excluir o esmalte?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PaisDBHelper paisDBHelper = new PaisDBHelper(getBaseContext());
                                        paisDBHelper.deletar(getBaseContext(), pais);
                                        adapter.remove(pais);
                                        adapter.notifyDataSetChanged();
                                        SnackbarHelper.notifica(rootView, "País deletado com sucesso.", Snackbar.LENGTH_LONG);
                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                    break;
            }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDialogDismiss(long result, Object obj) {

    }
}
