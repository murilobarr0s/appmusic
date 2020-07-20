package unoeste.murilo.appmusic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import db.bean.Musica;

public class MusicaAdapter extends ArrayAdapter<Musica>
{
    private int layout;
    public MusicaAdapter(Context context, int resource, List<Musica> music)
    {
        super(context, resource, music);
        layout=resource;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.layout,parent,false);
        }

        //renomear
        TextView titulo=(TextView)convertView.findViewById(R.id.tvnome);
        TextView interprete=(TextView)convertView.findViewById(R.id.tvinterprete);
        TextView duracao=(TextView)convertView.findViewById(R.id.tvduracao);
        TextView genero=(TextView) convertView.findViewById(R.id.tvgenero);
        TextView ano=(TextView)convertView.findViewById(R.id.tvano);


        Musica musica = this.getItem(position);
        titulo.setText(musica.getTitulo());
        interprete.setText(musica.getInterprete());
        duracao.setText(""+musica.getDuracao()+"min");
        ano.setText("Ano: "+musica.getAno());
        genero.setText(musica.getGenero().getNome());
        ano.setText(""+musica.getAno());

        if(musica.getId()%2 == 0)
            convertView.setBackgroundColor(Color.parseColor("#2E8B57"));
        else
            convertView.setBackgroundColor(Color.parseColor("#8FBC8F"));

        return convertView;

    }
}
