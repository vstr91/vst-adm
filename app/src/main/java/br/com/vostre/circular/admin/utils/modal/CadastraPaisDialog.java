package br.com.vostre.circular.admin.utils.modal;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.circular.admin.R;
import br.com.vostre.circular.admin.model.Pais;
import br.com.vostre.circular.admin.model.dao.PaisDBHelper;
import br.com.vostre.circular.admin.utils.listener.DialogDismissListener;


public class CadastraPaisDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    EditText editTextNome;
    EditText editTextISO3;
    Spinner spinnerStatus;
    Button btnSalvaPais;
    DialogDismissListener dialogDismissListener;
    Pais umPais;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getPais() != null){
            getDialog().setTitle("Editar País");
        } else{
            getDialog().setTitle("Cadastrar País");
        }



        View view = inflater.inflate(R.layout.dialog_cadastra_pais, container, false);

        editTextNome = (EditText) view.findViewById(R.id.editTextNome);
        editTextISO3 = (EditText) view.findViewById(R.id.editTextISO3);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        btnSalvaPais = (Button) view.findViewById(R.id.btnSalvarPais);

        btnSalvaPais.setOnClickListener(this);

        List<String> status = new ArrayList<>();
        status.add("Ativo");
        status.add("Inativo");
        status.add("Em Análise");

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, status);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        if(getPais() != null){
            editTextNome.setText(getPais().getNome());
            editTextISO3.setText(getPais().getIso3());

            int statusAtual = getPais().getStatus();

            switch(statusAtual){
                case 0:
                    spinnerStatus.setSelection(0);
                    break;
                case 2:
                    spinnerStatus.setSelection(1);
                    break;
                case 3:
                    spinnerStatus.setSelection(2);
                    break;
            }

        }


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSalvarPais:

                if(editTextNome.getText().length() > 0 && editTextISO3.getText().length() > 0){
                    PaisDBHelper paisDBHelper = new PaisDBHelper(getActivity());
                    Pais pais = new Pais();

                    if(getPais() != null){
                        pais.setId(getPais().getId());
                        pais.setIdRemoto(getPais().getIdRemoto());
                    }

                    int statusEscolhido = spinnerStatus.getSelectedItemPosition();

                    switch(statusEscolhido){
                        case 0:
                            pais.setStatus(0);
                            break;
                        case 1:
                            pais.setStatus(2);
                            break;
                        case 2:
                            pais.setStatus(3);
                            break;
                    }

                    pais.setNome(editTextNome.getText().toString());
                    pais.setIso3(editTextISO3.getText().toString());
                    pais.setDataCadastro(Calendar.getInstance());
                    pais.setEnviado(-1);

                    Long result = paisDBHelper.salvarOuAtualizar(getActivity(), pais);

                    if(getPais() == null){
                        pais.setId(result.intValue());
                        dialogDismissListener.onDialogDismiss(result, pais);
                    } else{
                        Pais paisEditado = getPais();
                        paisEditado.setNome(pais.getNome());
                        dialogDismissListener.onDialogDismiss(result, paisEditado);
                    }


                    dismiss();

                } else{
                    Toast.makeText(getActivity(), "Por favor, digite os dados!", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public void setDialogDismissListener(DialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public Pais getPais() {
        return umPais;
    }

    public void setPais(Pais umPais) {
        this.umPais = umPais;
    }
}
