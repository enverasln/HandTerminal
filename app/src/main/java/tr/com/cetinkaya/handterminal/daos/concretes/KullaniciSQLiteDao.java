package tr.com.cetinkaya.handterminal.daos.concretes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tr.com.cetinkaya.handterminal.daos.abstracts.IKullaniciDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.Kullanici;

public class KullaniciSQLiteDao implements IKullaniciDao {
    private String TAG = "Kullanici";


    private SQLiteDatabase sqLiteDatabase;

    public KullaniciSQLiteDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

    }

    @Override
    public int getCount() {
        int result = 0;
        try {
            String sql = "SELECT COUNT(*) AS SAYI FROM KULLANICILAR WHERE AKTIF=1";

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql, null);
            if (c == null || c.getCount() == 0) {
                result = 0;
            } else {
                c.moveToFirst();
                int sayi = c.getInt(0);
                result = sayi;

            }
            c.close();
            sqLiteDatabase.close();
        } catch (Exception exception) {
            Log.e("[KULLANICILAR]", exception.getMessage());
        }
        return result;
    }

    @Override
    public Kullanici getKullaniciWithKullaniciAdiAndSifre(String kullaniciAdi, String sifre) {
        Kullanici kullanici = null;
        try {
            String sql = String.format("SELECT Kullaniciadi, Sifre, DepoNo, dep_adi, Aktif FROM KULLANICILAR k " +
                    "INNER JOIN DEPOLAR d ON k.DepoNo = d.dep_no \n" +
                    "WHERE KullaniciAdi = '%s' AND Sifre='%s'", kullaniciAdi, sifre);

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql,null);
            int kAdiIndex = c.getColumnIndex(SQLiteHelper.KULLANICI_ADI);
            int sIndex = c.getColumnIndex(SQLiteHelper.SIFRE);
            int aktifIndex = c.getColumnIndex(SQLiteHelper.AKTIF);            int dNoIndex = c.getColumnIndex(SQLiteHelper.DEPO_NO);
            int dAdiIndex = c.getColumnIndex(SQLiteHelper.DEP_ADI);

            if (c!= null || c.getCount() > 0) {
                c.moveToFirst();
                Depo depo = new Depo();
                depo.setDep_no(c.getInt(dNoIndex));
                depo.setDep_adi(c.getString(dAdiIndex));
                kullanici = new Kullanici();
                kullanici.setKullanciadi(c.getString(kAdiIndex));
                kullanici.setSifre(c.getString(sIndex));
                kullanici.setAktif(c.getInt(aktifIndex));
                kullanici.setDepo(depo);
            }
            c.close();
        } catch (Exception exception) {
            Log.e("[KULLANICILAR]", exception.getMessage());
        }
        return kullanici;
    }

    @Override
    public int updateKullanici(Kullanici kullanici) {
        return 0;
    }

    @Override
    public void insertKullanici(Kullanici kullanici) {
        try {
            ContentValues values = getContentValues(kullanici);
            sqLiteDatabase.insert(SQLiteHelper.DEPOLAR, null, values);

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    private ContentValues getContentValues(Kullanici kullanici) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KULLANICI_ADI, kullanici.getKullanciadi());
        values.put(SQLiteHelper.SIFRE, kullanici.getSifre());
        values.put(SQLiteHelper.AKTIF, kullanici.getAktif());
        values.put(SQLiteHelper.DEPO_NO, kullanici.getDepo().getDep_no());

        return values;
    }
}
