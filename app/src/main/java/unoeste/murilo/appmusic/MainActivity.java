package unoeste.murilo.appmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import db.DBMySongs;
import db.bean.Genero;
import db.bean.Musica;
import db.dal.GeneroDAL;
import db.dal.MusicaDAL;

public class MainActivity extends AppCompatActivity {
    private ListView lvmusicas;
    private ArrayList<Musica> listamusicas;
    private ArrayList<Genero> listageneros;
    private EditText filtropesquisa;
    private AlertDialog alerta;
    private List<Musica> musicas;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuprincipal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itCadGenero:
                Intent intent = new Intent(MainActivity.this, CadGeneroActivity.class);
                startActivity(intent);
                break;
            case R.id.itCadMusica:
                Intent intent2 = new Intent(MainActivity.this, CadMusicaActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvmusicas = (ListView) findViewById(R.id.lvMusicas);
        filtropesquisa = findViewById(R.id.txtPesquisa);
        loadList();

        //insere alguns gêneros
        final MusicaDAL musicaDAL = new MusicaDAL(this);
        GeneroDAL dalg = new GeneroDAL(this);

        listamusicas = new ArrayList();
        listageneros = new ArrayList();
        listamusicas = musicaDAL.get("");
        listageneros = dalg.get("");

        final MusicaAdapter adapter = new
                MusicaAdapter(this, R.layout.bloco_lista, listamusicas);

        lvmusicas.setAdapter(adapter);


        filtropesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Musica> lista = new MusicaDAL(MainActivity.this).get("mus_titulo LIKE '%" + s.toString() + "%' or mus_interprete LIKE '%" + s.toString() + "%'");
                lvmusicas.setAdapter(new MusicaAdapter(MainActivity.this, R.layout.bloco_lista, lista));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvmusicas.setLongClickable(true);

        lvmusicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Musica musica = musicas.get(position);
                gerarLetraMusica(musica.getTitulo(),musica.getInterprete());
            }
        });

    }

   private void gerarLetraMusica(String titulo, String interprete){
        try {
            String iinterprete = interprete.toLowerCase();
            String ttitulo = titulo.toLowerCase();

              ttitulo = ttitulo.replaceAll(" ","-");
              iinterprete = iinterprete.replaceAll(" ","-");

            String url="https://api.vagalume.com.br/search.php?art="+iinterprete+"&mus="+ttitulo+"";
           // Toast.makeText( MainActivity.this, ""+url+"",Toast.LENGTH_SHORT).show();

            AcessaWSTask task=new AcessaWSTask();
            String json=task.execute(url).get();
            //Gson gson = new Gson();
            JSONObject reader = new JSONObject(json);
            JSONObject mus = (JSONObject) reader.getJSONArray("mus").get(0);
            String letra = mus.getString("text");

            Intent intent = new Intent(MainActivity.this, VisualizaLetraMusica.class);
            intent.putExtra("artista", interprete);
            intent.putExtra("titulo",titulo);
            intent.putExtra("letra",letra);
            startActivity(intent);
        }catch (Exception e)
        {
            Intent intent = new Intent(MainActivity.this, VisualizaLetraMusica.class);
            intent.putExtra("titulo","Desculpe");
            intent.putExtra("artista","Não foi encontrado a letra da música escolhida!");
            intent.putExtra("letra","Erro ao obter letra da música escolhida!");
            startActivity(intent);
        }
    }

    private void loadList()
    {
        MusicaDAL music = new MusicaDAL(this);
        this.musicas = music.get("");

        ArrayAdapter<Musica> adapte;
        adapte = new MusicaAdapter(this,R.layout.bloco_lista,this.musicas);
        lvmusicas.setAdapter(adapte);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MusicaDAL music = new MusicaDAL(this);
        this.musicas = music.get("");

        ArrayAdapter<Musica> adapte;
        adapte = new MusicaAdapter(this,R.layout.bloco_lista,this.musicas);
        lvmusicas.setAdapter(adapte);
    }
}


