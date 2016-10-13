package br.com.vostre.circular.admin.utils.modal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.circular.admin.R;
import br.com.vostre.circular.admin.adapter.CustomAdapter;
import br.com.vostre.circular.admin.model.Bairro;
import br.com.vostre.circular.admin.model.Local;
import br.com.vostre.circular.admin.model.Parada;
import br.com.vostre.circular.admin.model.dao.BairroDBHelper;
import br.com.vostre.circular.admin.model.dao.LocalDBHelper;
import br.com.vostre.circular.admin.model.dao.ParadaDBHelper;
import br.com.vostre.circular.admin.utils.AnimaUtils;
import br.com.vostre.circular.admin.utils.ListviewComFiltro;
import br.com.vostre.circular.admin.utils.listener.ListviewComFiltroListener;
import br.com.vostre.circular.admin.utils.listener.ModalCadastroListener;

/**
 * Created by Almir on 07/04/2015.
 */
public class ModalCadastroParada extends android.support.v4.app.DialogFragment implements View.OnClickListener,
        ListviewComFiltroListener {

    public double latitude;
    public double longitude;
    private GoogleMap map;
    EditText editTextReferencia;
    EditText editTextLatitude;
    EditText editTextLongitude;
    //private CustomSpinner cmbCidade;
    //private CustomSpinner cmbBairro;
    private Button btnSalvar;
    private Button btnFechar;

    private Button btnCidade;
    private Button btnBairro;
    Local localEscolhido;
    Bairro bairroEscolhido;
    Parada paradaSelecionada;

    ModalCadastroListener listener;

    ParadaDBHelper paradaDBHelper;
    BairroDBHelper bairroDBHelper;

    //Button btnNovoLocal;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_cadastro_parada, container, false);

        editTextReferencia = (EditText) view.findViewById(R.id.editTextReferencia);
        editTextLatitude = (EditText) view.findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) view.findViewById(R.id.editTextLongitude);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnCidade = (Button) view.findViewById(R.id.btnLocal);
        btnBairro = (Button) view.findViewById(R.id.btnBairro);
        //btnNovoLocal = (Button) view.findViewById(R.id.btnNovoLocal);

        if(this.getDialog() != null){
            //this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        final LocalDBHelper localDBHelper = new LocalDBHelper(getActivity());
        bairroDBHelper = new BairroDBHelper(getActivity());
        paradaDBHelper = new ParadaDBHelper(getActivity());

        editTextLatitude.setText(String.valueOf(this.getLatitude()));
        editTextLongitude.setText(String.valueOf(this.getLongitude()));

        final List<Local> locaisDb = localDBHelper.listarTodos(getActivity());
        final CustomAdapter<Local> adapter = new
                CustomAdapter<Local>(getActivity(), R.layout.custom_spinner, locaisDb, "Cidade", locaisDb.size());

        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        btnCidade.setOnClickListener(this);
        btnBairro.setOnClickListener(this);

        btnBairro.setEnabled(false);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        //btnNovoLocal.setOnClickListener(this);

        if(getParadaSelecionada() != null){
            Parada umaParada = (Parada) getParadaSelecionada();
            editTextLatitude.setText(umaParada.getLatitude());
            editTextLongitude.setText(umaParada.getLongitude());
            editTextReferencia.setText(umaParada.getReferencia());

            localEscolhido = umaParada.getBairro().getLocal();
            bairroEscolhido = umaParada.getBairro();

            String estado = "";

            if(localEscolhido.getCidade() != null){
                estado = localEscolhido.getCidade().getNome()+" - ";
            }

            estado = estado.concat(localEscolhido.getEstado().getNome());

            btnCidade.setText(umaParada.getBairro().getLocal().getNome() + "\r\n" + estado);
            btnBairro.setText(umaParada.getBairro().getNome());

            btnBairro.setEnabled(true);

        }

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

    public Parada getParadaSelecionada() {
        return paradaSelecionada;
    }

    public void setParadaSelecionada(Parada paradaSelecionada) {
        this.paradaSelecionada = paradaSelecionada;
    }

    public ModalCadastroListener getListener() {
        return listener;
    }

    public void setListener(ModalCadastroListener listener) {
        this.listener = listener;
    }

    private List<Bairro> listarBairrosLocal(Local local){
        bairroDBHelper = new BairroDBHelper(getActivity());
        return bairroDBHelper.listarPartidaPorItinerario(getActivity(), local);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLocal:
                LocalDBHelper localDBHelper = new LocalDBHelper(v.getContext());

                List<Local> locais = localDBHelper.listarTodosVinculados(v.getContext());

                ListviewComFiltro lista = new ListviewComFiltro();
                lista.setDados(locais);
                lista.setTipoObjeto("local");
                lista.setOnDismissListener(this);
                lista.show(getFragmentManager(), "lista");
                break;
            case R.id.btnBairro:
                List<Bairro> bairros = listarBairrosLocal(localEscolhido);

                ListviewComFiltro listaPartidas = new ListviewComFiltro();
                listaPartidas.setDados(bairros);
                listaPartidas.setTipoObjeto("bairro");
                listaPartidas.setOnDismissListener(this);
                listaPartidas.show(getFragmentManager(), "listaPartida");
                break;
            case R.id.btnSalvar:
                String referencia = editTextReferencia.getText().toString();
                //Bairro bairro = (Bairro) cmbBairro.getSelectedItem();
                String latitude = editTextLatitude.getText().toString();
                String longitude = editTextLongitude.getText().toString();

                if(referencia == null || latitude == null || longitude == null || bairroEscolhido == null || localEscolhido == null){
                    Toast.makeText(getActivity(), "Todos os dados são obrigatórios.", Toast.LENGTH_LONG).show();
                } else{
                    Parada parada = new Parada();

                    if(getParadaSelecionada() != null){
                        parada.setId(getParadaSelecionada().getId());
                    }

                    parada.setReferencia(referencia);
                    parada.setStatus(3);
                    parada.setLatitude(latitude);
                    parada.setLongitude(longitude);

                    parada.setBairro(bairroEscolhido);

                    Long resultado = paradaDBHelper.salvarOuAtualizar(getActivity(), parada);

                    listener.onModalCadastroDismissed(resultado.intValue());

                    Toast.makeText(getActivity(), "Parada salva com sucesso.", Toast.LENGTH_LONG).show();

                    dismiss();
                }

                break;
            case R.id.btnFechar:
                dismiss();
                break;
//            case R.id.btnNovoLocal:
//                ModalCadastroLocal modalCadastroLocal = new ModalCadastroLocal();
//                modalCadastroLocal.setListener(this);
//                modalCadastroLocal.show(getFragmentManager(), "modal_teste");
//                break;
        }
    }

    @Override
    public void onListviewComFiltroDismissed(Object result, String tipoObjeto, List<?> dados) {

        switch (tipoObjeto){
            case "local":
                localEscolhido = (Local) result;

                atualizaBotaoCidade();

                // Testa se retornou apenas um resultado. Em caso positivo, ja segue o preenchimento do restante do formulario,
                // tanto partida quanto destino
                if(dados.size() == 1){
                    Bairro bairro = (Bairro) dados.get(0);
                    bairroEscolhido = bairro;
                    btnBairro.setText(bairro.getNome());
                    btnBairro.setEnabled(true);
                } else{
                    btnBairro.setEnabled(true);
                    btnBairro.setText("Escolha o Bairro");
                    bairroEscolhido = null;
                    AnimaUtils.animaBotao(btnBairro);
                }
                break;
            case "bairro":
                Bairro bairro = (Bairro) result;
                bairroEscolhido = bairro;
                btnBairro.setText(bairro.getNome());
                btnBairro.setEnabled(true);
                break;
        }

    }

//    @Override
//    public void onModalCadastroDismissed(int resultado) {
//
//        if(resultado > 0){
//            LocalColetaDBHelper localColetaDBHelper = new LocalColetaDBHelper(getContext());
//            LocalColeta local = new LocalColeta();
//            local.setId(resultado);
//            local = localColetaDBHelper.carregar(getContext(), local);
//            localEscolhido = local;
//
//            atualizaBotaoCidade();
//
//        }
//
//    }

    private void atualizaBotaoCidade(){
        String estado = "";

        if(localEscolhido.getCidade() != null){
            estado = localEscolhido.getCidade().getNome()+" - ";
        }

        estado = estado.concat(localEscolhido.getEstado().getNome());

        btnCidade.setText(localEscolhido.getNome() + "\r\n" + estado);
    }

}
