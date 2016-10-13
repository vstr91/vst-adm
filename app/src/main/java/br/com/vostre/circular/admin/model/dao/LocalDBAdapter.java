package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.circular.admin.model.Estado;
import br.com.vostre.circular.admin.model.Local;

/**
 * Created by Almir on 14/04/2014.
 */
public class LocalDBAdapter {

    private SQLiteDatabase database;
    private LocalDBHelper localDBHelper;
    private Context context;

    public LocalDBAdapter(Context context, SQLiteDatabase database){
        localDBHelper = new LocalDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Local local){
        long retorno;
        ContentValues cv = new ContentValues();

        if(local.getId() > 0){
            cv.put("_id", local.getId());
        }

        cv.put("nome", local.getNome());
        cv.put("id_estado", local.getEstado().getId());
        cv.put("id_cidade", local.getCidade().getId());
        cv.put("status", local.getStatus());

        if(database.update("local", cv,  "id_remoto = "+local.getIdRemoto(), null) < 1){
            retorno = database.insert("local", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarLocal(Local local){
        int retorno = database.delete("local", "_id = "+local.getId(), null);
        return retorno;
    }

    public int deletarInativos(){
        int retorno = database.delete("local", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<Local> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, id_estado, id_cidade, status, id_remoto FROM local ORDER BY nome", null);
        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(context);
        List<Local> locais = new ArrayList<Local>();

        if(cursor.moveToFirst()){
            do{
                Local umLocal = new Local();
                umLocal.setId(cursor.getInt(0));
                umLocal.setNome(cursor.getString(1));

                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(2));
                umEstado = estadoDBHelper.carregar(context, umEstado);
                umLocal.setEstado(umEstado);

                if(umLocal.getId() != cursor.getInt(3)){
                    LocalDBHelper localDBHelper = new LocalDBHelper(context);
                    Local umaCidade = new Local();
                    umaCidade.setId(cursor.getInt(3));
                    umaCidade = localDBHelper.carregar(context, umaCidade);
                }

                umLocal.setStatus(cursor.getInt(4));
                umLocal.setIdRemoto(cursor.getInt(5));

               locais.add(umLocal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return locais;
    }

    public List<Local> listarTodosPorEstado(Estado estado){
        Cursor cursor = database.rawQuery("SELECT _id, nome, id_estado, id_cidade, status, id_remoto FROM local WHERE id_estado = ? ORDER BY nome", new String[]{String.valueOf(estado.getId())});
        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(context);
        List<Local> locais = new ArrayList<Local>();

        if(cursor.moveToFirst()){
            do{
                Local umLocal = new Local();
                umLocal.setId(cursor.getInt(0));
                umLocal.setNome(cursor.getString(1));

                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(2));
                umEstado = estadoDBHelper.carregar(context, umEstado);
                umLocal.setEstado(umEstado);

                if(umLocal.getId() != cursor.getInt(3)){
                    LocalDBHelper localDBHelper = new LocalDBHelper(context);
                    Local umaCidade = new Local();
                    umaCidade.setId(cursor.getInt(3));
                    umaCidade = localDBHelper.carregar(context, umaCidade);
                    umLocal.setCidade(umaCidade);
                }

                umLocal.setStatus(cursor.getInt(4));
                umLocal.setIdRemoto(cursor.getInt(5));

                locais.add(umLocal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return locais;
    }

    public List<Local> listarTodosPorEstadoEItinerario(Estado estado){
        Cursor cursor = database.rawQuery("SELECT DISTINCT l._id, l.nome, l.id_estado, l.id_cidade, l.status, l.id_remoto" +
                " FROM itinerario i LEFT JOIN" +
                " bairro b ON b._id = i.id_partida LEFT JOIN" +
                " local l ON l._id = b.id_local INNER JOIN" +
                " horario_itinerario hi ON hi.id_itinerario = i._id "
                +" WHERE id_estado = ? ORDER BY l.nome", new String[]{String.valueOf(estado.getId())});

        /*Cursor cursor = database.rawQuery("SELECT DISTINCT l._id, l.nome, l.id_estado, l.id_cidade, l.status" +
                " FROM itinerario i LEFT JOIN" +
                " parada_itinerario pit ON pit.id_itinerario = i._id LEFT JOIN " +
                "parada p ON p._id = pit.id_parada LEFT JOIN" +
                " bairro b ON b._id = i.id_partida OR (b._id = p.id_bairro AND pit.destaque = -1) LEFT JOIN" +
                " local l ON l._id = b.id_local INNER JOIN" +
                " horario_itinerario hi ON hi.id_itinerario = i._id "
                +" WHERE id_estado = ? ORDER BY l.nome", new String[]{String.valueOf(estado.getId())});*/

        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(context);
        List<Local> locais = new ArrayList<Local>();

        if(cursor.moveToFirst()){
            do{
                Local umLocal = new Local();
                umLocal.setId(cursor.getInt(0));
                umLocal.setNome(cursor.getString(1));

                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(2));
                umEstado = estadoDBHelper.carregar(context, umEstado);
                umLocal.setEstado(umEstado);

                if(umLocal.getId() != cursor.getInt(3)){
                    LocalDBHelper localDBHelper = new LocalDBHelper(context);
                    Local umaCidade = new Local();
                    umaCidade.setId(cursor.getInt(3));
                    umaCidade = localDBHelper.carregar(context, umaCidade);
                }

                umLocal.setStatus(cursor.getInt(4));
                umLocal.setIdRemoto(cursor.getInt(5));

                locais.add(umLocal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return locais;
    }

    public List<Local> listarTodosVinculados(){
        Cursor cursor = database.rawQuery("SELECT DISTINCT l._id, l.nome, l.id_estado, l.id_cidade, l.status, l.id_remoto" +
                " FROM itinerario i LEFT JOIN" +
                " bairro b ON b._id = i.id_partida LEFT JOIN" +
                " local l ON l._id = b.id_local INNER JOIN" +
                " horario_itinerario hi ON hi.id_itinerario = i._id ORDER BY l.nome", null);

        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(context);
        List<Local> locais = new ArrayList<Local>();

        if(cursor.moveToFirst()){
            do{
                Local umLocal = new Local();
                umLocal.setId(cursor.getInt(0));
                umLocal.setNome(cursor.getString(1));

                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(2));
                umEstado = estadoDBHelper.carregar(context, umEstado);
                umLocal.setEstado(umEstado);

                if(umLocal.getId() != cursor.getInt(3)){
                    LocalDBHelper localDBHelper = new LocalDBHelper(context);
                    Local umaCidade = new Local();
                    umaCidade.setId(cursor.getInt(3));
                    umaCidade = localDBHelper.carregar(context, umaCidade);
                    umLocal.setCidade(umaCidade);
                }

                umLocal.setStatus(cursor.getInt(4));
                umLocal.setIdRemoto(cursor.getInt(5));

                locais.add(umLocal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return locais;
    }

    public Local carregar(Local local){
        Cursor cursor = database.rawQuery("SELECT _id, nome, id_estado, id_cidade, status, id_remoto FROM local WHERE _id = ?", new String[]{String.valueOf(local.getId())});
        PaisDBHelper paisDBHelper = new PaisDBHelper(context);
        EstadoDBHelper estadoDBHelper = new EstadoDBHelper(context);

        Local umLocal = null;

        if(cursor.moveToFirst()){
            do{
                umLocal = new Local();
                umLocal.setId(cursor.getInt(0));
                umLocal.setNome(cursor.getString(1));

                Estado umEstado = new Estado();
                umEstado.setId(cursor.getInt(2));
                umEstado = estadoDBHelper.carregar(context, umEstado);
                umLocal.setEstado(umEstado);

                int idLocal = umLocal.getId();
                int cidade = cursor.getInt(3);

                if(idLocal != cidade){
                    Local umaCidade = new Local();
                    umaCidade.setId(cursor.getInt(3));
                    LocalDBHelper localDBHelper = new LocalDBHelper(context);
                    umaCidade = localDBHelper.carregar(context, umaCidade);
                    umLocal.setCidade(umaCidade);
                }

                umLocal.setStatus(cursor.getInt(4));
                umLocal.setIdRemoto(cursor.getInt(5));
                //paises.add(umPais);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umLocal;
    }

}
