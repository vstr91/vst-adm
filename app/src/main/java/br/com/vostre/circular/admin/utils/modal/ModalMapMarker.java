package br.com.vostre.circular.admin.utils.modal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import br.com.vostre.circular.admin.R;
import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.utils.listener.ModalCadastroListener;
import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.model.dao.ParadaDBHelper;

/**
 * Created by Almir on 07/04/2015.
 */
public class ModalMapMarker extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    public double latitude;
    public double longitude;
    private GoogleMap map;
    TextView textViewReferencia;
    TextView textViewBairro;
    TextView textViewLocal;
    private Button btnEditar;
    private Button btnExcluir;

    Parada paradaEscolhida;
    ModalCadastroListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_map_marker, container, false);

        textViewReferencia = (TextView) view.findViewById(R.id.textViewReferencia);
        textViewBairro = (TextView) view.findViewById(R.id.textViewBairro);
        textViewLocal = (TextView) view.findViewById(R.id.textViewLocal);
//        cmbCidade = (CustomSpinner) view.findViewById(R.id.spinnerCidade);
//        cmbBairro = (CustomSpinner) view.findViewById(R.id.spinnerBairro);
        btnEditar = (Button) view.findViewById(R.id.btnEditar);
        btnExcluir = (Button) view.findViewById(R.id.btnExcluir);

        if(this.getDialog() != null){
            this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        Parada parada = getParadaEscolhida();
        textViewReferencia.setText(parada.getReferencia());
        textViewBairro.setText(parada.getBairro().getNome());
        textViewLocal.setText(parada.getBairro().getLocal().getNome());

        btnEditar.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);

        return view;

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public Parada getParadaEscolhida() {
        return paradaEscolhida;
    }

    public void setParadaEscolhida(Parada paradaEscolhida) {
        this.paradaEscolhida = paradaEscolhida;
    }

    public ModalCadastroListener getListener() {
        return listener;
    }

    public void setListener(ModalCadastroListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditar:
                ModalCadastroParada modalCadastroParada = new ModalCadastroParada();
                modalCadastroParada.setParadaSelecionada(getParadaEscolhida());
                modalCadastroParada.setMap(map);
                modalCadastroParada.setListener(getListener());
                modalCadastroParada.show(getFragmentManager(), "modal");
                this.dismiss();
                break;
            case R.id.btnExcluir:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmar Exclusão")
                        .setMessage("Deseja realmente excluir a parada?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParadaDBHelper paradaDBHelper = new ParadaDBHelper(getActivity());
                                paradaDBHelper.excluir(getActivity(), (Parada) getParadaEscolhida());

                                listener.onModalCadastroDismissed(getParadaEscolhida().getId());

                                Toast.makeText(getActivity(), "Parada excluída com sucesso.", Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        }).show();
                break;
        }
    }

}
