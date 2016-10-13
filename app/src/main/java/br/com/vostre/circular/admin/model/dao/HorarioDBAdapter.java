package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.circular.admin.model.Bairro;
import br.com.vostre.circular.admin.model.Horario;

/**
 * Created by Almir on 14/04/2014.
 */
public class HorarioDBAdapter {

    private SQLiteDatabase database;
    private HorarioDBHelper horarioDBHelper;
    private Context context;

    public HorarioDBAdapter(Context context, SQLiteDatabase database){
        horarioDBHelper = new HorarioDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Horario horario){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long retorno = null;
        ContentValues cv = new ContentValues();

        if(horario.getId() > 0){
            cv.put("_id", horario.getId());
        }

        cv.put("id_remoto", horario.getIdRemoto());
        cv.put("nome", df.format(horario.getNome().getTime()));
        cv.put("status", horario.getStatus());

        if(database.update("horario", cv, "id_remoto = "+horario.getIdRemoto(), null) < 1){
            retorno = database.insert("horario", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletar(Horario horario){
        int retorno = database.delete("horario", "_id = "+horario.getId(), null);
        database.close();
        return retorno;
    }

    public int deletarInativos(){
        int retorno = database.delete("horario", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<Horario> listarTodos(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, id_remoto FROM horario", null);
        List<Horario> horarios = new ArrayList<Horario>();

        if(cursor.moveToFirst()){
            do{
                Horario umHorario = new Horario();
                umHorario.setId(cursor.getInt(0));
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(df.parse(cursor.getString(1)));
                    umHorario.setNome(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                umHorario.setStatus(cursor.getInt(2));
                umHorario.setIdRemoto(cursor.getInt(3));
               horarios.add(umHorario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return horarios;
    }

    public Horario listarProximoHorarioItinerario(Bairro partida, Bairro destino, String hora){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String diaAtual = "";

        switch(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                diaAtual = "domingo";
                break;
            case Calendar.MONDAY:
                diaAtual = "segunda";
                break;
            case Calendar.TUESDAY:
                diaAtual = "terca";
                break;
            case Calendar.WEDNESDAY:
                diaAtual = "quarta";
                break;
            case Calendar.THURSDAY:
                diaAtual = "quinta";
                break;
            case Calendar.FRIDAY:
                diaAtual = "sexta";
                break;
            case Calendar.SATURDAY:
                diaAtual = "sabado";
                break;
        }

        Cursor cursor = database.rawQuery("SELECT h._id, h.nome, h.status, h.id_remoto FROM horario_itinerario hi LEFT JOIN " +
                "itinerario i ON i._id = hi.id_itinerario LEFT JOIN horario h ON h._id = hi.id_horario WHERE id_partida = ? AND id_destino = ? " +
                "AND TIME(h.nome) > ? AND "+diaAtual+" = -1 ORDER BY  TIME(h.nome) LIMIT 1",
                new String[]{String.valueOf(partida.getId()), String.valueOf(destino.getId()), hora});
        List<Horario> horarios = new ArrayList<Horario>();

        Horario umHorario = null;

        if(cursor.moveToFirst()){
            do{
                umHorario = new Horario();
                umHorario.setId(cursor.getInt(0));
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(df.parse(cursor.getString(1)));
                    umHorario.setNome(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                umHorario.setStatus(cursor.getInt(2));
                umHorario.setIdRemoto(cursor.getInt(3));
                //horarios.add(umHorario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umHorario;
    }

    public Horario listarPrimeiroHorarioItinerario(Bairro partida, Bairro destino, Calendar dia){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String diaAtual = "";

        switch(dia.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                diaAtual = "domingo";
                break;
            case Calendar.MONDAY:
                diaAtual = "segunda";
                break;
            case Calendar.TUESDAY:
                diaAtual = "terca";
                break;
            case Calendar.WEDNESDAY:
                diaAtual = "quarta";
                break;
            case Calendar.THURSDAY:
                diaAtual = "quinta";
                break;
            case Calendar.FRIDAY:
                diaAtual = "sexta";
                break;
            case Calendar.SATURDAY:
                diaAtual = "sabado";
                break;
        }

        String q = "SELECT h._id, h.nome, h.status, h.id_remoto FROM horario_itinerario hi LEFT JOIN itinerario i ON i._id = hi.id_itinerario LEFT JOIN " +
                "horario h ON h._id = hi.id_horario WHERE id_partida = ? AND id_destino = ? AND "+diaAtual+" = -1 " +
                "ORDER BY TIME(h.nome) LIMIT 1";
        Cursor cursor = database.rawQuery("SELECT h._id, h.nome, h.status FROM horario_itinerario hi LEFT JOIN itinerario i ON i._id = hi.id_itinerario LEFT JOIN " +
                "horario h ON h._id = hi.id_horario WHERE id_partida = ? AND id_destino = ? AND "+diaAtual+" = -1 " +
                        "ORDER BY TIME(h.nome) LIMIT 1",
                new String[]{String.valueOf(partida.getIdRemoto()), String.valueOf(destino.getIdRemoto())});
        List<Horario> horarios = new ArrayList<Horario>();

        Horario umHorario = null;

        if(cursor.moveToFirst()){
            do{
                umHorario = new Horario();
                umHorario.setId(cursor.getInt(0));
                try {
                    Calendar cal = Calendar.getInstance();
                    String s = cursor.getString(1);
                    cal.setTime(df.parse(cursor.getString(1)));
                    umHorario.setNome(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                umHorario.setStatus(cursor.getInt(2));
                umHorario.setIdRemoto(cursor.getInt(3));
                //horarios.add(umHorario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umHorario;
    }

    public Horario carregar(Horario horario){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, id_remoto FROM horario WHERE id_remoto = ?", new String[]{String.valueOf(horario.getIdRemoto())});

        Horario umHorario = null;

        if(cursor.moveToFirst()){
            do{
                umHorario = new Horario();
                umHorario.setId(cursor.getInt(0));
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(df.parse(cursor.getString(1)));
                    umHorario.setNome(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                umHorario.setStatus(cursor.getInt(2));
                umHorario.setIdRemoto(cursor.getInt(3));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umHorario;
    }

    public List<Horario> listarTodosHorariosPorItinerario(Bairro bairroPartida, Bairro bairroDestino){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor cursor = database.rawQuery("SELECT h._id, h.nome, h.status, h.id_remoto FROM horario h LEFT JOIN horario_itinerario hi ON hi.id_horario = h._id LEFT JOIN " +
                "itinerario i ON i._id = hi.id_itinerario WHERE i.id_partida = ? AND i.id_destino = ? " +
                        "ORDER BY TIME(h.nome)",
                new String[]{String.valueOf(bairroPartida.getId()), String.valueOf(bairroDestino.getId())});
        List<Horario> horarios = new ArrayList<Horario>();

        if(cursor.moveToFirst()){
            do{
                Horario umHorario = new Horario();
                umHorario.setId(cursor.getInt(0));
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(df.parse(cursor.getString(1)));
                    umHorario.setNome(cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                umHorario.setStatus(cursor.getInt(2));
                umHorario.setIdRemoto(cursor.getInt(3));
                horarios.add(umHorario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return horarios;
    }

}
