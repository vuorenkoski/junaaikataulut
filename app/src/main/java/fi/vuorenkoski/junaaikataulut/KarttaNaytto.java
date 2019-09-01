package fi.vuorenkoski.junaaikataulut;

import androidx.fragment.app.FragmentActivity;

import android.os.Handler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.net.URL;
import java.util.Scanner;

public class KarttaNaytto extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int junaNumero;
    private char tunnus;
    private MarkerOptions merkki;
    private LatLng paikka;
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kartta_naytto);

        Intent intent = getIntent();
        junaNumero=Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_NUMERO));
        tunnus=intent.getStringExtra(MainActivity.EXTRA_MESSAGE_TUNNUS).charAt(0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            haeKoordinaatit();
            mMap.addMarker(merkki);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paikka,(float)13));
//            mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 13));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Koordinaattien hakeminen ei onnistunut (Juna:"+junaNumero+") :"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        doTheAutoRefresh();
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    haeKoordinaatit();
                    mMap.clear();
                    mMap.addMarker(merkki);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(this, "Koordinaattien hakeminen ei onnistunut (Juna:"+junaNumero+") :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                doTheAutoRefresh();
            }
        }, 15000);
    }

    private void haeKoordinaatit() throws Exception {
        URL urli = new URL("https://rata.digitraffic.fi/api/v1/train-locations/latest/"+junaNumero);
        Scanner tiedostonLukija = new Scanner(urli.openStream());
        JSONArray data=new JSONArray(tiedostonLukija.nextLine());
        String nopeus=data.getJSONObject(0).getString("speed");
        JSONArray paikkatieto=data.getJSONObject(0).getJSONObject("location").getJSONArray("coordinates");
        double xcor=paikkatieto.getDouble(0);
        double ycor=paikkatieto.getDouble(1);
        paikka=new LatLng(ycor,xcor);
        merkki=new MarkerOptions().position(paikka).title("Juna "+tunnus+":"+nopeus+" km/h");
    }
}
