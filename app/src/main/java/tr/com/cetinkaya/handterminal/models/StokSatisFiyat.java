package tr.com.cetinkaya.handterminal.models;

import java.io.Serializable;

public class StokSatisFiyat implements Serializable {
    private String sfiyat_guid;
    private Stok stok;
    private int sfiyat_listesirano;
    private int sfiyat_birim_pntr;
    private Depo depo;
    private float sfiyat_fiyati;
    private String sfiyat_create_date;
    private String sfiyat_lastup_date;

    public StokSatisFiyat() {
    }

    public StokSatisFiyat(String sfiyat_guid, Stok stok, int sfiyat_listesirano, int sfiyat_birim_pntr, Depo depo, float sfiyat_fiyati, String sfiyat_create_date, String sfiyat_update_date) {
        this.sfiyat_guid = sfiyat_guid;
        this.stok = stok;
        this.sfiyat_listesirano = sfiyat_listesirano;
        this.sfiyat_birim_pntr = sfiyat_birim_pntr;
        this.depo = depo;
        this.sfiyat_fiyati = sfiyat_fiyati;
        this.sfiyat_create_date = sfiyat_create_date;
        this.sfiyat_lastup_date = sfiyat_update_date;
    }

    public String getSfiyat_guid() {
        return sfiyat_guid;
    }

    public void setSfiyat_guid(String sfiyat_guid) {
        this.sfiyat_guid = sfiyat_guid;
    }

    public Stok getStok() {
        return stok;
    }

    public void setStok(Stok stok) {
        this.stok = stok;
    }

    public int getSfiyat_listesirano() {
        return sfiyat_listesirano;
    }

    public void setSfiyat_listesirano(int sfiyat_listesirano) {
        this.sfiyat_listesirano = sfiyat_listesirano;
    }

    public int getSfiyat_birim_pntr() {
        return sfiyat_birim_pntr;
    }

    public void setSfiyat_birim_pntr(int sfiyat_birim_pntr) {
        this.sfiyat_birim_pntr = sfiyat_birim_pntr;
    }

    public Depo getDepo() {
        return depo;
    }

    public void setDepo(Depo depo) {
        this.depo = depo;
    }

    public float getSfiyat_fiyati() {
        return sfiyat_fiyati;
    }

    public void setSfiyat_fiyati(float sfiyat_fiyati) {
        this.sfiyat_fiyati = sfiyat_fiyati;
    }

    public String getSfiyat_create_date() {
        return sfiyat_create_date;
    }

    public void setSfiyat_create_date(String sfiyat_create_date) {
        this.sfiyat_create_date = sfiyat_create_date;
    }

    public String getSfiyat_lastup_date() {
        return sfiyat_lastup_date;
    }

    public void setSfiyat_lastup_date(String sfiyat_update_date) {
        this.sfiyat_lastup_date = sfiyat_update_date;
    }
}
