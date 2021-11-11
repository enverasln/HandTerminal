package tr.com.cetinkaya.handterminal.models;

import java.io.Serializable;

public class Stok implements Serializable {
    String sto_guid;
    String sto_kod;
    String sto_isim;
    String sto_kisa_ismi;
    String sto_beden_kodu;
    String sto_mensei;
    int sto_yerli_yabanci;
    double sto_birim3_katsayi;
    String sto_birim3_ad;
    String sto_reyon_kodu;
    String sto_create_date;
    String sto_lastup_date;

    public Stok() {

    }

    public Stok(String sto_kod, String sto_isim, String sto_kisa_ismi,
                String sto_beden_kodu, String sto_mensei, int sto_yerli_yabanci,
                float sto_birim3_katsayi, String sto_birim3_ad, String sto_reyon_kodu,
                String sto_create_date, String sto_update_date) {
        this.sto_kod = sto_kod;
        this.sto_isim = sto_isim;
        this.sto_kisa_ismi = sto_kisa_ismi;
        this.sto_beden_kodu = sto_beden_kodu;
        this.sto_mensei = sto_mensei;
        this.sto_yerli_yabanci = sto_yerli_yabanci;
        this.sto_birim3_katsayi = sto_birim3_katsayi;
        this.sto_birim3_ad = sto_birim3_ad;
        this.sto_reyon_kodu = sto_reyon_kodu;
        this.sto_create_date = sto_create_date;
        this.sto_lastup_date = sto_update_date;
    }


    public String getSto_guid() {
        return sto_guid;
    }

    public void setSto_guid(String sto_guid) {
        this.sto_guid = sto_guid;
    }

    public String getSto_kod() {
        return sto_kod;
    }

    public void setSto_kod(String sto_kod) {
        this.sto_kod = sto_kod;
    }

    public String getSto_isim() {
        return sto_isim;
    }

    public void setSto_isim(String sto_isim) {
        this.sto_isim = sto_isim;
    }

    public String getSto_kisa_ismi() {
        return sto_kisa_ismi;
    }

    public void setSto_kisa_ismi(String sto_kisa_ismi) {
        this.sto_kisa_ismi = sto_kisa_ismi;
    }

    public String getSto_beden_kodu() {
        return sto_beden_kodu;
    }

    public void setSto_beden_kodu(String sto_beden_kodu) {
        this.sto_beden_kodu = sto_beden_kodu;
    }

    public String getSto_mensei() {
        return sto_mensei;
    }

    public void setSto_mensei(String sto_mensei) {
        this.sto_mensei = sto_mensei;
    }

    public int getSto_yerli_yabanci() {
        return sto_yerli_yabanci;
    }

    public void setSto_yerli_yabanci(int sto_yerli_yabanci) {
        this.sto_yerli_yabanci = sto_yerli_yabanci;
    }

    public double getSto_birim3_katsayi() {
        return sto_birim3_katsayi;
    }

    public void setSto_birim3_katsayi(double sto_birim3_katsayi) {
        this.sto_birim3_katsayi = sto_birim3_katsayi;
    }

    public String getSto_birim3_ad() {
        return sto_birim3_ad;
    }

    public void setSto_birim3_ad(String sto_birim3_ad) {
        this.sto_birim3_ad = sto_birim3_ad;
    }

    public String getSto_reyon_kodu() {
        return sto_reyon_kodu;
    }

    public void setSto_reyon_kodu(String sto_reyon_kodu) {
        this.sto_reyon_kodu = sto_reyon_kodu;
    }

    public String getSto_create_date() {
        return sto_create_date;
    }

    public void setSto_create_date(String sto_create_date) {
        this.sto_create_date = sto_create_date;
    }

    public String getSto_lastup_date() {
        return sto_lastup_date;
    }

    public void setSto_lastup_date(String sto_update_date) {
        this.sto_lastup_date = sto_update_date;
    }

    @Override
    public String toString() {
        return "Stok{" +
                "sto_guid='" + sto_guid + '\'' +
                ", sto_kod='" + sto_kod + '\'' +
                ", sto_isim='" + sto_isim + '\'' +
                '}';
    }
}
