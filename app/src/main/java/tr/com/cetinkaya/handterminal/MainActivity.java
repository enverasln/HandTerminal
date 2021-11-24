package tr.com.cetinkaya.handterminal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import tr.com.cetinkaya.handterminal.business.concretes.DepoBO;
import tr.com.cetinkaya.handterminal.business.concretes.KullaniciBO;
import tr.com.cetinkaya.handterminal.daos.concretes.DepoSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.KullaniciSQLiteDao;
import tr.com.cetinkaya.handterminal.databinding.ActivityMainBinding;
import tr.com.cetinkaya.handterminal.helpers.Helper;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.Kullanici;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = this.getSharedPreferences("tr.com.cetinkaya.handterminal", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String[] permissions = {
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
            };
            int requestCode = 200;
            requestPermissions(permissions, requestCode);
        }


        //loadDatabase();
    }


    public void login(View view) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        KullaniciBO kullaniciBO = new KullaniciBO(new KullaniciSQLiteDao(db));

        int kullaniciSayisi = kullaniciBO.getCount();
/*
        if(kullaniciSayisi == 0) {
            createMBTuser();
        }*/

        Kullanici kullanici = kullaniciBO.getKullaniciWithKullaniciAdiAndSifre(
                binding.userNameText.getText().toString(),
                binding.passwordText.getText().toString());

        if (kullanici != null) {
            sharedPreferences.edit().putString("loggedUser", kullanici.getKullanciadi()).apply();
            sharedPreferences.edit().putString("userDepo", kullanici.getDepo().getDep_adi()).apply();
            sharedPreferences.edit().putInt("depoNo", kullanici.getDepo().getDep_no()).apply();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void createMBTuser() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        KullaniciBO kullaniciBO = new KullaniciBO(new KullaniciSQLiteDao(db));
        DepoBO depoBO = new DepoBO(new DepoSQLiteDao((db)));

        Depo depo = new Depo();
        depo.setDep_adi("MERKEZ");
        depo.setDep_no(1);

        depoBO.insertDepo(depo);

        Kullanici kullanici = new Kullanici();
        kullanici.setKullanciadi("mbt");
        kullanici.setSifre("00");
        kullanici.setDepo(depo);
        kullanici.setAktif(1);

        kullaniciBO.insertKullanici(kullanici);
        db.close();
    }

    private void loadDatabase() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        KullaniciBO kullaniciBO = new KullaniciBO(new KullaniciSQLiteDao(db));

        int count = kullaniciBO.getCount();
        System.out.println(count);
        if (count == 0) {
            try {
                String sourceDB = String.format("%s/MobilEtiket/%s.db",
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(), SQLiteHelper.DATABASE_NAME);
                String destinationDB = getDatabasePath(SQLiteHelper.DATABASE_NAME).toString();
                Helper.loadDatabase(sourceDB, destinationDB);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}