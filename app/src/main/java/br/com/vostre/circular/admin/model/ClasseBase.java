package br.com.vostre.circular.admin.model;

import java.util.Calendar;

/**
 * Created by Almir on 11/10/2016.
 */
public class ClasseBase {

    private int id;
    private int idRemoto;
    private Calendar dataCadastro;
    private int status;
    private int enviado;

    public int getId() {
        return id;
    }

    public void setId(int idLocal) {
        this.id = idLocal;
    }

    public int getIdRemoto() {
        return idRemoto;
    }

    public void setIdRemoto(int idRemoto) {
        this.idRemoto = idRemoto;
    }

    public Calendar getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Calendar dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

}
