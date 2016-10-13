package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.circular.admin.model.Bairro;
import br.com.vostre.circular.admin.model.Itinerario;
import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.model.ParadaItinerario;

/**
 * Created by Almir on 14/04/2014.
 */
public class ParadaItinerarioDBAdapter {

    private SQLiteDatabase database;
    private ParadaItinerarioDBHelper paradaItinerarioDBHelper;
    private Context context;

    public ParadaItinerarioDBAdapter(Context context, SQLiteDatabase database){
        paradaItinerarioDBHelper = new ParadaItinerarioDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(ParadaItinerario paradaItinerario){
        long retorno;
        ContentValues cv = new ContentValues();

        if(paradaItinerario.getId() > 0){
            cv.put("_id", paradaItinerario.getId());
        }

        cv.put("id_remoto", paradaItinerario.getIdRemoto());
        cv.put("id_parada", paradaItinerario.getParada().getId());
        cv.put("id_itinerario", paradaItinerario.getItinerario().getId());
        cv.put("ordem", paradaItinerario.getOrdem());
        cv.put("status", paradaItinerario.getStatus());
        cv.put("destaque", paradaItinerario.getDestaque());
        cv.put("valor", paradaItinerario.getValor());

        if(database.update("parada_itinerario", cv,  "id_remoto = "+paradaItinerario.getIdRemoto(), null) < 1){
            retorno = database.insert("parada_itinerario", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("parada_itinerario", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<ParadaItinerario> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, id_parada, id_itinerario, ordem, status, destaque, valor, id_remoto " +
                "FROM parada_itinerario", null);
        ParadaDBHelper paradaDBHelper = new ParadaDBHelper(context);
        ItinerarioDBHelper itinerarioDBHelper = new ItinerarioDBHelper(context);
        List<ParadaItinerario> paradasItinerarios = new ArrayList<ParadaItinerario>();

        if(cursor.moveToFirst()){
            do{
                ParadaItinerario umaParadaItinerario = new ParadaItinerario();
                umaParadaItinerario.setId(cursor.getInt(0));

                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(1));
                umaParada = paradaDBHelper.carregar(context, umaParada);

                umaParadaItinerario.setParada(umaParada);

                Itinerario umItinerario = new Itinerario();
                umItinerario.setId(cursor.getInt(2));
                umItinerario = itinerarioDBHelper.carregar(context, umItinerario);

                umaParadaItinerario.setItinerario(umItinerario);

                umaParadaItinerario.setOrdem(cursor.getInt(3));
                umaParadaItinerario.setStatus(cursor.getInt(4));
                umaParadaItinerario.setDestaque(cursor.getInt(5));
                umaParadaItinerario.setValor(cursor.getDouble(6));
                umaParadaItinerario.setIdRemoto(cursor.getInt(7));

               paradasItinerarios.add(umaParadaItinerario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradasItinerarios;
    }

    public List<ParadaItinerario> listaParadasDestaquePorItinerario(Bairro partida, Bairro destino){
        Cursor cursor = database.rawQuery("SELECT pit._id, pit.id_parada, pit.id_itinerario, pit.ordem, pit.status, pit.destaque, valor, id_remoto " +
                "FROM parada_itinerario pit INNER JOIN itinerario i ON i._id = pit.id_itinerario AND i.id_partida = ? AND i.id_destino = ? " +
                        "WHERE pit.destaque = -1",
                new String[]{String.valueOf(partida.getId()), String.valueOf(destino.getId())});
        ParadaDBHelper paradaDBHelper = new ParadaDBHelper(context);
        ItinerarioDBHelper itinerarioDBHelper = new ItinerarioDBHelper(context);
        List<ParadaItinerario> paradasItinerarios = new ArrayList<ParadaItinerario>();

        if(cursor.moveToFirst()){
            do{
                ParadaItinerario umaParadaItinerario = new ParadaItinerario();
                umaParadaItinerario.setId(cursor.getInt(0));

                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(1));
                umaParada = paradaDBHelper.carregar(context, umaParada);

                umaParadaItinerario.setParada(umaParada);

                Itinerario umItinerario = new Itinerario();
                umItinerario.setId(cursor.getInt(2));
                umItinerario = itinerarioDBHelper.carregar(context, umItinerario);

                umaParadaItinerario.setItinerario(umItinerario);

                umaParadaItinerario.setOrdem(cursor.getInt(3));
                umaParadaItinerario.setStatus(cursor.getInt(4));
                umaParadaItinerario.setDestaque(cursor.getInt(5));
                umaParadaItinerario.setValor(cursor.getDouble(6));
                umaParadaItinerario.setIdRemoto(cursor.getInt(7));

                paradasItinerarios.add(umaParadaItinerario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradasItinerarios;
    }

    public List<ParadaItinerario> listaParadasDestaquePorItinerario(Itinerario itinerario){

        Cursor cursor = database.rawQuery("SELECT pit._id, pit.id_parada, pit.id_itinerario, pit.ordem, pit.status, pit.destaque, pit.valor, pit.id_remoto " +
                "FROM parada_itinerario pit INNER JOIN itinerario i ON i._id = pit.id_itinerario AND i._id = ?" +
                        "WHERE pit.destaque = -1",
                new String[]{String.valueOf(itinerario.getId())});
        ParadaDBHelper paradaDBHelper = new ParadaDBHelper(context);
        ItinerarioDBHelper itinerarioDBHelper = new ItinerarioDBHelper(context);
        List<ParadaItinerario> paradasItinerarios = new ArrayList<ParadaItinerario>();

        if(cursor.moveToFirst()){
            do{
                ParadaItinerario umaParadaItinerario = new ParadaItinerario();
                umaParadaItinerario.setId(cursor.getInt(0));

                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(1));
                umaParada = paradaDBHelper.carregar(context, umaParada);

                umaParadaItinerario.setParada(umaParada);

                Itinerario umItinerario = new Itinerario();
                umItinerario.setId(cursor.getInt(2));
                umItinerario = itinerarioDBHelper.carregar(context, umItinerario);

                umaParadaItinerario.setItinerario(umItinerario);

                umaParadaItinerario.setOrdem(cursor.getInt(3));
                umaParadaItinerario.setStatus(cursor.getInt(4));
                umaParadaItinerario.setDestaque(cursor.getInt(5));
                umaParadaItinerario.setValor(cursor.getDouble(6));
                umaParadaItinerario.setIdRemoto(cursor.getInt(7));

                paradasItinerarios.add(umaParadaItinerario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradasItinerarios;
    }

    public ParadaItinerario carregar(ParadaItinerario paradaItinerario){
        Cursor cursor = database.rawQuery("SELECT _id, id_parada, id_itinerario, ordem, status, destaque, valor, id_remoto " +
                "FROM parada_itinerario WHERE _id = ?", new String[]{String.valueOf(paradaItinerario.getId())});
        ParadaDBHelper paradaDBHelper = new ParadaDBHelper(context);
        ItinerarioDBHelper itinerarioDBHelper = new ItinerarioDBHelper(context);

        ParadaItinerario umaParadaItinerario = null;

        if(cursor.moveToFirst()){
            do{
                umaParadaItinerario = new ParadaItinerario();
                umaParadaItinerario.setId(cursor.getInt(0));

                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(1));
                umaParada = paradaDBHelper.carregar(context, umaParada);

                umaParadaItinerario.setParada(umaParada);

                Itinerario umItinerario = new Itinerario();
                umItinerario.setId(cursor.getInt(2));
                umItinerario = itinerarioDBHelper.carregar(context, umItinerario);

                umaParadaItinerario.setItinerario(umItinerario);

                umaParadaItinerario.setOrdem(cursor.getInt(3));
                umaParadaItinerario.setStatus(cursor.getInt(4));
                umaParadaItinerario.setDestaque(cursor.getInt(5));
                umaParadaItinerario.setValor(cursor.getDouble(6));
                umaParadaItinerario.setIdRemoto(cursor.getInt(7));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umaParadaItinerario;
    }

    public Parada carregarParadaEmbarque(Itinerario itinerario){
        Cursor cursor = database.rawQuery("SELECT _id, id_parada, id_itinerario, ordem, status, destaque, valor, id_remoto FROM parada_itinerario" +
                " WHERE id_itinerario = ? AND ordem = 1", new String[]{String.valueOf(itinerario.getId())});
        ParadaDBHelper paradaDBHelper = new ParadaDBHelper(context);
        ItinerarioDBHelper itinerarioDBHelper = new ItinerarioDBHelper(context);

        ParadaItinerario umaParadaItinerario = null;

        if(cursor.moveToFirst()){
            do{
                umaParadaItinerario = new ParadaItinerario();
                umaParadaItinerario.setId(cursor.getInt(0));

                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(1));
                umaParada = paradaDBHelper.carregar(context, umaParada);

                umaParadaItinerario.setParada(umaParada);

                Itinerario umItinerario = new Itinerario();
                umItinerario.setId(cursor.getInt(2));
                umItinerario = itinerarioDBHelper.carregar(context, umItinerario);

                umaParadaItinerario.setItinerario(umItinerario);

                umaParadaItinerario.setOrdem(cursor.getInt(3));
                umaParadaItinerario.setStatus(cursor.getInt(4));
                umaParadaItinerario.setDestaque(cursor.getInt(5));
                umaParadaItinerario.setValor(cursor.getDouble(6));
                umaParadaItinerario.setIdRemoto(cursor.getInt(7));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umaParadaItinerario.getParada();
    }

}
