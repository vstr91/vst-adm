package br.com.vostre.circular.admin.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.circular.admin.model.dao.ItinerarioDBHelper;

/**
 * Created by Almir on 14/04/2014.
 */
public class Itinerario extends ClasseBase {

    private Bairro partida;
    private Bairro destino;
    private Empresa empresa;
    private double valor;
    private String observacao;

    public Bairro getPartida() {
        return partida;
    }

    public void setPartida(Bairro partida) {
        this.partida = partida;
    }

    public Bairro getDestino() {
        return destino;
    }

    public void setDestino(Bairro destino) {
        this.destino = destino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {

        if(this.getPartida().getLocal().getIdRemoto() != this.getDestino().getLocal().getIdRemoto()){
            return this.getPartida().getLocal().getNome()+" x "+this.getDestino().getLocal().getNome();
        } else{
            return this.getPartida().getNome()+" x "+this.getDestino().getNome();
        }


    }

    public void atualizarDados(JSONArray dados, int qtdDados, Context context) throws JSONException {

        ItinerarioDBHelper itinerarioDBHelper = new ItinerarioDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            JSONObject itinerarioObject =  dados.getJSONObject(i);
            Itinerario umItinerario = new Itinerario();
            umItinerario.setIdRemoto(itinerarioObject.getInt("id"));

            Bairro bairroPartida = new Bairro();
            bairroPartida.setIdRemoto(itinerarioObject.getInt("partida"));
            umItinerario.setPartida(bairroPartida);

            Bairro bairroDestino = new Bairro();
            bairroDestino.setIdRemoto(itinerarioObject.getInt("destino"));
            umItinerario.setDestino(bairroDestino);

            umItinerario.setValor(itinerarioObject.getDouble("valor"));
            umItinerario.setStatus(itinerarioObject.getInt("status"));

            Empresa empresaItinerario = new Empresa();
            empresaItinerario.setIdRemoto(itinerarioObject.getInt("empresa"));
            umItinerario.setEmpresa(empresaItinerario);

            umItinerario.setObservacao(itinerarioObject.getString("observacao"));

            itinerarioDBHelper.salvarOuAtualizar(context, umItinerario);

        }

        itinerarioDBHelper.deletarInativos(context);

    }

}
