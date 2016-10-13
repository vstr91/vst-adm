package br.com.vostre.circular.admin.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Almir on 07/08/2014.
 */
public class CircularDBHelper extends SQLiteOpenHelper {

    public static final int DBVERSION = 1;
    public static final String DBNAME = "circular.db";

    public CircularDBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PaisDBHelper.DBCREATE);
        db.execSQL(HorarioDBHelper.DBCREATE);
        db.execSQL(EstadoDBHelper.DBCREATE);
        db.execSQL(LocalDBHelper.DBCREATE);
        db.execSQL(BairroDBHelper.DBCREATE);
        db.execSQL(ParadaDBHelper.DBCREATE);
        db.execSQL(EmpresaDBHelper.DBCREATE);
        db.execSQL(ItinerarioDBHelper.DBCREATE);
        db.execSQL(ParadaItinerarioDBHelper.DBCREATE);
        db.execSQL(HorarioItinerarioDBHelper.DBCREATE);

        db.execSQL(ParametroDBHelper.DBCREATE);
        db.execSQL(ParametroDBHelper.DBPOPULATE);
//
//        db.execSQL(MensagemDBHelper.DBCREATE);
        db.execSQL(SecaoItinerarioDBHelper.DBCREATE);
//
//        // versao 3
//        db.execSQL(LocalColetaDBHelper.DBCREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion){
            case 1:
//                db.execSQL(MensagemDBHelper.DBCREATE);
//                db.execSQL(SecaoItinerarioDBHelper.DBCREATE);
//                db.execSQL(ParametroDBHelper.DBALTER_2);
//                db.execSQL(ParametroDBHelper.DBPOPULATE_2);
//                db.execSQL(HorarioItinerarioDBHelper.DBALTER_1);
//                db.execSQL(ParadaDBHelper.DBALTER_1);

                // versao 3
                //db.execSQL(LocalColetaDBHelper.DBCREATE);
                break;
//            case 2:
//                db.execSQL(LocalColetaDBHelper.DBCREATE);
//                break;
        }

    }
}
