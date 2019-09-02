package fi.vuorenkoski.junaaikataulut;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class Datahaku {

    // hakee yhdeltä asemalta lähtevät junat, parametreina lahtoasema ja maaraasema, palauttaa listan Junista
    public static ArrayList<Juna> haeJunat(String asema, String maaranpaa) throws Exception {
        ArrayList<Juna> junat=new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date nykyhetki = new Date(System.currentTimeMillis());

        //  URL urli=new URL("https://rata.digitraffic.fi/api/v1/live-trains?station="+asema+"&departing_trains=70&train");
        // haku aikamäärällä, mutta palauttaa paljon junia
        // URL urli=new URL("https://rata.digitraffic.fi/api/v1//live-trains/station/"+asema+"?minutes_before_departure=15&minutes_after_departure=15&minutes_before_arrival=240&minutes_after_arrival=15&train_categories=Commuter");

        URL urli=new URL("https://rata.digitraffic.fi/api/v1/live-trains/station/"+asema+"?departing_trains=50&train_categories=Commuter");
        Scanner tiedostonLukija = new Scanner(urli.openStream());
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
    }

    public static Kohde haeKoordinaatit(int junaNumero) throws Exception {
        URL urli = new URL("https://rata.digitraffic.fi/api/v1/train-locations/latest/"+junaNumero);
        Scanner tiedostonLukija = new Scanner(urli.openStream());
        JSONArray data=new JSONArray(tiedostonLukija.nextLine());
        String nopeus=data.getJSONObject(0).getString("speed");
        JSONArray paikkatieto=data.getJSONObject(0).getJSONObject("location").getJSONArray("coordinates");
        double xcor=paikkatieto.getDouble(0);
        double ycor=paikkatieto.getDouble(1);
        return new Kohde(new LatLng(ycor,xcor),nopeus);
    }
}
