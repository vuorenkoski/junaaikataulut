package fi.vuorenkoski.junaaikataulut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Juna implements Comparable<Juna> {
    private int numero;
    private Asema lahtoasema;
    private char tunnus;
    private String raide;
    private Date lahtoAika;
    private boolean peruttu;
    private boolean arvioOn;
    private Date arvioituAika;
    private Date saapumisAika;
    private Asema saapumisAsema;
    private Asema paateasema;
    private String syyt;

    int myohassa;

    public Juna(int numero, String lahtoasema, char tunnus, String raide, Date lahtoAika, boolean peruttu, boolean arvioOn, Date arvioituAika, Date saapumisAika, String saapumisAsema, String paateasema, String syyt) {
        this.numero=numero;
        this.lahtoasema=new Asema(lahtoasema);
        this.tunnus = tunnus;
        this.raide = raide;
        this.lahtoAika = lahtoAika;
        this.peruttu = peruttu;
        this.arvioOn = arvioOn;
        this.arvioituAika = arvioituAika;
        this.saapumisAika = saapumisAika;
        this.saapumisAsema=new Asema(saapumisAsema);
        this.paateasema = new Asema(paateasema);
        this.myohassa = (int)((arvioituAika.getTime()-lahtoAika.getTime())/1000);
        if (this.myohassa<60) this.myohassa=0;
        this.syyt=syyt;
    }

    public String getLahtoAikaStr() {
        return aikaString(lahtoAika);
    }

    public String getArvioituAikaStr() {
        return aikaString(arvioituAika);
    }

    public String getSaapumisAikaStr() {
        return aikaString(saapumisAika);
    }

    public Date getLahtoAika() {
        return lahtoAika;
    }

    public char getTunnus() {
        return tunnus;
    }

    public String getRaide() {
        return raide;
    }

    public String getLahtoAikaKorjattuStr() {
        if (this.myohassa!=0) {
            return aikaString(lahtoAika)+"-->"+aikaString(arvioituAika);
        } else return aikaString(lahtoAika);
    }

    public String getSyyt() {
        return syyt;
    }

    public String getHuomautus() {
        if (this.peruttu) return "Peruttu";
        if (this.arvioOn && !this.getLahtoAikaStr().equals(this.getArvioituAikaStr())) return "->"+this.getArvioituAikaStr();
        return "";
    }

    private String aikaString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        if (this.peruttu) return ""+tunnus+" "+this.getLahtoAikaStr()+" PERUTTU";
        if (this.myohassa!=0) return ""+tunnus+" "+raide+" "+this.getLahtoAikaStr()+"-->"+this.getArvioituAikaStr()+"  ("+this.saapumisAsema+" "+this.getSaapumisAikaStr()+")";
        return ""+tunnus+" "+raide+" "+this.getArvioituAikaStr()+"  ("+this.saapumisAsema+" "+this.getSaapumisAikaStr()+")";
    }

    @Override
    public int compareTo(Juna t) {
        return (int) (this.lahtoAika.getTime()-t.getLahtoAika().getTime());
    }

}
