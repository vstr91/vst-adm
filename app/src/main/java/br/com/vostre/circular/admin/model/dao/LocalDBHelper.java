package br.com.vostre.circular.admin.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.circular.admin.model.Estado;
import br.com.vostre.circular.admin.model.Local;

/**
 * Created by Almir on 14/04/2014.
 */
public class LocalDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = CircularDBHelper.DBVERSION;
    private static final String DBNAME = CircularDBHelper.DBNAME;

    public static final String DBCREATE = "CREATE TABLE local( _id integer primary key, nome text NOT NULL, id_estado integer NOT NULL, " +
            "id_cidade integer NOT NULL, status integer NOT NULL, id_remoto integer);";
    CircularDBHelper circularDBHelper;

    public LocalDBHelper(Context context){
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
        Log.w(LocalDBHelper.class.getName(), "Atualização da Versão "+oldVersion+" para a Versão "+newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Local> listarTodos(Context context){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Local> listarTodosPorEstado(Context context, Estado estado){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodosPorEstado(estado);
    }

    public List<Local> listarTodosPorEstadoEItinerario(Context context, Estado estado){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodosPorEstadoEItinerario(estado);
    }

    public List<Local> listarTodosVinculados(Context context){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodosVinculados();
    }

    public long salvarOuAtualizar(Context context, Local local){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(local);
    }

    public long deletar(Context context, Local local){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.deletarLocal(local);
    }

    public long deletarInativos(Context context){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Local carregar(Context context, Local local){
        LocalDBAdapter adapter = new LocalDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.carregar(local);
    }

}
