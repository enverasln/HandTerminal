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

        String sql = "";
        // insert stores
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES (1,'MERKEZ');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(2,'DUDULLU');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(3,'KADIK??Y');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(4,'??SK??DAR');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(5,'BEYO??LU');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(6,'GAZ??OSMANPA??A');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(7,'BAKIRK??Y');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(8,'ANTALYA');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(9,'KURUK??PR??');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(10,'??ZM??T');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(12,'MAV??BULVAR');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(13,'MERS??N');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(14,'POZCU');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(15,'HAL DEPO');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(16,'KARGIPINAR');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(17,'MARA?? P??AZZA');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(18,'BOLU 14 BURDA AVM.');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(19,'ANKARA');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(20,'PAKETLEME');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(21,'ADANA ??MALAT');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(22,'DUDULLU IMALAT');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(23,'DUDULLU TOPTAN');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(24,'ET??KET');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(25,'????R??NEVLER');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(26,'??ZM??R KONAK');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(27,'PEND??K');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(28,'BA??CILAR');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(29,'MERTER');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(31,'BURSA HEYKEL');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(32,'??ANLIURFA P??AZZA');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(33,'GAZ??ANTEP M1');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(34,'??EH??TKAM??L');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(35,'??THALAT DEPO');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(36,'DEN??ZL?? AVM.');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(37,'BURSA ALTIPARMAK');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(39,'TRABZON CEVAH??R');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(40,'E T??CARET');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(41,'TRABZON VARLIBA?? AVM');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(42,'KIRIKKALE');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(43,'ATLASPARK');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(44,'MALATYA');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(45,'SAKARYA ADA AVM');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(46,'P??YU SOL??');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(47,'P??YU YARENL??K');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(48,'P??YU KUVAY?? M??LL??YE');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(49,'P??YU ??EH??T ??SHAK');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(50,'TARSUS H??K??MET KONA??I');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(51,'P??YU KIZKALES??');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(52,'P??YU TEK??R');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(97,'??HRACAT DEPO');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(98,'GUZELYALI');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(99,'TRANSFER DEPOSU');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(555,'TEST MAGAZA');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(986,'E T??CARET V??RMAN');\n";
        db.execSQL(sql);
        sql = "INSERT INTO DEPOLAR(dep_no, dep_adi) VALUES(987,'G??NEY S??PAR???? MERKEZ??');";
        db.execSQL(sql);

        // insert users

        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('mbt','00',1,1)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('22','00',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('112','00',1,2)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('701','00',1,2)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('702','702',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('703','703',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('704','704',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('705','705',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('706','706',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('707','707',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('708','708',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('709','709',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('710','710',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('711','711',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('712','712',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('713','713',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('714','714',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('715','715',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('513','341325*',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('514','00',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('501','00',1,5)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('169','00',1,4)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('166','00',1,5)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('180','00',1,19)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('300','00',1,13)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('227','00',1,9)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('127','00',1,3)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('159','00',1,6)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('409','00',1,8)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('419','00',1,25)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('136','00',1,7)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('433','00',1,28)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('195','00',1,27)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('459','00',1,37)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('217','00',1,10)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('277','00',1,12)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('336','00',1,14)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('377','00',1,16)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('582','00',1,17)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('585','00',1,18)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('592','00',1,40)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('593','00',1,26)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('435','00',1,31)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('495','00',1,32)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('461','00',1,33)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('914','00',1,34)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('489','00',1,36)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('483','00',1,39)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('393','00',1,44)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('562','00',1,45)\n";
        db.execSQL(sql);
        sql = "INSERT INTO KULLANICILAR(KullaniciAdi, Sifre, Aktif, DepoNo) VALUES('481','00',1,41)\n";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
