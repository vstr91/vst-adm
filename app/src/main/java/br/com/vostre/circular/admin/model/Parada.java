package br.com.vostre.circular.admin.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.circular.admin.model.dao.ParadaDBHelper;
import br.com.vostre.circular.admin.utils.DateUtils;

/**
 * Created by Almir on 14/04/2014.
 */
public class Parada extends ClasseBase {

    private Bairro bairro;
    private String referencia;
    private String latitude;
    private String longitude;

    private Double taxaDeEmbarque;

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getTaxaDeEmbarque() {
        return taxaDeEmbarque;
    }

    public void setTaxaDeEmbarque(Double taxaDeEmbarque) {
        this.taxaDeEmbarque = taxaDeEmbarque;
    }

    @Override
    public String toString() {
        return this.getReferencia();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, Context context) throws JSONException {

        ParadaDBHelper paradaDBHelper = new ParadaDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            JSONObject paradaObject =  dados.getJSONObject(i);
            Parada umaParada = new Parada();
            umaParada.setIdRemoto(paradaObject.getInt("id"));
            umaParada.setReferencia(paradaObject.getString("referencia"));
            umaParada.setLatitude(paradaObject.getString("latitude"));
            umaParada.setLongitude(paradaObject.getString("longitude"));
            umaParada.setStatus(paradaObject.getInt("status"));

            Bairro umBairro = new Bairro();
            umBairro.setIdRemoto(paradaObject.getInt("bairro"));

            umaParada.setBairro(umBairro);

            if(!paradaObject.getString("taxaDeEmbarque").equals("null") && paradaObject.get("taxaDeEmbarque") != null){
                umaParada.setTaxaDeEmbarque(paradaObject.getDouble("taxaDeEmbarque"));
            }

            paradaDBHelper.salvarOuAtualizar(context, umaParada);

        }

        paradaDBHelper.deletarInativos(context);

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": "+this.getIdRemoto()+", \"referencia\": \""+this.getReferencia()+"\", " +
                "\"latitude\": \""+this.getLatitude()+"\", \"longitude\": \""+this.getLongitude()+"\", " +
                "\"bairro\": "+this.getBairro().getIdRemoto()+"}";


        return resultado;
    }

}
