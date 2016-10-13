package br.com.vostre.circular.admin.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import br.com.vostre.circular.admin.R;
import br.com.vostre.circular.admin.model.ClasseBase;

/**
 * Created by Almir on 27/09/2014.
 */
public class Lista<ClasseBase extends Object> extends ArrayAdapter<ClasseBase> {

    private final Activity context;
    private final List<ClasseBase> itens;


    public Lista(Activity context, List<ClasseBase> objects) {
        super(context, R.layout.listview_generica, objects);
        this.context = context;
        this.itens = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_generica, null, true);
        TextView textViewToString = (TextView) rowView.findViewById(R.id.textViewToString);

        Object object = itens.get(position);

        textViewToString.setText(object.toString());

        return rowView;
    }

}
