package tr.com.cetinkaya.handterminal.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public static final String DATABASE_NAME = "BarkodEtiketDB";
    // Database Definitions
    // BARKOD TANIMLARI Table
    public static final String BARKOD_TANIMLARI = "BARKOD_TANIMLARI";
    public static final String BAR_GUID = "bar_Guid";
    public static final String BAR_KODU = "bar_kodu";
    public static final String BAR_STOKKODU = "bar_stokkodu";
    public static final String BAR_BIRIMPNTR = "bar_birimpntr";
    public static final String BAR_BEDENPNTR = "bar_bedenpntr";
    public static final String BAR_BEDENNUMARASI = "bar_bedennumarasi";
    public static final String BAR_CREATE_DATE = "bar_create_date";
    public static final String BAR_LASTUP_DATE = "bar_lastup_date";
    // DEPOLAR Table
    public static final String DEPOLAR = "DEPOLAR";
    public static final String DEP_NO = "dep_no";
    public static final String DEP_ADI = "dep_adi";

    // STOK_SATIS_FIYAT_LISTELERI Table
    public static final String STOK_SATIS_FIYAT_LISTELERI = "STOK_SATIS_FIYAT_LISTELERI";
    public static final String SFIYAT_GUID = "sfiyat_Guid";
    public static final String SFIYAT_STOKKOD = "sfiyat_stokkod";
    public static final String SFIYAT_LISTESIRANO = "sfiyat_listesirano";
    public static final String SFIYAT_BIRIM_PNTR = "sfiyat_birim_pntr";
    public static final String SFIYAT_DEPOSIRANO = "sfiyat_deposirano";
    public static final String SFIYAT_FIYATI = "sfiyat_fiyati";
    public static final String SFIYAT_CREATE_DATE = "sfiyat_create_date";
    public static final String SFIYAT_LASTUP_DATE = "sfiyat_lastup_date";

    // STOKLAR Table
    public static final String STOKLAR = "STOKLAR";
    public static final String STO_GUID = "sto_Guid";
    public static final String STO_KOD = "sto_kod";
    public static final String STO_ISIM = "sto_isim";
    public static final String STO_KISA_ISMI = "sto_kisa_ismi";
    public static final String STO_BEDEN_KODU = "sto_beden_kodu";
    public static final String STO_MENSEI = "sto_mensei";
    public static final String STO_YERLI_YABANCI = "sto_yerli_yabanci";
    public static final String STO_BIRIM3_KATSAYI = "sto_birim3_katsayi";
    public static final String STO_BIRIM3_AD = "sto_birim3_ad";
    public static final String STO_REYON_KODU = "sto_reyon_kodu";
    public static final String STO_CREATE_DATE = "sto_create_date";
    public static final String STO_LASTUP_DATE = "sto_lastup_date";

    // KULLANICILAR TAble
    public static final String KULLANICILAR = "KULLANICILAR";
    public static final String KULLANICI_ADI = "KullaniciAdi";
    public static final String SIFRE = "Sifre";
    public static final String AKTIF = "Aktif";
    public static final String DEPO_NO = "DepoNo";

    private String createBarkodTanimlariSQL
            = "CREATE TABLE IF NOT EXISTS " + BARKOD_TANIMLARI + "(\n" +
            BAR_GUID + " TEXT,\n" +
            BAR_KODU + " TEXT,\n" +
            BAR_STOKKODU + " TEXT,\n" +
            BAR_BIRIMPNTR + " INTEGER,\n" +
            BAR_BEDENPNTR + " INTEGER,\n" +
            BAR_BEDENNUMARASI + " TEXT,\n" +
            BAR_CREATE_DATE + " TEXT,\n" +
            BAR_LASTUP_DATE + " TEXT);";

    private String createDepolarSQL
            = "CREATE TABLE IF NOT EXISTS " + DEPOLAR + "(\n" +
            DEP_NO + " INTEGER,\n" +
            DEP_ADI + " TEXT);";

    private String createSatisFiyatListeleriSQL
            = "CREATE TABLE IF NOT EXISTS " + STOK_SATIS_FIYAT_LISTELERI + "(\n" +
            SFIYAT_GUID + " TEXT,\n" +
            SFIYAT_STOKKOD + " TEXT,\n" +
            SFIYAT_LISTESIRANO + " INTEGER,\n" +
            SFIYAT_BIRIM_PNTR + " INTEGER,\n" +
            SFIYAT_DEPOSIRANO + " INTEGER,\n" +
            SFIYAT_FIYATI + " REAL,\n" +
            SFIYAT_CREATE_DATE + " TEXT,\n" +
            SFIYAT_LASTUP_DATE + " TEXT);";

    private String createStoklarSQL
            = "CREATE TABLE IF NOT EXISTS " + STOKLAR + "(\n" +
            STO_GUID + " TEXT,\n" +
            STO_KOD + " TEXT,\n" +
            STO_ISIM + " TEXT,\n" +
            STO_KISA_ISMI + " TEXT,\n" +
            STO_BEDEN_KODU + " TEXT,\n" +
            STO_MENSEI + " TEXT,\n" +
            STO_YERLI_YABANCI + " INTEGER,\n" +
            STO_BIRIM3_KATSAYI + " REAL,\n" +
            STO_BIRIM3_AD + " TEXT,\n" +
            STO_REYON_KODU + " TEXT,\n" +
            STO_CREATE_DATE + " TEXT,\n" +
            STO_LASTUP_DATE + " TEXT);";

    private String createKullanicilarSQL
            = "CREATE TABLE IF NOT EXISTS " + KULLANICILAR + "(\n" +
            KULLANICI_ADI + " TEXT,\n" +
            SIFRE + " TEXT,\n" +
            AKTIF + " INTEGER,\n" +
            DEPO_NO + " INTEGER);";



    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createBarkodTanimlariSQL);
        db.execSQL(createDepolarSQL);
        db.execSQL(createSatisFiyatListeleriSQL);
        db.execSQL(createStoklarSQL);
        db.execSQL(createKullanicilarSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}
