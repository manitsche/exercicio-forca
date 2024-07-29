package manitsche.projeto.exerccioforca;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView palavraView;
    private EditText campoView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button button;
    private String[] palavras = {"ANDROID", "JAVA", "KOTLIN", "MOBILE", "STUDIO"};
    private String palavraEscolhida;
    private char[] palavraMostrada;
    private Set<Character> letrasTentadas = new HashSet<>();
    private int erros = 0;
    private final int MAX_ERROS = 6; // Total de 7 imagens, portanto, MAX_ERROS é 6
    private int progresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        palavraView = findViewById(R.id.palavra);
        campoView = findViewById(R.id.campo);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        iniciarJogo();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarLetra();
            }
        });
    }

    private void iniciarJogo() {
        Random random = new Random();
        palavraEscolhida = palavras[random.nextInt(palavras.length)];
        palavraMostrada = new char[palavraEscolhida.length()];
        Arrays.fill(palavraMostrada, '_');
        palavraView.setText(String.valueOf(palavraMostrada));
        erros = 0;
        progresso = 0;
        letrasTentadas.clear();
        progressBar.setProgress(progresso);
        imageView.setImageResource(R.drawable.imagem1);
    }

    private void verificarLetra() {
        String letraStr = campoView.getText().toString().toUpperCase();
        if (letraStr.length() != 1) {
            Toast.makeText(this, "Digite apenas uma letra", Toast.LENGTH_SHORT).show();
            return;
        }

        char letra = letraStr.charAt(0);
        if (letrasTentadas.contains(letra)) {
            Toast.makeText(this, "Você já tentou essa letra", Toast.LENGTH_SHORT).show();
            return;
        }

        letrasTentadas.add(letra);
        boolean acertou = false;
        for (int i = 0; i < palavraEscolhida.length(); i++) {
            if (palavraEscolhida.charAt(i) == letra) {
                palavraMostrada[i] = letra;
                acertou = true;
            }
        }

        if (acertou) {
            Toast.makeText(this, "Letra existe!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Letra não existe!", Toast.LENGTH_SHORT).show();
            erros++;
        }

        progresso = calcularProgresso();
        progressBar.setProgress(progresso);
        palavraView.setText(String.valueOf(palavraMostrada));

        int imagemId = getResources().getIdentifier("imagem" + (erros + 1), "drawable", getPackageName());
        imageView.setImageResource(imagemId);

        if (Arrays.equals(palavraMostrada, palavraEscolhida.toCharArray())) {
            Toast.makeText(this, "Você venceu!", Toast.LENGTH_LONG).show();
            iniciarJogo();
        } else if (erros > MAX_ERROS) {
            Toast.makeText(this, "Você perdeu! A palavra era: " + palavraEscolhida, Toast.LENGTH_LONG).show();
            iniciarJogo();
        }

        campoView.setText("");
    }

    private int calcularProgresso() {
        int progresso = 0;
        for (char c : palavraMostrada) {
            if (c != '_') {
                progresso++;
            }
        }
        return (int) ((double) progresso / palavraEscolhida.length() * 100);
    }
}