package fi.vuorenkoski.junaaikataulut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements JunaAdapter.ItemClickListener {
    private Spinner spinnerLahtoasema;
    private Spinner spinnerMaaraasema;
    private RecyclerView recyclerView;
    private JunaAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Juna> junat;

    public static final String EXTRA_MESSAGE_NUMERO ="fi.vuorenkoski.junaaikataulut.extra.MESSAGE_NUMERO";
    public static final String EXTRA_MESSAGE_TUNNUS ="fi.vuorenkoski.junaaikataulut.extra.MESSAGE_TUNNUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) // Tarvitaan nettiyhteyttä varten
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        spinnerLahtoasema = findViewById(R.id.lahtoasema);
        ArrayAdapter<CharSequence> adapterLahtoasema = ArrayAdapter.createFromResource(this,
                R.array.asemaVaihtoehdot, android.R.layout.simple_spinner_item);
        adapterLahtoasema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLahtoasema.setAdapter(adapterLahtoasema);

        spinnerMaaraasema = findViewById(R.id.maaraasema);
        ArrayAdapter<CharSequence> adapterMaaraasema = ArrayAdapter.createFromResource(this,
                R.array.asemaVaihtoehdot, android.R.layout.simple_spinner_item);
        adapterMaaraasema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaaraasema.setAdapter(adapterMaaraasema);

        recyclerView = findViewById(R.id.junaLista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void paivitaData(View view) {
        Asema lahto=new Asema(String.valueOf(spinnerLahtoasema.getSelectedItem()));
        Asema maaranpaa=new Asema(String.valueOf(spinnerMaaraasema.getSelectedItem()));

        if (!lahto.getLyhenne().equals(maaranpaa.getLyhenne())) {
            try {
                junat = Datahaku.haeJunat(lahto.getLyhenne(), maaranpaa.getLyhenne());
                mAdapter = new JunaAdapter(this, junat);
                mAdapter.setClickListener(this);
                recyclerView.setAdapter(mAdapter);
            } catch (Exception e) {
                Toast.makeText(this, "Datan hakeminen ei onnistunut: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, KarttaNaytto.class);
        intent.putExtra(EXTRA_MESSAGE_NUMERO,String.valueOf(junat.get(position).getNumero()));
        intent.putExtra(EXTRA_MESSAGE_TUNNUS,""+junat.get(position).getTunnus());
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_valikko, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.tietoja) {
            startActivity(new Intent(this, Tietoja.class));
        }
        return true;
    }

}
