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
import com.google.android.gms.maps.model.MarkerOptions;

public class KarttaNaytto extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int junaNumero;
    private char tunnus;
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
            Kohde kohde=Datahaku.haeKoordinaatit(junaNumero);
            MarkerOptions merkki=new MarkerOptions().position(kohde.getPaikka()).title("Juna "+tunnus+":"+kohde.getNopeus()+" km/h");
            mMap.addMarker(merkki);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kohde.getPaikka(),(float)13));
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
                    Kohde kohde=Datahaku.haeKoordinaatit(junaNumero);
                    MarkerOptions merkki=new MarkerOptions().position(kohde.getPaikka()).title("Juna "+tunnus+":"+kohde.getNopeus()+" km/h");
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
}
