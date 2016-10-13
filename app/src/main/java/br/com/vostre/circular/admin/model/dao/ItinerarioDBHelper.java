package br.com.vostre.circular.admin.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.circular.admin.model.Bairro;
import br.com.vostre.circular.admin.model.HorarioItinerario;
import br.com.vostre.circular.admin.model.Itinerario;
import br.com.vostre.circular.admin.model.Parada;

/**
 * Created by Almir on 14/04/2014.
 */
public class ItinerarioDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = CircularDBHelper.DBVERSION;
    private static final String DBNAME = CircularDBHelper.DBNAME;
    public static final String TABELA = "itinerario";
    public static final String ID = "_id";
    public static final String PARTIDA = "id_partida";
    public static final String DESTINO = "id_destino";
    public static final String EMPRESA = "id_empresa";
    public static final String OBSERVACAO = "observacao";
    public static final String VALOR = "valor";
    public static final String STATUS = "status";
    public static final String DBCREATE = "CREATE TABLE itinerario( _id integer primary key, id_partida integer NOT NULL, id_destino integer NOT NULL, " +
            "id_empresa integer NOT NULL, observacao text, valor real, status integer NOT NULL, id_remoto integer);";
    CircularDBHelper circularDBHelper;

    public ItinerarioDBHelper(Context context){
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
        Log.w(ItinerarioDBHelper.class.getName(), "Atualização da Versão "+oldVersion+" para a Versão "+newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Itinerario> listarTodos(Context context){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<HorarioItinerario> listarTodosPorParada(Context context, Parada parada, String hora){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarTodosPorParada(parada, hora);
    }

    public long salvarOuAtualizar(Context context, Itinerario itinerario){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(itinerario);
    }

    public long deletarInativos(Context context){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Itinerario carregar(Context context, Itinerario itinerario){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.carregar(itinerario);
    }

    public Itinerario carregarPorPartidaEDestino(Context context, Itinerario itinerario){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.carregarPorPartidaEDestino(itinerario);
    }

    public List<HorarioItinerario> listarOutrasOpcoesItinerario(Context context, Itinerario itinerario, String hora, int dia){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getReadableDatabase());
        return adapter.listarOutrasOpcoesItinerario(itinerario, hora, dia);
    }

    public Itinerario checarReverso(Context context, Bairro partida, Bairro destino){
        ItinerarioDBAdapter adapter = new ItinerarioDBAdapter(context, circularDBHelper.getWritableDatabase());
        return adapter.checarReverso(partida, destino);
    }

}
