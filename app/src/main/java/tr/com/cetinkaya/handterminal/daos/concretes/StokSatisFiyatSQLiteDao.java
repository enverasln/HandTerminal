package tr.com.cetinkaya.handterminal.daos.concretes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tr.com.cetinkaya.handterminal.daos.abstracts.IStokSatisFiyatDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public class StokSatisFiyatSQLiteDao implements IStokSatisFiyatDao {

    private SQLiteDatabase sqLiteDatabase;

    public StokSatisFiyatSQLiteDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

    }

    @Override
    public StokSatisFiyat getIndirimliFiyat(Barkod barkod, Depo depo) {
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
            Log.e("[STOK_SATIS_FIYAT]", exception.getMessage());
        }
        return stokSatisFiyat;

    }

    @Override
    public StokSatisFiyat getEtiketFiyat(Barkod barkod, Depo depo) {
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
            Log.e("[STOK_SATIS_FIYAT]", exception.getMessage());
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
            Log.e("[STOK_SATIS_FIYAT]", exception.getMessage());
        }
        return stokSatisFiyat;
    }
}
