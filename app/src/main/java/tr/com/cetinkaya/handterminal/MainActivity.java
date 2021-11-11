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
import android.util.Log;
import android.view.View;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import tr.com.cetinkaya.handterminal.business.concretes.KullaniciBO;
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

        loadDatabase();
        //kullaniciGetir();

    }


   /* private void kullaniciGetir() {

        String url = String.format("%s/db/stoklar", Helper.API_URL);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sto_lastup_date", "2011-02-11 10:40:49.237");
            jsonBody.put("page_number", 1);

            final String mRequestBody = jsonBody.toString();

            StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.e("Cevap", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray kullaniciList = jsonObject.getJSONArray("data");
                        for (int i = 0; i < kullaniciList.length(); i++) {

                        JSONObject kullanici = kullaniciList.getJSONObject(i);

                        String kullaniciAdi = kullanici.getString("sto_kod");
                        String sifre = kullanici.getString("sto_isim");
                        int aktif = kullanici.getInt("sto_birim1_katsayi");
                        Log.e("Cevap", kullaniciAdi + " " + sifre + " " + aktif);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Cevap", error.toString());
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                mRequestBody, "utf-8");
                        return null;
                    }
                }
            };
            Volley.newRequestQueue(this).add(istek);
        }catch(JSONException exception) {
            exception.printStackTrace();
        }

    }*/

    /**
     *
     */
    public void login(View view) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        KullaniciBO kullaniciBO = new KullaniciBO(new KullaniciSQLiteDao(db));
        Kullanici kullanici = kullaniciBO.getKullaniciWithKullaniciAdiAndSifre(binding.userNameText.getText().toString(),
                binding.passwordText.getText().toString());

        if (kullanici != null) {
            sharedPreferences.edit().putString("loggedUser", kullanici.getKullanciadi()).apply();
            sharedPreferences.edit().putString("userDepo", kullanici.getDepo().getDep_adi()).apply();
            sharedPreferences.edit().putInt("depoNo", kullanici.getDepo().getDep_no()).apply();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }

    /**
     * Check user table count. If the count is 0, load external database
     * from ./DCIM/MobilEtiket directory to internal database
     */
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