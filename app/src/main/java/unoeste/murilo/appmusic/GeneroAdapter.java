package unoeste.murilo.appmusic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import db.bean.Genero;

public class GeneroAdapter extends ArrayAdapter<Genero> {
    private int layout;
    public GeneroAdapter(Context context, int resource, List<Genero> gen)
    {
        super(context, resource, gen);
        layout=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.layout,parent,false);
        }

        //renomear
        TextView nome = convertView.findViewById(R.id.tvnomeGenero);


        Genero gen = this.getItem(position);
        nome.setText(String.format(gen.getNome()));

        if(gen.getId()%2 == 0)
            convertView.setBackgroundColor(Color.parseColor("#2E8B57"));
        else
            convertView.setBackgroundColor(Color.parseColor("#8FBC8F"));

        return convertView;

    }
}
