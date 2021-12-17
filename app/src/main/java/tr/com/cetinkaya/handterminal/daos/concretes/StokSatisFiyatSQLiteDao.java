package tr.com.cetinkaya.handterminal.daos.concretes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tr.com.cetinkaya.handterminal.daos.abstracts.IStokSatisFiyatDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public class StokSatisFiyatSQLiteDao implements IStokSatisFiyatDao {
    private final String TAG = "SatisFiyat";

    private SQLiteDatabase sqLiteDatabase;

    public StokSatisFiyatSQLiteDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

    }

    @Override
    public StokSatisFiyat getDepoFiyati(Barkod barkod, Depo depo) {
        StokSatisFiyat stokSatisFiyat = null;
        try {
            String sql = String.format("SELECT * FROM STOK_SATIS_FIYAT_LISTELERI " +
                    "WHERE sfiyat_listesirano = 1 AND sfiyat_birim_pntr = %d AND sfiyat_stokkod = '%s' AND " +
                    "sfiyat_deposirano = %d ORDER BY sfiyat_lastup_date LIMIT 1",
                    barkod.getBar_birimpntr(), barkod.getStok().getSto_kod(),depo.getDep_no());

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql,null);
            int sfiyatGuidIx = c.getColumnIndex(SQLiteHelper.SFIYAT_GUID);
            int sfiyatListeSiraNoIx = c.getColumnIndex(SQLiteHelper.SFIYAT_LISTESIRANO);
            int sfiyatBirimPntrIx = c.getColumnIndex(SQLiteHelper.SFIYAT_BIRIM_PNTR);
            int sfiyatFiyatiIx = c.getColumnIndex(SQLiteHelper.SFIYAT_FIYATI);
            int sfiyatCreateDateIx = c.getColumnIndex(SQLiteHelper.SFIYAT_CREATE_DATE);
            int sfiyatLastUpDateIx = c.getColumnIndex(SQLiteHelper.SFIYAT_LASTUP_DATE);

            if (c!= null || c.getCount() > 0) {
                c.moveToFirst();
                stokSatisFiyat = new StokSatisFiyat();
                stokSatisFiyat.setSfiyat_guid(c.getString(sfiyatGuidIx));
                stokSatisFiyat.setStok(barkod.getStok());
                stokSatisFiyat.setSfiyat_listesirano(c.getInt(sfiyatListeSiraNoIx));
                stokSatisFiyat.setSfiyat_birim_pntr(c.getInt(sfiyatBirimPntrIx));
                stokSatisFiyat.setDepo(depo);
                stokSatisFiyat.setSfiyat_fiyati(c.getFloat(sfiyatFiyatiIx));
                stokSatisFiyat.setSfiyat_create_date(c.getString(sfiyatCreateDateIx));
                stokSatisFiyat.setSfiyat_lastup_date(c.getString(sfiyatLastUpDateIx));

            }
            c.close();
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        return stokSatisFiyat;

    }

    @Override
    public StokSatisFiyat getEtiketFiyat(Barkod barkod, Depo depo) {
        StokSatisFiyat stokSatisFiyat = null;
        try {
            String sql = String.format("SELECT * FROM STOK_SATIS_FIYAT_LISTELERI " +
                            "WHERE sfiyat_birim_pntr = %d AND sfiyat_stokkod = '%s' AND " +
                            "sfiyat_deposirano = %d ORDER BY sfiyat_lastup_date LIMIT 1",
                    barkod.getBar_birimpntr(), barkod.getStok().getSto_kod(),depo.getDep_no());

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql,null);
            int sfiyatGuidIx = c.getColumnIndex(SQLiteHelper.SFIYAT_GUID);
            int sfiyatListeSiraNoIx = c.getColumnIndex(SQLiteHelper.SFIYAT_LISTESIRANO);
            int sfiyatBirimPntrIx = c.getColumnIndex(SQLiteHelper.SFIYAT_BIRIM_PNTR);
            int sfiyatFiyatiIx = c.getColumnIndex(SQLiteHelper.SFIYAT_FIYATI);
            int sfiyatCreateDateIx = c.getColumnIndex(SQLiteHelper.SFIYAT_CREATE_DATE);
            int sfiyatLastUpDateIx = c.getColumnIndex(SQLiteHelper.SFIYAT_LASTUP_DATE);

            if (c!= null || c.getCount() > 0) {
                c.moveToFirst();
                stokSatisFiyat = new StokSatisFiyat();
                stokSatisFiyat.setSfiyat_guid(c.getString(sfiyatGuidIx));
                stokSatisFiyat.setStok(barkod.getStok());
                stokSatisFiyat.setSfiyat_listesirano(c.getInt(sfiyatListeSiraNoIx));
                stokSatisFiyat.setSfiyat_birim_pntr(c.getInt(sfiyatBirimPntrIx));
                stokSatisFiyat.setDepo(depo);
                stokSatisFiyat.setSfiyat_fiyati(c.getFloat(sfiyatFiyatiIx));
                stokSatisFiyat.setSfiyat_create_date(c.getString(sfiyatCreateDateIx));
                stokSatisFiyat.setSfiyat_lastup_date(c.getString(sfiyatLastUpDateIx));

            }
            c.close();
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        return stokSatisFiyat;
    }

    @Override
    public StokSatisFiyat getTakstiliFiyat(Barkod barkod, Depo depo) {
        StokSatisFiyat stokSatisFiyat = null;
        try {
            String sql = String.format("SELECT * FROM STOK_SATIS_FIYAT_LISTELERI " +
                            "WHERE sfiyat_listesirano = 2 AND sfiyat_birim_pntr = %d AND sfiyat_stokkod = '%s' AND " +
                            "sfiyat_deposirano = %d ORDER BY sfiyat_lastup_date LIMIT 1",
                    barkod.getBar_birimpntr(), barkod.getStok().getSto_kod(),depo.getDep_no());

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql,null);
            int sfiyatGuidIx = c.getColumnIndex(SQLiteHelper.SFIYAT_GUID);
            int sfiyatListeSiraNoIx = c.getColumnIndex(SQLiteHelper.SFIYAT_LISTESIRANO);
            int sfiyatBirimPntrIx = c.getColumnIndex(SQLiteHelper.SFIYAT_BIRIM_PNTR);
            int sfiyatFiyatiIx = c.getColumnIndex(SQLiteHelper.SFIYAT_FIYATI);
            int sfiyatCreateDateIx = c.getColumnIndex(SQLiteHelper.SFIYAT_CREATE_DATE);
            int sfiyatLastUpDateIx = c.getColumnIndex(SQLiteHelper.SFIYAT_LASTUP_DATE);

            if (c!= null || c.getCount() > 0) {
                c.moveToFirst();
                stokSatisFiyat = new StokSatisFiyat();
                stokSatisFiyat.setSfiyat_guid(c.getString(sfiyatGuidIx));
                stokSatisFiyat.setStok(barkod.getStok());
                stokSatisFiyat.setSfiyat_listesirano(c.getInt(sfiyatListeSiraNoIx));
                stokSatisFiyat.setSfiyat_birim_pntr(c.getInt(sfiyatBirimPntrIx));
                stokSatisFiyat.setDepo(depo);
                stokSatisFiyat.setSfiyat_fiyati(c.getFloat(sfiyatFiyatiIx));
                stokSatisFiyat.setSfiyat_create_date(c.getString(sfiyatCreateDateIx));
                stokSatisFiyat.setSfiyat_lastup_date(c.getString(sfiyatLastUpDateIx));

            }
            c.close();
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        return stokSatisFiyat;
    }

    @Override
    public String getLastupDate() {
        String lastupDate = "";
        try {
            String sql = "SELECT MAX(sfiyat_lastup_date) AS SAYI FROM STOK_SATIS_FIYAT_LISTELERI";

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
            Log.e(TAG, exception.getMessage());
        }
        return lastupDate;
    }

    @Override
    public int updateSatifFiyat(StokSatisFiyat satisFiyat) {
        int result = 0;
        try {
            ContentValues values = getContentValues(satisFiyat);
            result = sqLiteDatabase.update(SQLiteHelper.STOK_SATIS_FIYAT_LISTELERI, values, "sfiyat_guid = ?", new String[]{satisFiyat.getSfiyat_guid()});

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
            Log.e(TAG, satisFiyat.getSfiyat_guid());
        }
        return result;
    }

    @Override
    public void insertSatisFiyat(StokSatisFiyat satisFiyat) {
        try {
            ContentValues values = getContentValues(satisFiyat);
            sqLiteDatabase.insert(SQLiteHelper.STOK_SATIS_FIYAT_LISTELERI, null, values);

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private ContentValues getContentValues(StokSatisFiyat stokSatisFiyat) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.SFIYAT_GUID, stokSatisFiyat.getSfiyat_guid());
        values.put(SQLiteHelper.SFIYAT_STOKKOD, stokSatisFiyat.getStok().getSto_kod());
        values.put(SQLiteHelper.SFIYAT_LISTESIRANO, stokSatisFiyat.getSfiyat_listesirano());
        values.put(SQLiteHelper.SFIYAT_BIRIM_PNTR, stokSatisFiyat.getSfiyat_birim_pntr());
        values.put(SQLiteHelper.SFIYAT_DEPOSIRANO, stokSatisFiyat.getDepo().getDep_no());
        values.put(SQLiteHelper.SFIYAT_FIYATI, stokSatisFiyat.getSfiyat_fiyati());
        values.put(SQLiteHelper.SFIYAT_CREATE_DATE, stokSatisFiyat.getSfiyat_create_date());
        values.put(SQLiteHelper.SFIYAT_LASTUP_DATE, stokSatisFiyat.getSfiyat_lastup_date());

        return values;
    }
}
