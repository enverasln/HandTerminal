package tr.com.cetinkaya.handterminal.daos.concretes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tr.com.cetinkaya.handterminal.daos.abstracts.IBarkodDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Stok;

public class BarkodSQLiteDao implements IBarkodDao {
    private final String TAG = "BarkodSQLiteDao";
    private SQLiteDatabase sqLiteDatabase;

    public BarkodSQLiteDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

    }


    @Override
    public Barkod getBarkodWithBarkod(String barkodu) {
        Barkod barkod = null;
        try {
            String sql = String.format("SELECT * FROM STOKLAR s INNER JOIN BARKOD_TANIMLARI bt ON bt.bar_stokkodu = s.sto_kod WHERE bt.bar_kodu = '%s' ORDER BY bar_lastup_date DESC",barkodu);

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql,null);
            // Stok sütun indeksleri
            int stoGuidIx = c.getColumnIndex(SQLiteHelper.STO_GUID);
            int stoKodIx = c.getColumnIndex(SQLiteHelper.STO_KOD);
            int stoIsimIx = c.getColumnIndex(SQLiteHelper.STO_ISIM);
            int stoKisaIsimIx = c.getColumnIndex(SQLiteHelper.STO_KISA_ISMI);
            int stoBedenKoduIx = c.getColumnIndex(SQLiteHelper.STO_BEDEN_KODU);
            int stoMenseiIx = c.getColumnIndex(SQLiteHelper.STO_MENSEI);
            int stoYerliYabanciIx = c.getColumnIndex(SQLiteHelper.STO_YERLI_YABANCI);
            int stobirim3KatsayiIx = c.getColumnIndex(SQLiteHelper.STO_BIRIM3_KATSAYI);
            int stobirim3AdIx = c.getColumnIndex(SQLiteHelper.STO_BIRIM3_AD);
            int stoReyonKoduIx = c.getColumnIndex(SQLiteHelper.STO_REYON_KODU);
            int stoCreateDateIx = c.getColumnIndex(SQLiteHelper.STO_CREATE_DATE);
            int stoLastupDateIx = c.getColumnIndex(SQLiteHelper.STO_LASTUP_DATE);

            // Barkod sütün indexleri
            int barGuidIx = c.getColumnIndex(SQLiteHelper.BAR_GUID);
            int barKoduIx = c.getColumnIndex(SQLiteHelper.BAR_KODU);
            int barBirimPntrIx = c.getColumnIndex(SQLiteHelper.BAR_BIRIMPNTR);
            int barBedenPntrIx = c.getColumnIndex(SQLiteHelper.BAR_BEDENPNTR);
            int barBedenNumarasiIx = c.getColumnIndex(SQLiteHelper.BAR_BEDENNUMARASI);
            int barCreateDateIx = c.getColumnIndex(SQLiteHelper.BAR_CREATE_DATE);
            int barLastupDateIx = c.getColumnIndex(SQLiteHelper.BAR_LASTUP_DATE);

            if (c!= null || c.getCount() > 0) {
                c.moveToFirst();
                Stok stok = new Stok();
                stok.setSto_guid(c.getString(stoGuidIx));
                stok.setSto_kod(c.getString(stoKodIx));
                stok.setSto_isim(c.getString(stoIsimIx));
                stok.setSto_kisa_ismi(c.getString(stoKisaIsimIx));
                stok.setSto_beden_kodu(c.getString(stoBedenKoduIx));
                stok.setSto_mensei(c.getString(stoMenseiIx));
                stok.setSto_yerli_yabanci(c.getInt(stoYerliYabanciIx));
                stok.setSto_birim3_katsayi(c.getFloat(stobirim3KatsayiIx));
                stok.setSto_birim3_ad(c.getString(stobirim3AdIx));
                stok.setSto_reyon_kodu(c.getString(stoReyonKoduIx));
                stok.setSto_create_date(c.getString(stoCreateDateIx));
                stok.setSto_lastup_date(c.getString(stoLastupDateIx));

                barkod = new Barkod();
                barkod.setBar_guid(c.getString(barGuidIx));
                barkod.setBar_kodu(c.getString(barKoduIx));
                barkod.setStok(stok);
                barkod.setBar_birimpntr(c.getInt(barBirimPntrIx));
                barkod.setBar_bedenpntr(c.getInt(barBedenPntrIx));
                barkod.setBar_bedennumarasi(c.getString(barBedenNumarasiIx));
                barkod.setBar_create_date(c.getString(barCreateDateIx));
                barkod.setBar_lastup_date(c.getString(barLastupDateIx));
            }
            c.close();
        } catch (Exception exception) {
            Log.e("[BARKOD_TANIMLARI]", exception.getMessage());
        }
        return barkod;
    }

    @Override
    public String getLastupDate() {
        String lastupDate = "";
        try {
            String sql = "SELECT MAX(bar_lastup_date) AS SAYI FROM BARKOD_TANIMLARI";

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql, null);
            if (c == null || c.getCount() == 0) {
                lastupDate = "";
            } else {
                c.moveToFirst();
                String tarih = c.getString(0);
                lastupDate = tarih;

            }
            c.close();
        } catch (Exception exception) {
            Log.e("[STOKLAR]", exception.getMessage());
        }
        return lastupDate;
    }

    @Override
    public int updateBarkod(Barkod barkod) {
        int result = 0;
        try {
            ContentValues values = getContentValues(barkod);
            result = sqLiteDatabase.update(SQLiteHelper.BARKOD_TANIMLARI, values, "bar_Guid = ?", new String[]{barkod.getBar_guid()});

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        return result;
    }

    @Override
    public void insertBarkod(Barkod barkod) {

        try {
            ContentValues values = getContentValues(barkod);
            sqLiteDatabase.insert(SQLiteHelper.BARKOD_TANIMLARI, null, values);

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }

    }

    private ContentValues getContentValues(Barkod barkod) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.BAR_GUID, barkod.getBar_guid());
        values.put(SQLiteHelper.BAR_KODU, barkod.getBar_kodu());
        values.put(SQLiteHelper.BAR_STOKKODU, barkod.getStok().getSto_kod());
        values.put(SQLiteHelper.BAR_BIRIMPNTR, barkod.getBar_birimpntr());
        values.put(SQLiteHelper.BAR_BEDENPNTR, barkod.getBar_bedenpntr());
        values.put(SQLiteHelper.BAR_BEDENNUMARASI, barkod.getBar_bedennumarasi());
        values.put(SQLiteHelper.BAR_CREATE_DATE, barkod.getBar_create_date());
        values.put(SQLiteHelper.BAR_LASTUP_DATE, barkod.getBar_lastup_date());

        return values;
    }
}
