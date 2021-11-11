package tr.com.cetinkaya.handterminal.daos.concretes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import tr.com.cetinkaya.handterminal.daos.abstracts.IStokDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Stok;

public class StokSQLiteDao implements IStokDao {
    private final String TAG = "StokSQLiteDao";

    private SQLiteDatabase sqLiteDatabase;

    public StokSQLiteDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

    }

    @Override
    public String getLastupDate() {
        String lastupDate = "";
        try {
            String sql = "SELECT MAX(sto_lastup_date) AS SAYI FROM STOKLAR";

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
    public void insertStok(Stok stok) {
        try {
            ContentValues values = getContentValues(stok);
            sqLiteDatabase.insert(SQLiteHelper.STOKLAR, null, values);

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    @Override
    public int updateStok(Stok stok) {
        int result = 0;
        try {
            ContentValues values = getContentValues(stok);
            result = sqLiteDatabase.update(SQLiteHelper.STOKLAR, values, "sto_guid = ?", new String[]{stok.getSto_guid()});

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        return result;
    }

    private ContentValues getContentValues(Stok stok) {
        ContentValues values = new ContentValues();
        values.put("sto_guid", stok.getSto_guid());
        values.put("sto_kod", stok.getSto_kod());
        values.put("sto_isim", stok.getSto_isim());
        values.put("sto_kisa_ismi", stok.getSto_kisa_ismi());
        values.put("sto_beden_kodu", stok.getSto_beden_kodu());
        values.put("sto_mensei", stok.getSto_mensei());
        values.put("sto_yerli_yabanci", stok.getSto_yerli_yabanci());
        values.put("sto_birim3_katsayi", stok.getSto_birim3_katsayi());
        values.put("sto_birim3_ad", stok.getSto_birim3_ad());
        values.put("sto_reyon_kodu", stok.getSto_reyon_kodu());
        values.put("sto_create_date", stok.getSto_create_date());
        values.put("sto_lastup_date", stok.getSto_lastup_date());
        return values;
    }
}
