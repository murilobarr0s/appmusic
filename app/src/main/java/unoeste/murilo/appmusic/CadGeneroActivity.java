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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import db.bean.Genero;
import db.bean.Musica;
import db.dal.GeneroDAL;
import db.dal.MusicaDAL;

public class CadGeneroActivity extends AppCompatActivity {
    private Button btvoltar;
    private Button btcadastrar;
    private EditText etgenero,etid;
    private List<Genero> genero;
    private ListView lvgenero;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_genero);

        btvoltar = findViewById(R.id.btVoltar);
        btcadastrar= findViewById(R.id.btCadastrar);
        etgenero = findViewById(R.id.etNome);
        etid = findViewById(R.id.etId);
        lvgenero = findViewById(R.id.lvgenero);
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
                String nomegenero = etgenero.getText().toString();

                if (!nomegenero.equals(""))
                {
                    GeneroDAL dalg = new GeneroDAL(CadGeneroActivity.this);
                    if(Integer.parseInt(etid.getText().toString())== 0){
                        dalg.salvar(new Genero(nomegenero));
                        Toast.makeText(CadGeneroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                        etgenero.setText("");
                        loadList();
                    }
                    else{
                        dalg.alterar(new Genero(Integer.parseInt(etid.getText().toString()),nomegenero));
                        Toast.makeText(CadGeneroActivity.this, "Cadastro Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        etid.setText("0");
                        etgenero.setText("");
                        btcadastrar.setText("Gravar");
                        loadList();
                    }
                }
                else
                    Toast.makeText(CadGeneroActivity.this, "Preencha o nome do gênero de forma correta!", Toast.LENGTH_LONG).show();
            }
        });

        lvgenero.setLongClickable(true);

        lvgenero.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                Genero musica = genero.get(position);
                caixaDialogo(musica);
                return true;
            }
        });

        lvgenero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Genero gen = genero.get(position);
                etid.setText(""+gen.getId());
                etgenero.setText(gen.getNome());
                btcadastrar.setText("Atualizar");
            }
        });
    }

    private void loadList()
    {
        GeneroDAL gdal = new GeneroDAL (this);
        this.genero = gdal.get("");

        ArrayAdapter<Genero> adapte;
        adapte = new GeneroAdapter(this,R.layout.bloco_lista_genero, this.genero);
        lvgenero.setAdapter(adapte);
    }

    private void caixaDialogo(final Genero gen) //Cria o gerador do AlertDialog
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Deseja realizar a exclusão de "+gen.getNome()); //define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                GeneroDAL music = new GeneroDAL (CadGeneroActivity.this);
                MusicaDAL musica = new MusicaDAL(CadGeneroActivity.this);
                ArrayList<Musica> musicaGenero = musica.getGenero(gen.getId());

                if(musicaGenero.size() == 0){
                    if(music.apagar(gen.getId())){
                        Toast toast = Toast.makeText(CadGeneroActivity.this, "Exclusão feita com sucesso!", Toast.LENGTH_SHORT);
                        toast.show();
                        loadList();
                    }
                    else{
                        Toast toast = Toast.makeText(CadGeneroActivity.this, " Falha na Exclusão!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                    Toast.makeText(CadGeneroActivity.this, " O gênero está sendo utilizado em outro registro!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alerta = builder.create();
        alerta.show();
    }
}
