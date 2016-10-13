package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.circular.admin.model.Estado;
import br.com.vostre.circular.admin.model.Pais;

/**
 * Created by Almir on 14/04/2014.
 */
public class EstadoDBAdapter {

    private SQLiteDatabase database;
    private EstadoDBHelper estadoDBHelper;
    private Context context;

    public EstadoDBAdapter(Context context, SQLiteDatabase database){
        estadoDBHelper = new EstadoDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public long cadastrarEstado(Estado estado){
        ContentValues cv = new ContentValues();

        if(estado.getId() > 0){
            cv.put("_id", estado.getId());
        }

        cv.put("id_remoto", estado.getIdRemoto());
        cv.put("nome", estado.getNome());
        cv.put("sigla", estado.getSigla());
        cv.put("status", estado.getStatus());
        cv.put("pais", estado.getPais().getIdRemoto());
        long insertId = database.insert("estado", null, cv);

        database.close();

        return insertId;
    }

    public long salvarOuAtualizar(Estado estado){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(estado.getId() > 0){
            cv.put("_id", estado.getId());
        }

        cv.put("id_remoto", estado.getIdRemoto());
        cv.put("nome", estado.getNome());
        cv.put("sigla", estado.getSigla());
        cv.put("status", estado.getStatus());
        cv.put("id_pais", estado.getPais().getIdRemoto());

        if(database.update("estado", cv,  "id_remoto = "+estado.getIdRemoto(), null) < 1){
            retorno = database.insert("estado", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarEstado(Estado estado){
        int retorno = database.delete("estado", "_id = "+estado.getId(), null);
        database.close();
        return retorno;
    }

    public int deletarInativos(){
        int retorno = database.delete("estado", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<Estado> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, sigla, status, id_pais, id_remoto FROM estado", null);
        PaisDBHelper paisDBHelper = new PaisDBHelper(context);
        List<Estado> estados = new ArrayList<Estado>();

        if(cursor.moveToFirst()){
            do{
                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(0));
                umEstado.setNome(cursor.getString(1));
                umEstado.setSigla(cursor.getString(2));
                umEstado.setStatus(cursor.getInt(3));
                Pais umPais = new Pais();
                umPais.setId(cursor.getInt(4));
                umPais = paisDBHelper.carregar(context, umPais);

                umEstado.setPais(umPais);
                umEstado.setIdRemoto(cursor.getInt(5));
               estados.add(umEstado);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return estados;
    }

    public List<Estado> listarTodosComVinculo(){
        Cursor cursor = database.rawQuery("SELECT DISTINCT e._id, e.nome, e.sigla, e.status, e.id_pais, e.id_remoto " +
                        "FROM estado e INNER JOIN " +
                        "local l ON l.id_estado = e._id INNER JOIN " +
                        "bairro bp ON bp.id_local = l._id INNER JOIN " +
                        "bairro bd ON bd.id_local = l._id INNER JOIN " +
                        "itinerario i ON i.id_partida = bp._id OR " +
                        "id_destino = bp._id OR " +
                        "i.id_partida = bd._id OR " +
                        "id_destino = bd._id", null);
        PaisDBHelper paisDBHelper = new PaisDBHelper(context);
        List<Estado> estados = new ArrayList<Estado>();

        if(cursor.moveToFirst()){
            do{
                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(0));
                umEstado.setNome(cursor.getString(1));
                umEstado.setSigla(cursor.getString(2));
                umEstado.setStatus(cursor.getInt(3));
                Pais umPais = new Pais();
                umPais.setId(cursor.getInt(4));
                umPais = paisDBHelper.carregar(context, umPais);

                umEstado.setPais(umPais);
                umEstado.setIdRemoto(cursor.getInt(5));
                estados.add(umEstado);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return estados;
    }

    public Estado carregar(Estado estado){
        Cursor cursor = database.rawQuery("SELECT _id, nome, sigla, status, id_pais, id_remoto FROM estado " +
                "WHERE id_remoto = ?", new String[]{String.valueOf(estado.getIdRemoto())});
        PaisDBHelper paisDBHelper = new PaisDBHelper(context);

        Estado umEstado = null;

        if(cursor.moveToFirst()){
            do{
                umEstado = new Estado();
                umEstado.setId(cursor.getInt(0));
                umEstado.setNome(cursor.getString(1));
                umEstado.setSigla(cursor.getString(2));
                umEstado.setStatus(cursor.getInt(3));

                Pais umPais = new Pais();
                umPais.setId(cursor.getInt(4));
                umPais = paisDBHelper.carregar(context, umPais);

                umEstado.setPais(umPais);
                umEstado.setIdRemoto(cursor.getInt(5));
                //paises.add(umPais);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umEstado;
    }

}
