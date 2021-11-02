package tr.com.cetinkaya.handterminal.models;

public class Barkod {
    private String bar_guid;
    private String bar_kodu;
    private Stok stok;
    private int bar_birimpntr;
    private int bar_bedenpntr;
    private String bar_bedennumarasi;
    private String bar_create_date;
    private String bar_lastup_date;

    public Barkod() {
    }

    public Barkod(String bar_guid, String bar_kodu, Stok stok,
                  int bar_birimpntr, int bar_bedenpntr, String bar_bedennumarasi,
                  String bar_create_date, String bar_update_date) {
        this.bar_guid = bar_guid;
        this.bar_kodu = bar_kodu;
        this.stok = stok;
        this.bar_birimpntr = bar_birimpntr;
        this.bar_bedenpntr = bar_bedenpntr;
        this.bar_bedennumarasi = bar_bedennumarasi;
        this.bar_create_date = bar_create_date;
        this.bar_lastup_date = bar_update_date;
    }

    public String getBar_guid() {
        return bar_guid;
    }

    public void setBar_guid(String bar_guid) {
        this.bar_guid = bar_guid;
    }

    public String getBar_kodu() {
        return bar_kodu;
    }

    public void setBar_kodu(String bar_kodu) {
        this.bar_kodu = bar_kodu;
    }

    public Stok getStok() {
        return stok;
    }

    public void setStok(Stok stok) {
        this.stok = stok;
    }

    public int getBar_birimpntr() {
        return bar_birimpntr;
    }

    public void setBar_birimpntr(int bar_birimpntr) {
        this.bar_birimpntr = bar_birimpntr;
    }

    public int getBar_bedenpntr() {
        return bar_bedenpntr;
    }

    public void setBar_bedenpntr(int bar_bedenpntr) {
        this.bar_bedenpntr = bar_bedenpntr;
    }

    public String getBar_bedennumarasi() {
        return bar_bedennumarasi;
    }

    public void setBar_bedennumarasi(String bar_bedennumarasi) {
        this.bar_bedennumarasi = bar_bedennumarasi;
    }

    public String getBar_create_date() {
        return bar_create_date;
    }

    public void setBar_create_date(String bar_create_date) {
        this.bar_create_date = bar_create_date;
    }

    public String getBar_lastup_date() {
        return bar_lastup_date;
    }

    public void setBar_lastup_date(String bar_lastup_date) {
        this.bar_lastup_date = bar_lastup_date;
    }
}
