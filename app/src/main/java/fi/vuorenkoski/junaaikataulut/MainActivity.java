package fi.vuorenkoski.junaaikataulut;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements JunaAdapter.ItemClickListener {
    private Spinner spinnerLahtoasema;
    private Spinner spinnerMaaraasema;
    private RecyclerView recyclerView;
    private JunaAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Juna> junat;


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

        spinnerLahtoasema = (Spinner) findViewById(R.id.lahtoasema);
        ArrayAdapter<CharSequence> adapterLahtoasema = ArrayAdapter.createFromResource(this,
                R.array.asemaVaihtoehdot, android.R.layout.simple_spinner_item);
        adapterLahtoasema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLahtoasema.setAdapter(adapterLahtoasema);

        spinnerMaaraasema = (Spinner) findViewById(R.id.maaraasema);
        ArrayAdapter<CharSequence> adapterMaaraasema = ArrayAdapter.createFromResource(this,
                R.array.asemaVaihtoehdot, android.R.layout.simple_spinner_item);
        adapterMaaraasema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaaraasema.setAdapter(adapterMaaraasema);

        recyclerView = (RecyclerView) findViewById(R.id.junaLista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public static ArrayList<Juna> haeJunat(String asema, String maaranpaa) throws MalformedURLException {
        ArrayList<Juna> junat=new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date nykyhetki = new Date(System.currentTimeMillis());

//        URL urli=new URL("https://rata.digitraffic.fi/api/v1/live-trains?station="+asema+"&departing_trains=70&train");
        URL urli=new URL("https://rata.digitraffic.fi/api/v1/live-trains/station/"+asema+"?departing_trains=50&train_categories=Commuter");
        // haku aikamäärällä, mutta palauttaa paljon junia
        // URL urli=new URL("https://rata.digitraffic.fi/api/v1//live-trains/station/"+asema+"?minutes_before_departure=15&minutes_after_departure=15&minutes_before_arrival=240&minutes_after_arrival=15&train_categories=Commuter");

        try (Scanner tiedostonLukija = new Scanner(urli.openStream())) {
            JSONArray data=new JSONArray(tiedostonLukija.nextLine());

            for (int i=0;i<data.length();i++)
            {
                String tunnusStr=data.getJSONObject(i).getString("commuterLineID");
                if (tunnusStr.length()==1) { // kauko ym. junilla tämä koodi on tyhjä
                    char tunnus=tunnusStr.charAt(0);
                    JSONArray aikatauluRivit=data.getJSONObject(i).getJSONArray("timeTableRows"); // Aikautalutiedot

                    int numero=data.getJSONObject(i).getInt("trainNumber");

                    // Junan koko reitin aikataulutiedot on asemittain, erikseen DEPARTURE ja ARRIVAL
                    // ensin etsitään asema jolta halutaan lähteä
                    boolean lahtoAsemaLoytyi=false;
                    boolean saapumisAsemaLoytyi=false;
                    boolean arvioOn=false;
                    Date arvioituAika=new Date();
                    Date lahtoAika=new Date();
                    Date saapumisAika=new Date();
                    String paateasema="";
                    boolean peruttu=false;
                    String raide="";
                    String syyt=null;

                    for (int j=0;j<aikatauluRivit.length();j++) {
                        if (aikatauluRivit.getJSONObject(j).getString("stationShortCode").equals(asema) &&
                                aikatauluRivit.getJSONObject(j).getString("type").equals("DEPARTURE") &&
                                aikatauluRivit.getJSONObject(j).getBoolean("trainStopping")) {
                            lahtoAsemaLoytyi=true;

                            lahtoAika=dateFormat.parse(aikatauluRivit.getJSONObject(j).getString("scheduledTime"));
                            if (aikatauluRivit.getJSONObject(j).has("liveEstimateTime")) {
                                arvioituAika=dateFormat.parse(aikatauluRivit.getJSONObject(j).getString("liveEstimateTime"));
                                arvioOn=true;
                            }
                            peruttu=aikatauluRivit.getJSONObject(j).getBoolean("cancelled");
                            raide=aikatauluRivit.getJSONObject(j).getString("commercialTrack");
                        }
                        if (lahtoAsemaLoytyi && aikatauluRivit.getJSONObject(j).getString("stationShortCode").equals(maaranpaa) &&
                                aikatauluRivit.getJSONObject(j).getString("type").equals("ARRIVAL") &&
                                aikatauluRivit.getJSONObject(j).getBoolean("trainStopping")) {
                            saapumisAsemaLoytyi=true;
                            saapumisAika=dateFormat.parse(aikatauluRivit.getJSONObject(j).getString("scheduledTime"));
                        }
                        paateasema=aikatauluRivit.getJSONObject(j).getString("stationShortCode");
                        syyt=aikatauluRivit.getJSONObject(j).getJSONArray("causes").toString();
                    }

                    if (!arvioOn) arvioituAika=lahtoAika;

                    if (saapumisAsemaLoytyi && arvioituAika.compareTo(nykyhetki)>0) {
                        junat.add(new Juna(numero,asema,tunnus, raide, lahtoAika, peruttu, arvioOn, arvioituAika, saapumisAika, maaranpaa, paateasema, syyt));
                    }
                }
            }
            return junat.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
            return null;
        }
    }

    public void paivitaData(View view) {
        Asema lahto=new Asema(String.valueOf(spinnerLahtoasema.getSelectedItem()));
        Asema maaranpaa=new Asema(String.valueOf(spinnerMaaraasema.getSelectedItem()));

        if (!lahto.getLyhenne().equals(maaranpaa.getLyhenne())) {

            junat = null;
            try {
                junat = haeJunat(lahto.getLyhenne(), maaranpaa.getLyhenne());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // specify an adapter (see also next example)
            mAdapter = new JunaAdapter(this,junat);
            mAdapter.setClickListener(this);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, junat.get(position).getSyyt(), Toast.LENGTH_SHORT).show();
    }

    //junan paikkatiedon ja nopeuden hakeminen
    // https://rata.digitraffic.fi/api/v1/train-locations/latest/8334

}
