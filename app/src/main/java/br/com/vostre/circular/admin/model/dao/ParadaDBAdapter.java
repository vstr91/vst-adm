package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.vostre.circular.admin.model.Bairro;
import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.utils.DateUtils;

/**
 * Created by Almir on 14/04/2014.
 */
public class ParadaDBAdapter {

    private SQLiteDatabase database;
    private ParadaDBHelper paradaDBHelper;
    private Context context;

    public ParadaDBAdapter(Context context, SQLiteDatabase database){
        paradaDBHelper = new ParadaDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Parada parada){
        long retorno;
        ContentValues cv = new ContentValues();

        if(parada.getId() > 0){
            cv.put("_id", parada.getId());
        }

        cv.put("id_remoto", parada.getIdRemoto());
        cv.put("id_bairro", parada.getBairro().getId());
        cv.put("referencia", parada.getReferencia());
        cv.put("latitude", parada.getLatitude());
        cv.put("longitude", parada.getLongitude());
        cv.put("status", parada.getStatus());
        cv.put("taxa_de_embarque", parada.getTaxaDeEmbarque());

        if(database.update("parada", cv, "id_remoto = "+parada.getIdRemoto(), null) < 1){
            retorno = database.insert("parada", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("parada", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<Parada> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, referencia, latitude, longitude, status, id_bairro, taxa_de_embarque, " +
                "id_remoto FROM parada", null);
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
        List<Parada> paradas = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do{
                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);
                umaParada.setTaxaDeEmbarque(cursor.getDouble(6));
                umaParada.setIdRemoto(cursor.getInt(7));
               paradas.add(umaParada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradas;
    }

    public List<Parada> listarTodosComItinerario(){
        Cursor cursor = database.rawQuery("SELECT DISTINCT p._id, p.referencia, p.latitude, p.longitude, p.status, p.id_bairro, p.taxa_de_embarque, p.id_remoto " +
                "FROM parada p INNER JOIN parada_itinerario pi ON pi.id_parada = p._id INNER JOIN horario_itinerario hi ON hi.id_itinerario = pi.id_itinerario", null);
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
        List<Parada> paradas = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do{
                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);
                umaParada.setTaxaDeEmbarque(cursor.getDouble(6));
                umaParada.setIdRemoto(cursor.getInt(7));
                paradas.add(umaParada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradas;
    }

    // FUNCAO COS NAO EXISTE SQLITE
//    public List<Parada> listarTodosProximosComItinerario(Double distancia, Location location){
//        Cursor cursor = database.rawQuery("SELECT DISTINCT p._id, p.referencia, p.latitude, p.longitude, p.status, p.id_bairro, " +
//                "( 6371 * acos( cos( radians("+location.getLatitude()+") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("+location.getLongitude()+") ) + " +
//                "sin( radians("+location.getLatitude()+") ) * sin( radians( latitude ) ) ) ) AS distance  FROM "
//                +paradaDBHelper.TABELA+" p INNER JOIN "+ParadaItinerarioDBHelper.TABELA+" pi ON pi.id_parada = p._id INNER JOIN "
//                +HorarioItinerarioDBHelper.TABELA+" hi ON hi.id_itinerario = pi.id_itinerario HAVING distance < "+distancia+" ORDER BY distance", null);
//        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
//        List<Parada> paradas = new ArrayList<Parada>();
//
//        if(cursor.moveToFirst()){
//            do{
//                Parada umaParada = new Parada();
//                umaParada.setId(cursor.getInt(0));
//                umaParada.setReferencia(cursor.getString(1));
//                umaParada.setLatitude(cursor.getString(2));
//                umaParada.setLongitude(cursor.getString(3));
//                umaParada.setStatus(cursor.getInt(4));
//                Bairro umBairro = new Bairro();
//                umBairro.setId(cursor.getInt(5));
//                umBairro = bairroDBHelper.carregar(context, umBairro);
//
//                umaParada.setBairro(umBairro);
//                paradas.add(umaParada);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        database.close();
//
//        return paradas;
//    }

    public Parada carregar(Parada parada){
        Cursor cursor = database.rawQuery("SELECT _id, referencia, latitude, longitude, status, id_bairro, taxa_de_embarque, id_remoto FROM parada WHERE _id = ?", new String[]{String.valueOf(parada.getId())});
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);

        Parada umaParada = null;

        if(cursor.moveToFirst()){
            do{
                umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);
                umaParada.setTaxaDeEmbarque(cursor.getDouble(6));
                umaParada.setIdRemoto(cursor.getInt(7));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umaParada;
    }

    public List<Parada> listarTodosPorBairro(Bairro bairro){
        Cursor cursor = database.rawQuery("SELECT _id, referencia, latitude, longitude, status, id_bairro, taxa_de_embarque, id_remoto FROM parada WHERE id_bairro = ?", new String[]{String.valueOf(bairro.getId())});
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
        List<Parada> paradas = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do{
                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);
                umaParada.setTaxaDeEmbarque(cursor.getDouble(6));
                umaParada.setIdRemoto(cursor.getInt(7));
                paradas.add(umaParada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradas;
    }

    public List<Parada> listarTodosComItinerarioPorBairro(Bairro bairro){
        Cursor cursor = database.rawQuery("SELECT DISTINCT p._id, p.referencia, p.latitude, p.longitude, p.status, p.id_bairro, p.taxa_de_embarque, p.id_remoto " +
                "FROM parada p INNER JOIN parada_itinerario pi ON pi.id_parada = p._id INNER JOIN horario_itinerario hi ON hi.id_itinerario = pi.id_itinerario " +
                "WHERE id_bairro = ? ORDER BY p.referencia", new String[]{String.valueOf(bairro.getId())});
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
        List<Parada> paradas = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do{
                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);
                umaParada.setTaxaDeEmbarque(cursor.getDouble(6));
                umaParada.setIdRemoto(cursor.getInt(7));
                paradas.add(umaParada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradas;
    }

    public int excluir(Parada parada){
        return database.delete("parada", "_id = "+parada.getId(), null);
    }

    public List<Parada> listarTodosNaoEnviados() {
        Cursor cursor = database.rawQuery("SELECT _id, referencia, latitude, longitude, status, id_bairro, id_remoto FROM parada WHERE status = 3", null);
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
        List<Parada> paradas = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do{
                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);

//                Calendar cal = Calendar.getInstance();
//
//                try{
//                    Date date = DateUtils.convertePadraoBancoParaDate(cursor.getString(6));
//                    cal.setTime(date);
//                } catch(ParseException e){
//                    e.printStackTrace();
//                }

                umaParada.setIdRemoto(cursor.getInt(6));

                paradas.add(umaParada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradas;
    }

    public List<Parada> listarTodosEnviados() {
        Cursor cursor = database.rawQuery("SELECT _id, referencia, latitude, longitude, status, id_bairro, id_remoto FROM parada WHERE status = 4", null);
        BairroDBHelper bairroDBHelper = new BairroDBHelper(context);
        List<Parada> paradas = new ArrayList<Parada>();

        if(cursor.moveToFirst()){
            do{
                Parada umaParada = new Parada();
                umaParada.setId(cursor.getInt(0));
                umaParada.setReferencia(cursor.getString(1));
                umaParada.setLatitude(cursor.getString(2));
                umaParada.setLongitude(cursor.getString(3));
                umaParada.setStatus(cursor.getInt(4));
                Bairro umBairro = new Bairro();
                umBairro.setId(cursor.getInt(5));
                umBairro = bairroDBHelper.carregar(context, umBairro);

                umaParada.setBairro(umBairro);
                umaParada.setIdRemoto(cursor.getInt(6));
                paradas.add(umaParada);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paradas;
    }

}
