package tr.com.cetinkaya.handterminal.daos.concretes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tr.com.cetinkaya.handterminal.daos.abstracts.IDepoDao;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Depo;

public class DepoSQLiteDao implements IDepoDao {


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
}
