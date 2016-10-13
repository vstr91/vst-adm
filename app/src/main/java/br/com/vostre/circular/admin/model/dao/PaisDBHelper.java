package br.com.vostre.circular.admin.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.circular.admin.model.Pais;

/**
 * Created by Almir on 14/04/2014.
 */
public class PaisDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = CircularDBHelper.DBVERSION;
    private static final String DBNAME = CircularDBHelper.DBNAME;

    public static final String DBCREATE = "CREATE TABLE pais( _id integer primary key, nome text NOT NULL, iso3 text NOT NULL, status integer NOT NULL, " +
            "id_remoto integer);";
    CircularDBHelper circularDBHelper;

    public PaisDBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
        circularDBHelper = new CircularDBHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
     //db.execSQL(DBCREATE);
     //db.execSQL("INSERT INTO pais (nome, iso3, status) VALUES ('Brasil', 'BRA', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(PaisDBHelper.class.getName(), "Atualização da Versão "+oldVersion+" para a Versão "+newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Pais> listarTodos(Context context){
        PaisDBAdapter adapter = new PaisDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public long salvar(Context context, Pais pais){
        PaisDBAdapter adapter = new PaisDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.cadastrarPais(pais);
    }

    public long salvarOuAtualizar(Context context, Pais pais){
        PaisDBAdapter adapter = new PaisDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(pais);
    }

    public long deletar(Context context, Pais pais){
        PaisDBAdapter adapter = new PaisDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.deletarPais(pais);
    }

    public long deletarInativos(Context context){
        PaisDBAdapter adapter = new PaisDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Pais carregar(Context context, Pais pais){
        PaisDBAdapter adapter = new PaisDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.carregar(pais);
    }

}
