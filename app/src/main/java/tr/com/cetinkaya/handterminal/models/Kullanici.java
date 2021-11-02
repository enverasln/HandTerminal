package tr.com.cetinkaya.handterminal.models;

public class Kullanici {
    private String kullanciadi;
    private String sifre;
    private int aktif;
    private Depo depo;

    public Kullanici() {

    }

    public Kullanici(String kullanciadi, String sifre, Integer aktif, Depo depo) {
        this.kullanciadi = kullanciadi;
        this.sifre = sifre;
        this.aktif = aktif;
        this.depo = depo;
    }

    public String getKullanciadi() {
        return kullanciadi;
    }

    public void setKullanciadi(String kullanciadi) {
        this.kullanciadi = kullanciadi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public int getAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif;
    }

    public Depo getDepo() {
        return depo;
    }

    public void setDepo(Depo depo) {
        this.depo = depo;
    }
}
