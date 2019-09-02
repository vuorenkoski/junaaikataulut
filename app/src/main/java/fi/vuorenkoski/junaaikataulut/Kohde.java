package fi.vuorenkoski.junaaikataulut;

import com.google.android.gms.maps.model.LatLng;

public class Kohde {
    private LatLng paikka;
    private String nopeus;

    public Kohde(LatLng paikka, String nopeus) {
        this.paikka = paikka;
        this.nopeus = nopeus;
    }

    public LatLng getPaikka() {
        return paikka;
    }

    public String getNopeus() {
        return nopeus;
    }
}
