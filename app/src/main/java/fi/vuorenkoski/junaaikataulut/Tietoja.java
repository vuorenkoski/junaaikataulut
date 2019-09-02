package fi.vuorenkoski.junaaikataulut;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Tietoja extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tietoja);
        TextView versioTeksti = findViewById(R.id.versioView);
        versioTeksti.setText(BuildConfig.APPLICATION_ID+"\nVersio: "+BuildConfig.VERSION_NAME+" "+BuildConfig.BUILD_TYPE);
    }
}
