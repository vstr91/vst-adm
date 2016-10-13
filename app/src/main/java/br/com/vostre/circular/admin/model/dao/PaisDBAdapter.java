package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.circular.admin.model.Pais;

/**
 * Created by Almir on 14/04/2014.
 */
public class PaisDBAdapter {

    private SQLiteDatabase database;
    private PaisDBHelper paisDBHelper;

    public PaisDBAdapter(Context context, SQLiteDatabase database){
        paisDBHelper = new PaisDBHelper(context);
        this.database = database;
    }

    public long cadastrarPais(Pais pais){
        ContentValues cv = new ContentValues();
        cv.put("_id", pais.getId());
        cv.put("id_remoto", pais.getIdRemoto());
        cv.put("nome", pais.getNome());
        cv.put("iso3", pais.getIso3());
        cv.put("status", pais.getStatus());
        long insertId = database.insert("pais", null, cv);

        database.close();

        return insertId;
    }

    public long salvarOuAtualizar(Pais pais){
        long retorno;
        ContentValues cv = new ContentValues();

        if(pais.getId() > 0){
            cv.put("_id", pais.getId());
        }

        cv.put("id_remoto", pais.getIdRemoto());
        cv.put("nome", pais.getNome());
        cv.put("iso3", pais.getIso3());
        cv.put("status", pais.getStatus());

        if(database.update("pais", cv, "id_remoto = "+pais.getIdRemoto(), null) < 1){
            retorno = database.insert("pais", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarPais(Pais pais){
        int retorno = database.delete("pais", "_id = "+pais.getId(), null);
        database.close();
        return retorno;
    }

    public int deletarInativos(){
        int retorno = database.delete("pais", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<Pais> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, iso3, status, id_remoto FROM pais", null);

        List<Pais> paises = new ArrayList<Pais>();

        if(cursor.moveToFirst()){
            do{
                Pais umPais = new Pais();
                umPais.setId(cursor.getInt(0));
                umPais.setNome(cursor.getString(1));
                umPais.setIso3(cursor.getString(2));
                umPais.setStatus(cursor.getInt(3));
                umPais.setIdRemoto(cursor.getInt(4));
               paises.add(umPais);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paises;
    }

    public Pais carregar(Pais pais){
        Cursor cursor = database.rawQuery("SELECT _id, nome, iso3, status, id_remoto FROM pais WHERE id_remoto = ?", new String[]{String.valueOf(pais.getIdRemoto())});

        List<Pais> paises = new ArrayList<Pais>();
        Pais umPais = null;

        if(cursor.moveToFirst()){
            do{
                umPais = new Pais();
                umPais.setId(cursor.getInt(0));
                umPais.setNome(cursor.getString(1));
                umPais.setIso3(cursor.getString(2));
                umPais.setStatus(cursor.getInt(3));
                umPais.setIdRemoto(cursor.getInt(4));
                //paises.add(umPais);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umPais;
    }

}
