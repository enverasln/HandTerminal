package tr.com.cetinkaya.handterminal.daos.concretes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tr.com.cetinkaya.handterminal.daos.abstracts.IDepoDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public class DepoSQLiteDao implements IDepoDao {
    private final String TAG = "Depo";

    private SQLiteDatabase sqLiteDatabase;

    public DepoSQLiteDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

    }


    @Override
    public Depo getDepoById(int id) {
        Depo depo = null;
        try {
            String sql = String.format("SELECT * FROM DEPOLAR d " +
                    "WHERE d.dep_no = %d", id);

            Cursor c;
            c = sqLiteDatabase.rawQuery(sql,null);
            int dNoIndex = c.getColumnIndex(SQLiteHelper.DEP_NO);
            int dAdiIndex = c.getColumnIndex(SQLiteHelper.DEP_ADI);

            if (c!= null || c.getCount() > 0) {
                c.moveToFirst();
                depo = new Depo();
                depo.setDep_no(c.getInt(dNoIndex));
                depo.setDep_adi(c.getString(dAdiIndex));
            }
            c.close();
        } catch (Exception exception) {
            Log.e("[DEPOLAR]", exception.getMessage());
        }
        return depo;
    }

    @Override
    public int updateDepo(Depo depo) {
        int result = 0;
        try {
            ContentValues values = getContentValues(depo);
            result = sqLiteDatabase.update(SQLiteHelper.DEPOLAR, values, "dep_no = ?", new String[]{Integer.toString(depo.getDep_no())});

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        return result;
    }

    @Override
    public void insertDepo(Depo depo) {
        try {
            ContentValues values = getContentValues(depo);
            sqLiteDatabase.insert(SQLiteHelper.DEPOLAR, null, values);

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }


    private ContentValues getContentValues(Depo depo) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.DEP_NO, depo.getDep_no());
        values.put(SQLiteHelper.DEP_ADI, depo.getDep_adi());

        return values;
    }
}
