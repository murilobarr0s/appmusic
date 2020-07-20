package unoeste.murilo.appmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VisualizaLetraMusica extends AppCompatActivity {
    private EditText etletra;
    private TextView tvmusica, tvautor;
    private Button btvoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_letra_musica);
        etletra = findViewById(R.id.etres);
        tvmusica = findViewById(R.id.tvMusica);
        tvautor = findViewById(R.id.tvAutor);
        btvoltar = findViewById(R.id.btVoltar);

        Intent intent = getIntent();
        String artista = ""+intent.getStringExtra("artista");
        String musica = ""+intent.getStringExtra("titulo");
        String letra = ""+intent.getStringExtra("letra");

        etletra.setText(letra);
        tvmusica.setText(musica);
        tvautor.setText(artista);

        etletra.setFocusable(false); // não permite editar

        btvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
