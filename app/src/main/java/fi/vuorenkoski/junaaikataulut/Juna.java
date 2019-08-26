package fi.vuorenkoski.junaaikataulut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Juna implements Comparable<Juna> {
    char tunnus;
    int raide;
    Date lahtoAika;
    boolean peruttu;
    boolean arvioOn;
    Date arvioituAika;
    Date saapumisAika;
    String saapumisAsema;
    String paateasema;

    int myohassa;

    public Juna(char tunnus, int raide, Date lahtoAika, boolean peruttu, boolean arvioOn, Date arvioituAika, Date saapumisAika, String saapumisAsema, String paateasema) {
        this.tunnus = tunnus;
        this.raide = raide;
        this.lahtoAika = lahtoAika;
        this.peruttu = peruttu;
        this.arvioOn = arvioOn;
        this.arvioituAika = arvioituAika;
        this.saapumisAika = saapumisAika;
        this.saapumisAsema=saapumisAsema;
        this.paateasema = paateasema;
        this.myohassa = (int)((arvioituAika.getTime()-lahtoAika.getTime())/1000);
        if (this.myohassa<60) this.myohassa=0;
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
