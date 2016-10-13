package br.com.vostre.circular.admin.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.circular.admin.model.Empresa;

/**
 * Created by Almir on 14/04/2014.
 */
public class EmpresaDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = CircularDBHelper.DBVERSION;
    private static final String DBNAME = CircularDBHelper.DBNAME;
    public static final String TABELA = "empresa";

    public static final String DBCREATE = "CREATE TABLE empresa( _id integer primary key, razao_social text NOT NULL, fantasia text NOT NULL, " +
            "cnpj text, site text, telefone text, status integer NOT NULL, id_remoto integer);";
    CircularDBHelper circularDBHelper;

    public EmpresaDBHelper(Context context){
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
        Log.w(EmpresaDBHelper.class.getName(), "Atualização da Versão "+oldVersion+" para a Versão "+newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Empresa> listarTodos(Context context){
        EmpresaDBAdapter adapter = new EmpresaDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public long salvarOuAtualizar(Context context, Empresa empresa){
        EmpresaDBAdapter adapter = new EmpresaDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(empresa);
    }

    public long deletarInativos(Context context){
        EmpresaDBAdapter adapter = new EmpresaDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Empresa carregar(Context context, Empresa empresa){
        EmpresaDBAdapter adapter = new EmpresaDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.carregar(empresa);
    }

}
