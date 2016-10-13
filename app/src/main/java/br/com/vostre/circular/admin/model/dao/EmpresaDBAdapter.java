package br.com.vostre.circular.admin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.circular.admin.model.Empresa;

/**
 * Created by Almir on 14/04/2014.
 */
public class EmpresaDBAdapter {

    private SQLiteDatabase database;
    private EmpresaDBHelper empresaDBHelper;
    private Context context;

    public EmpresaDBAdapter(Context context, SQLiteDatabase database){
        empresaDBHelper = new EmpresaDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Empresa empresa){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(empresa.getId() > 0){
            cv.put("_id", empresa.getId());
        }

        cv.put("id_remoto", empresa.getIdRemoto());
        cv.put("razao_social", empresa.getRazaoSocial());
        cv.put("fantasia", empresa.getFantasia());
        cv.put("cnpj", empresa.getCnpj());
        cv.put("site", empresa.getSite());
        cv.put("telefone", empresa.getTelefone());
        cv.put("status", empresa.getStatus());

        if(database.update("empresa", cv, "id_remoto = "+empresa.getIdRemoto(), null) < 1){
            retorno = database.insert("empresa", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("empresa", "status = 2", null);
        database.close();
        return retorno;
    }

    public List<Empresa> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, razao_social, fantasia, cnpj, site, telefone, status, id_remoto FROM empresa", null);
        EmpresaDBHelper empresaDBHelper = new EmpresaDBHelper(context);
        List<Empresa> empresas = new ArrayList<Empresa>();

        if(cursor.moveToFirst()){
            do{
                Empresa umaEmpresa = new Empresa();
                umaEmpresa.setId(cursor.getInt(0));

                umaEmpresa.setRazaoSocial(cursor.getString(1));
                umaEmpresa.setFantasia(cursor.getString(2));
                umaEmpresa.setCnpj(cursor.getString(3));
                umaEmpresa.setSite(cursor.getString(4));
                umaEmpresa.setTelefone(cursor.getString(5));
                umaEmpresa.setStatus(cursor.getInt(6));
                umaEmpresa.setIdRemoto(cursor.getInt(7));

                empresas.add(umaEmpresa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return empresas;
    }

    public Empresa carregar(Empresa empresa){
        Cursor cursor = database.rawQuery("SELECT _id, razao_social, fantasia, cnpj, site, telefone, status, id_remoto FROM empresa" +
                " WHERE id_remoto = ?", new String[]{String.valueOf(empresa.getIdRemoto())});
        EmpresaDBHelper empresaDBHelper = new EmpresaDBHelper(context);

        Empresa umaEmpresa = null;

        if(cursor.moveToFirst()){
            do{
                umaEmpresa = new Empresa();
                umaEmpresa.setId(cursor.getInt(0));

                umaEmpresa.setRazaoSocial(cursor.getString(1));
                umaEmpresa.setFantasia(cursor.getString(2));
                umaEmpresa.setCnpj(cursor.getString(3));
                umaEmpresa.setSite(cursor.getString(4));
                umaEmpresa.setTelefone(cursor.getString(5));
                umaEmpresa.setStatus(cursor.getInt(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umaEmpresa;
    }

}
