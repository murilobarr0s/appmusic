package unoeste.murilo.appmusic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import db.bean.Genero;
import db.bean.Musica;
import db.dal.GeneroDAL;
import db.dal.MusicaDAL;

public class CadMusicaActivity extends AppCompatActivity {
    private Button btvoltar, btcadastrar;
    private EditText ettitulo, etinterprete, etano, etduracao,etid;
    private Spinner spgenero;
    private List<Genero> generos;
    private List<Musica> musicas;
    private ListView lvmusicas;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_musica);

        btvoltar = findViewById(R.id.btVoltar);
        btcadastrar = findViewById(R.id.btCadastrar);
        ettitulo = findViewById(R.id.etTitulo);
        etinterprete = findViewById(R.id.etInterprete);
        etano = findViewById(R.id.etAno);
        etduracao = findViewById(R.id.etDuracao);
        spgenero = findViewById(R.id.spGenero);
        lvmusicas = findViewById(R.id.lvMusic);
        etid = findViewById(R.id.tvId);

        spgenero.setAdapter(montarSpinner());
        loadList();

        btvoltar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid()){
                    MusicaDAL mdal = new MusicaDAL(CadMusicaActivity.this);
                    if(Integer.parseInt(etid.getText().toString()) == 0){
                        Musica m = new Musica(
                                Integer.parseInt(etano.getText().toString().toUpperCase()),
                                ettitulo.getText().toString(),
                                etinterprete.getText().toString(),
                                (Genero) spgenero.getSelectedItem(),
                                Double.parseDouble(etduracao.getText().toString())
                        );

                        if(mdal.salvar(m)){
                            Toast toast = Toast.makeText(CadMusicaActivity.this, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG);
                            toast.show();
                            loadList();
                        }
                        else {
                            Toast toast = Toast.makeText(CadMusicaActivity.this, "Erro no cadastro da música!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                    else{
                        Musica atualizada = new Musica(
                                Integer.parseInt(etid.getText().toString()),
                                Integer.parseInt(etano.getText().toString()),
                                ettitulo.getText().toString(),
                                etinterprete.getText().toString(),
                                (Genero) spgenero.getSelectedItem(),
                                Double.parseDouble(etduracao.getText().toString())
                        );

                        if(mdal.alterar(atualizada)){
                            etid.setText("0");
                            etano.setText("");
                            ettitulo.setText("");
                            etinterprete.setText("");
                            spgenero.setSelection(0);
                            etduracao.setText("");
                            btcadastrar.setText("Gravar");
                            loadList();
                            Toast toast = Toast.makeText(CadMusicaActivity.this, "Cadastro Atualizado com Sucesso!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast = Toast.makeText(CadMusicaActivity.this, "Erro ao Atualizar!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }

            }
        });

        lvmusicas.setLongClickable(true);

        lvmusicas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                Musica musica = musicas.get(position);
                caixaDialogo(musica);
                return true;
            }
        });

        lvmusicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Musica musica = musicas.get(position);
                etid.setText(""+musica.getId());
                ettitulo.setText(musica.getTitulo());
                etano.setText(""+musica.getAno());
                etinterprete.setText(musica.getInterprete());
                etduracao.setText(""+musica.getDuracao());

                GeneroDAL dal = new GeneroDAL(CadMusicaActivity.this);
                generos = dal.get("");

                int pos = 0;
                for (Genero gen:generos
                ) {
                    if(gen.getNome().equals(musica.getGenero().getNome())){
                        break;
                    }
                    pos++;
                }

                spgenero.setSelection(pos);

                btcadastrar.setText("Atualizar");
            }
        });
    }

    private void caixaDialogo(final Musica musica) //Cria o gerador do AlertDialog
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Deseja realizar a exclusão de "+musica.getTitulo()); //define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                MusicaDAL music = new MusicaDAL(CadMusicaActivity.this);
                if(music.apagar(musica.getId())){
                    Toast toast = Toast.makeText(CadMusicaActivity.this, "Exclusão feita com sucesso!", Toast.LENGTH_SHORT);
                    toast.show();
                    loadList();
                }
                else{
                    Toast toast = Toast.makeText(CadMusicaActivity.this, " Falha na Exclusão!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alerta = builder.create();
        alerta.show();
    }

    private boolean isValid(){
        boolean flag = true;
        String erros="";
        if(etano.getText().toString().isEmpty()){
            erros+="Ano inválido!";
            flag = false;
        }

        if(ettitulo.getText().toString().isEmpty()){
            erros+="\nTítulo inválido!";
            flag = false;
        }

        if(etinterprete.getText().toString().isEmpty()){
            erros+="\nIntérprete inválido!";
            flag = false;
        }

        if(etduracao.getText().toString().isEmpty()){
            erros+="\nDuração inválido!";
            flag = false;
        }
        Toast.makeText(CadMusicaActivity.this,""+erros+"",Toast.LENGTH_LONG).show();
        return flag;

    }

    private void loadList()
    {
        MusicaDAL music = new MusicaDAL(this);
        this.musicas = music.get("");

        ArrayAdapter<Musica> adapte;
        adapte = new MusicaAdapter(this,R.layout.bloco_lista,this.musicas);
        lvmusicas.setAdapter(adapte);
    }

    private ArrayAdapter<Genero> montarSpinner() {
        List<Genero> atemps = new ArrayList();
        GeneroDAL dalg = new GeneroDAL(this);
        this.generos = dalg.get("");
        for (Genero gen : this.generos) {
            atemps.add(gen);
        }
        ArrayAdapter<Genero> adaptergen = new ArrayAdapter(this, android.R.layout.simple_spinner_item, atemps);
        return adaptergen;
    }

}
