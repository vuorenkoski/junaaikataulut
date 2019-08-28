package fi.vuorenkoski.junaaikataulut;

public class Asema {
    private int asema;
    private String lyhenne;
    private String kokonimi;

    public Asema(String asema) {
        this.asema=0;
        if (asema.equals("HKI") || asema.equals("Helsinki")) {
            this.asema=1;
            this.lyhenne="HKI";
            this.kokonimi="Helsinki";
        }
        if (asema.equals("PSL") || asema.equals("Pasila")) {
            this.asema=2;
            this.lyhenne="PSL";
            this.kokonimi="Pasila";
        }
        if (asema.equals("ILA") || asema.equals("Ilmala")) {
            this.asema=3;
            this.lyhenne="ILA";
            this.kokonimi="Ilmala";
        }
        if (asema.equals("HPL") || asema.equals("Huopalahti")) {
            this.asema=4;
            this.lyhenne="HPL";
            this.kokonimi="Huopalahti";
        }
        if (asema.equals("VMO") || asema.equals("Valimo")) {
            this.asema=5;
            this.lyhenne="VMO";
            this.kokonimi="Valimo";
        }
        if (asema.equals("PJM") || asema.equals("Pitäjänmäki")) {
            this.asema=6;
            this.lyhenne="PJM";
            this.kokonimi="Pitäjänmäki";
        }
        if (asema.equals("MÄK") || asema.equals("Mäkkylä")) {
            this.asema=7;
            this.lyhenne="MÄK";
            this.kokonimi="Mäkkylä";
        }
        if (asema.equals("LPV") || asema.equals("Leppävaara")) {
            this.asema=8;
            this.lyhenne="LPV";
            this.kokonimi="Leppävaara";
        }
        if (asema.equals("KIL") || asema.equals("Kilo")) {
            this.asema=9;
            this.lyhenne="KIL";
            this.kokonimi="Kilo";
        }
        if (asema.equals("KEA") || asema.equals("Kera")) {
            this.asema=10;
            this.lyhenne="KEA";
            this.kokonimi="Kera";
        }
        if (asema.equals("KNI") || asema.equals("Kauniainen")) {
            this.asema=11;
            this.lyhenne="KNI";
            this.kokonimi="Kauniainen";
        }
        if (asema.equals("KVH") || asema.equals("Koivuhovi")) {
            this.asema = 12;
            this.lyhenne = "KVH";
            this.kokonimi = "Koivuhovi";
        }
        if (asema.equals("TRL") || asema.equals("Tuomarila")) {
            this.asema=13;
            this.lyhenne="TRL";
            this.kokonimi="Tuomarila";
        }
        if (asema.equals("EPO") || asema.equals("Espoo")) {
            this.asema=14;
            this.lyhenne="EPO";
            this.kokonimi="Espoo";
        }
        if (asema.equals("KLH") || asema.equals("Kauklahti")) {
            this.asema=15;
            this.lyhenne="KLH";
            this.kokonimi="Kauklahti";
        }

        if (asema.equals("MAS") || asema.equals("Masala")) {
            this.asema=16;
            this.lyhenne="MAS";
            this.kokonimi="Masala";
        }

        if (asema.equals("JRS") || asema.equals("Jorvas")) {
            this.asema=17;
            this.lyhenne="JRS";
            this.kokonimi="Jorvas";
        }

        if (asema.equals("TOL") || asema.equals("Tolsa")) {
            this.asema=18;
            this.lyhenne="TOL";
            this.kokonimi="Tolsa";
        }

        if (asema.equals("KKN") || asema.equals("Kirkkonummi")) {
            this.asema=19;
            this.lyhenne="KKN";
            this.kokonimi="Kirkkonummi";
        }

        if (asema.equals("STI") || asema.equals("Siuntio")) {
            this.asema=20;
            this.lyhenne="STI";
            this.kokonimi="Siuntio";
        }

    }

    public String getLyhenne() {
        return lyhenne;
    }

    public String getKokonimi() {
        return kokonimi;
    }

    @Override
    public String toString() {
        return this.kokonimi;
    }
}
