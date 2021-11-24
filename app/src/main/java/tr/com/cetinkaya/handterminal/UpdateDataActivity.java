package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;


import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import tr.com.cetinkaya.handterminal.business.concretes.BarkodBO;
import tr.com.cetinkaya.handterminal.business.concretes.DepoBO;
import tr.com.cetinkaya.handterminal.business.concretes.KullaniciBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.daos.concretes.BarkodSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.DepoSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.KullaniciSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.StokSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.StokSatisFiyatSQLiteDao;
import tr.com.cetinkaya.handterminal.databinding.ActivityUpdateDataBinding;

import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.Kullanici;
import tr.com.cetinkaya.handterminal.models.Stok;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;


public class UpdateDataActivity extends AppCompatActivity {
    private final String TAG = "UpdateDataActivity";
    private SharedPreferences sharedPreferences;
    private int etiketDepoNo;
    private int depoNo;

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
    private StokBO stokBO;
    private BarkodBO barkodBO;
    private DepoBO depoBO;
    private KullaniciBO kullaniciBO;
    private StokSatisFiyatBO stokSatisFiyatBO;
    private ActivityUpdateDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = this.getSharedPreferences("tr.com.cetinkaya.handterminal", MODE_PRIVATE);
        binding.magazaAdiTitleText.setText(sharedPreferences.getString("userDepo", ""));

        etiketDepoNo = 24;
        depoNo = sharedPreferences.getInt("depoNo", 0);


        sqLiteHelper = new SQLiteHelper(this);

        makeInvisibleInformationLabels();
        initDatabase();
        loadLastupdate();
    }

    private void makeInvisibleInformationLabels() {
        binding.stokAktarimAdetText.setVisibility(View.GONE);
        binding.barkodAktarimAdetText.setVisibility(View.GONE);
        binding.fiyatAktarimAdetText.setVisibility(View.GONE);
        binding.depoAktarimAdetText.setVisibility(View.GONE);
        binding.stokAktarimAdetText.setText("");
        binding.barkodAktarimAdetText.setText("");
        binding.fiyatAktarimAdetText.setText("");
        binding.depoAktarimAdetText.setText("");
    }

    private void loadLastupdate() {
        binding.stokTarihButton.setText(getFormattedDate(getShortDate(stokBO.getLastupDate())));
        binding.barkodTarihButton.setText(getFormattedDate(getShortDate(barkodBO.getLastupDate())));
        binding.fiyatTarihButton.setText(getFormattedDate(getShortDate(stokSatisFiyatBO.getLastupDate())));

    }

    public void setTarih(View view) {
        DatePickerDialog datePicker;//Datepicker objemiz
        final Button button = (Button) view;
        String date = button.getText().toString(); // binding.stokTarihButton.getText().toString();

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5)) - 1;
        int year = Integer.parseInt(date.substring(6, 10));
        datePicker = new DatePickerDialog(UpdateDataActivity.this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                int month = monthOfYear + 1;

                String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : Integer.toString(dayOfMonth);
                String monthStr = month < 10 ? "0" + month : Integer.toString(month);

                if (button.getId() == R.id.stokTarihButton) {
                    binding.stokTarihButton.setText(dayStr + "/" + monthStr + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    binding.barkodTarihButton.setText(dayStr + "/" + monthStr + "/" + year);
                    binding.fiyatTarihButton.setText(dayStr + "/" + monthStr + "/" + year);
                } else if (button.getId() == R.id.barkodTarihButton) {
                    binding.barkodTarihButton.setText(dayStr + "/" + monthStr + "/" + year);
                } else if (button.getId() == R.id.fiyatTarihButton) {
                    binding.fiyatTarihButton.setText(dayStr + "/" + monthStr + "/" + year);
                }

            }
        }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
        datePicker.setTitle("Tarih Seçiniz");
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

        datePicker.show();
    }

    private String getShortDate(String date) {
        return date.substring(0, 10);
    }

    private String getFormattedDate(String date) {
        String[] datePart = date.split("-");
        String formattedString = "";
        formattedString = datePart[2].length() == 1 ? "0" + datePart[2] : datePart[2];
        formattedString += "/" + (datePart[1].length() == 1 ? "0" + datePart[1] : datePart[1]);
        formattedString += "/" + datePart[0];

        return formattedString;
    }

    private String getRawDate(String date) {
        String[] datePart = date.split("/");
        String formattedString = "";

        if(datePart.length > 2) {
            formattedString += datePart[2];

            formattedString += "-" + (datePart[1].length() == 1 ? "0" + datePart[1] : datePart[1]);
            formattedString += "-" + (datePart[0].length() == 1 ? "0" + datePart[0] : datePart[0]);
            return formattedString;
        }
        return date;
    }

    private void makeVisibleInformationLabels() {
        if (binding.stokCheckBox.isChecked()) {
            binding.stokAktarimAdetText.setVisibility(View.VISIBLE);
        }

        if (binding.barkodCheckBox.isChecked()) {
            binding.barkodAktarimAdetText.setVisibility(View.VISIBLE);
        }

        if (binding.fiyatCheckBox.isChecked()) {
            binding.fiyatAktarimAdetText.setVisibility(View.VISIBLE);
        }

        if (binding.depoCheckBox.isChecked()) {

            binding.depoAktarimAdetText.setVisibility(View.VISIBLE);
        }
    }

    public void closeActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void passiveAllComponent() {
        binding.stokCheckBox.setEnabled(false);
        binding.barkodCheckBox.setEnabled(false);
        binding.fiyatCheckBox.setEnabled(false);
        binding.depoCheckBox.setEnabled(false);

        binding.stokTarihButton.setEnabled(false);
        binding.barkodTarihButton.setEnabled(false);
        binding.fiyatTarihButton.setEnabled(false);

        binding.veriGuncelleButton.setEnabled(false);



    }

    public void activeAllComponent() {
        binding.stokCheckBox.setEnabled(true);
        binding.barkodCheckBox.setEnabled(true);
        binding.fiyatCheckBox.setEnabled(true);
        binding.depoCheckBox.setEnabled(true);

        binding.stokTarihButton.setEnabled(true);
        binding.barkodTarihButton.setEnabled(true);
        binding.fiyatTarihButton.setEnabled(true);

        binding.veriGuncelleButton.setEnabled(true);
    }

    public void updateData(View view) {
        passiveAllComponent();
        makeInvisibleInformationLabels();

        makeVisibleInformationLabels();


        if (binding.stokCheckBox.isChecked()) {
            new GettingAllStok(binding.stokTarihButton.getText().toString(), 0, 0).execute("http://192.127.2.194:3000/db/stoklar");
        } else if (binding.barkodCheckBox.isChecked()) {
            new GettingAllBarkod(binding.barkodTarihButton.getText().toString(), 0, 0).execute("http://192.127.2.194:3000/db/barkodlar");

        } else if (binding.fiyatCheckBox.isChecked()) {
            new GettingAllFiyat(binding.barkodTarihButton.getText().toString(), 0, etiketDepoNo, depoNo, 0).execute("http://192.127.2.194:3000/db/fiyatlar");

        } else if (binding.depoCheckBox.isChecked()) {
            new GettingAllDepo(0).execute("http://192.127.2.194:3000/db/depolar");
        }

    }

    private void initDatabase() {
        db = sqLiteHelper.getWritableDatabase();

        stokBO = new StokBO(new StokSQLiteDao(db));
        barkodBO = new BarkodBO(new BarkodSQLiteDao(db));
        depoBO = new DepoBO(new DepoSQLiteDao(db));
        kullaniciBO = new KullaniciBO(new KullaniciSQLiteDao(db));
        stokSatisFiyatBO = new StokSatisFiyatBO(new StokSatisFiyatSQLiteDao(db));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public class GettingAllStok extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "ServerConnection";

        private int page;
        private int count;
        private String lastupDate;
        private String jsonStr;

        private boolean flStokSuccess;

        public GettingAllStok(String lastupDate, int page, int count) {
            this.lastupDate = getRawDate(lastupDate);
            this.count = count;
            this.page = page;
            this.jsonStr = "{" +
                    "\"sto_lastup_date\": \"" + this.lastupDate + "\"," +
                    "\"page_number\": " + page + "}";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            flStokSuccess = true;
            try {
                StringEntity requestEntity = new StringEntity(jsonStr, "UTF-8");
                requestEntity.setContentType("application/json");
                HttpPost httpPost = new HttpPost(urls[0]);
                httpPost.setEntity(requestEntity);
                HttpClient httpClient = new DefaultHttpClient();

                //httpPost.addHeader("Content-Type","application/json");
                HttpResponse response = httpClient.execute(httpPost);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String data = EntityUtils.toString(responseEntity);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray stokJSONArray = jsonObject.getJSONArray("data");
                    if (stokJSONArray.length() == 0) {
                        return false;
                    }

                    // Save the stok to the database
                    for (int i = 0; i < stokJSONArray.length(); i++) {
                        Stok newStok = new Stok();
                        JSONObject stok = stokJSONArray.getJSONObject(i);
                        newStok.setSto_guid(stok.getString("sto_guid"));
                        newStok.setSto_kod(stok.getString("sto_kod"));
                        newStok.setSto_isim(stok.getString("sto_isim"));
                        newStok.setSto_kisa_ismi(stok.getString("sto_kisa_ismi"));
                        newStok.setSto_beden_kodu(stok.getString("sto_beden_kodu"));
                        newStok.setSto_mensei(stok.getString("sto_mensei"));
                        newStok.setSto_yerli_yabanci(stok.getInt("sto_yerli_yabanci"));
                        newStok.setSto_birim3_katsayi(stok.getDouble("sto_birim3_katsayi"));
                        newStok.setSto_birim3_ad(stok.getString("sto_birim3_ad"));
                        newStok.setSto_reyon_kodu(stok.getString("sto_reyon_kodu"));
                        newStok.setSto_create_date(stok.getString("sto_create_date").replace("T", " ").replace("Z", ""));
                        newStok.setSto_lastup_date(stok.getString("sto_lastup_date").replace("T", " ").replace("Z", ""));


                        if (stokBO.updateStok(newStok) == 0) {
                            stokBO.insertStok(newStok);
                            count++;
                            publishProgress(count);
                        } else {
                            count++;
                            publishProgress(count);
                        }
                    }
                    Log.d(TAG, jsonObject.toString());
                    return true;
                } else if (status == 404) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    JSONObject jsonObject = new JSONObject(data);
                    Log.d(TAG, jsonObject.toString());
                    return true;
                }

            } catch (IOException exception) {
                exception.printStackTrace();
                flStokSuccess = false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                flStokSuccess = false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.stokAktarimAdetText.setText("Stok tablosu güncelleniyor. \nGüncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllStok(lastupDate, ++page, count).execute("http://192.127.2.194:3000/db/stoklar");
            } else {
                if (!flStokSuccess) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateDataActivity.this);
                    alert.setTitle("Dikkat")
                            .setMessage("Stok bilgileri güncellenirken hata oluştu.\n" +
                                    "Wi-fi kapalı olabilir. Lütfen kontrol ediniz.\n" +
                                    "Wi-fi bağlantısı var ise BT ekibiyle görüşünüz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UpdateDataActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                    return;
                }

                if (count != 0) {
                    binding.stokAktarimAdetText.setText(count + " adet stok kaydı güncellendi. \nAktarım tamamlandı.");
                } else {
                    binding.stokAktarimAdetText.setText("Stok tablosu hali hazırda güncel durumdadır.");
                }

                if (binding.barkodCheckBox.isChecked()) {
                    new GettingAllBarkod(binding.barkodTarihButton.getText().toString(), 0, 0).execute("http://192.127.2.194:3000/db/barkodlar");
                } else if (binding.fiyatCheckBox.isChecked()) {
                    new GettingAllFiyat(binding.fiyatTarihButton.getText().toString(), 0, etiketDepoNo, depoNo, 0).execute("http://192.127.2.194:3000/db/fiyatlar");
                } else if (binding.depoCheckBox.isChecked()) {
                    new GettingAllDepo(0).execute("http://192.127.2.194:3000/db/depolar");
                } else {
                    activeAllComponent();
                }
            }

        }
    }

    public class GettingAllBarkod extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "BarkodConnection";

        private int page;
        private int count;
        private String lastupDate;
        private String jsonStr;

        private boolean flBarkodSuccess;

        public GettingAllBarkod(String lastupDate, int page, int count) {
            this.lastupDate = getRawDate(lastupDate);
            this.count = count;
            this.page = page;
            this.jsonStr = "{" +
                    "\"bar_lastup_date\": \"" + this.lastupDate + "\"," +
                    "\"page_number\": " + page + "}";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            flBarkodSuccess = true;
            try {
                StringEntity requestEntity = new StringEntity(jsonStr, "UTF-8");
                requestEntity.setContentType("application/json");
                HttpPost httpPost = new HttpPost(urls[0]);
                httpPost.setEntity(requestEntity);
                HttpClient httpClient = new DefaultHttpClient();

                //httpPost.addHeader("Content-Type","application/json");
                HttpResponse response = httpClient.execute(httpPost);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String data = EntityUtils.toString(responseEntity);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray stokJSONArray = jsonObject.getJSONArray("data");
                    if (stokJSONArray.length() == 0) {
                        return false;
                    }

                    // Save the barkod to the database
                    for (int i = 0; i < stokJSONArray.length(); i++) {
                        Barkod newBarkod = new Barkod();
                        JSONObject barkod = stokJSONArray.getJSONObject(i);
                        newBarkod.setBar_guid(barkod.getString(SQLiteHelper.BAR_GUID));
                        newBarkod.setBar_kodu(barkod.getString(SQLiteHelper.BAR_KODU));
                        newBarkod.setBar_birimpntr(barkod.getInt(SQLiteHelper.BAR_BIRIMPNTR));
                        newBarkod.setBar_bedenpntr(barkod.getInt(SQLiteHelper.BAR_BEDENPNTR));
                        newBarkod.setBar_bedennumarasi(barkod.getString(SQLiteHelper.BAR_BEDENNUMARASI));
                        newBarkod.setBar_create_date(barkod.getString(SQLiteHelper.BAR_CREATE_DATE)
                                .replace("T", " ")
                                .replace("Z", ""));
                        newBarkod.setBar_lastup_date(barkod.getString(SQLiteHelper.BAR_LASTUP_DATE)
                                .replace("T", " ")
                                .replace("Z", ""));

                        // Get Stok information from JsonObject
                        Stok stok = stokBO.getStokByStokKod(barkod.getString(SQLiteHelper.BAR_STOKKODU));

                        newBarkod.setStok(stok);


                        if (barkodBO.updateBarkod(newBarkod) == 0) {
                            barkodBO.insertBarkod(newBarkod);
                            count++;
                            publishProgress(count);
                        } else {
                            count++;
                            publishProgress(count);
                        }
                    }
                    Log.d(TAG, jsonObject.toString());
                    return true;
                } else if (status == 404) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObject = new JSONObject(data);
                    Log.d(TAG, jsonObject.toString());
                    return true;
                }

            } catch (IOException exception) {
                exception.printStackTrace();
                flBarkodSuccess = false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                flBarkodSuccess = false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.barkodAktarimAdetText.setText("Barkod tablosu güncelleniyor. \nGüncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllBarkod(lastupDate, ++page, count).execute("http://192.127.2.194:3000/db/barkodlar");
            } else {
                if (!flBarkodSuccess) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateDataActivity.this);
                    alert.setTitle("Dikkat")
                            .setMessage("Barkod bilgileri güncellenirken hata oluştu.\n" +
                                    "Wi-fi kapalı olabilir. Lütfen kontrol ediniz.\n" +
                                    "Wi-fi bağlantısı var ise BT ekibiyle görüşünüz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UpdateDataActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                    return;
                }

                if (count != 0) {
                    binding.barkodAktarimAdetText.setText(count + " adet barkod kaydı güncellendi. \nAktarım tamamlandı.");
                } else {
                    binding.barkodAktarimAdetText.setText("Barkod tablosu hali hazırda güncel durumdadır.");
                }
                if (binding.fiyatCheckBox.isChecked()) {
                    new GettingAllFiyat(binding.fiyatTarihButton.getText().toString(), 0, etiketDepoNo, depoNo, 0).execute("http://192.127.2.194:3000/db/fiyatlar");
                } else if (binding.depoCheckBox.isChecked()) {
                    new GettingAllDepo(0).execute("http://192.127.2.194:3000/db/depolar");
                } else {
                    activeAllComponent();
                }
            }

        }
    }

    public class GettingAllFiyat extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "ServerConnection";

        private int page;
        private int etiketDepo;
        private int depo;
        private int count;
        private String lastUpdate;
        private String jsonStr;

        private boolean flFiyatSuccess;

        public GettingAllFiyat(String lastUpdate, int page, int etiketDepo, int depo, int count) {
            this.lastUpdate = getRawDate(lastUpdate);
            this.count = count;
            this.page = page;
            this.etiketDepo = etiketDepo;
            this.depo = depo;
            this.jsonStr = "{" +
                    "\"sfiyat_lastup_date\": \"" + this.lastUpdate + "\"," +
                    "\"etiketDepo\": \"" + etiketDepo + "\"," +
                    "\"depo\": \"" + depo + "\"," +
                    "\"page_number\": " + page + "}";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            flFiyatSuccess = true;
            try {
                StringEntity requestEntity = new StringEntity(jsonStr, "UTF-8");
                requestEntity.setContentType("application/json");
                HttpPost httpPost = new HttpPost(urls[0]);
                httpPost.setEntity(requestEntity);
                HttpClient httpClient = new DefaultHttpClient();

                //httpPost.addHeader("Content-Type","application/json");
                HttpResponse response = httpClient.execute(httpPost);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String data = EntityUtils.toString(responseEntity);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray stokJSONArray = jsonObject.getJSONArray("data");
                    if (stokJSONArray.length() == 0) {
                        return false;
                    }

                    // Save the barkod to the database
                    for (int i = 0; i < stokJSONArray.length(); i++) {
                        JSONObject sfiyat = stokJSONArray.getJSONObject(i);
                        StokSatisFiyat newSFiyat = new StokSatisFiyat();

                        newSFiyat.setSfiyat_guid(sfiyat.getString(SQLiteHelper.SFIYAT_GUID));
                        Stok stok = stokBO.getStokByStokKod(sfiyat.getString(SQLiteHelper.SFIYAT_STOKKOD));
                        newSFiyat.setStok(stok);
                        newSFiyat.setSfiyat_listesirano(sfiyat.getInt(SQLiteHelper.SFIYAT_LISTESIRANO));
                        newSFiyat.setSfiyat_birim_pntr(sfiyat.getInt(SQLiteHelper.SFIYAT_BIRIM_PNTR));
                        newSFiyat.setSfiyat_fiyati(sfiyat.getInt(SQLiteHelper.SFIYAT_FIYATI));
                        Depo depo = depoBO.getDepoById(sfiyat.getInt(SQLiteHelper.SFIYAT_DEPOSIRANO));
                        newSFiyat.setDepo(depo);
                        newSFiyat.setSfiyat_create_date(sfiyat.getString(SQLiteHelper.SFIYAT_CREATE_DATE).replace("T", " ").replace("Z", ""));
                        newSFiyat.setSfiyat_lastup_date(sfiyat.getString(SQLiteHelper.SFIYAT_LASTUP_DATE).replace("T", " ").replace("Z", ""));


                        if (stokSatisFiyatBO.updateSatifFiyat(newSFiyat) == 0) {
                            stokSatisFiyatBO.insertSatisFiyat(newSFiyat);
                            count++;
                            publishProgress(count);
                        } else {
                            count++;
                            publishProgress(count);
                        }
                    }
                    Log.d(TAG, jsonObject.toString());
                    return true;
                } else if (status == 404) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObject = new JSONObject(data);
                    Log.d(TAG, jsonObject.toString());
                    return true;
                }

            } catch (IOException exception) {
                exception.printStackTrace();
                flFiyatSuccess = false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                flFiyatSuccess = false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.fiyatAktarimAdetText.setText("Fiyat tablosu güncelleniyor. \nGüncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllFiyat(lastUpdate, ++page, etiketDepo, depo, count).execute("http://192.127.2.194:3000/db/fiyatlar");
            } else {
                if (!flFiyatSuccess) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateDataActivity.this);
                    alert.setTitle("Dikkat")
                            .setMessage("Fiyat bilgileri güncellenirken hata oluştu.\n" +
                                    "Wi-fi kapalı olabilir. Lütfen kontrol ediniz.\n" +
                                    "Wi-fi bağlantısı var ise BT ekibiyle görüşünüz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UpdateDataActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                    return;
                }

                if (count != 0) {
                    binding.fiyatAktarimAdetText.setText(count + " adet fiyat kaydı güncellendi. \nAktarım tamamlandı.");
                } else {
                    binding.fiyatAktarimAdetText.setText("Fiyat tablosu hali hazırda güncel durumdadır.");
                }
                if (binding.depoCheckBox.isChecked()) {
                    new GettingAllDepo(0).execute("http://192.127.2.194:3000/db/depolar");
                } else {
                    activeAllComponent();
                }

            }

        }
    }

    public class GettingAllDepo extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "DepoConnection";

        private int count;

        private boolean flDepoSuccess;

        public GettingAllDepo(int count) {
            this.count = count;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            flDepoSuccess = true;
            try {

                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpGet);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String data = EntityUtils.toString(responseEntity);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray stokJSONArray = jsonObject.getJSONArray("data");
                    if (stokJSONArray.length() == 0) {
                        return false;
                    }

                    // Save the barkod to the database
                    for (int i = 0; i < stokJSONArray.length(); i++) {
                        JSONObject depo = stokJSONArray.getJSONObject(i);
                        Depo newDepo = new Depo();
                        newDepo.setDep_no(depo.getInt(SQLiteHelper.DEP_NO));
                        newDepo.setDep_adi(depo.getString(SQLiteHelper.DEP_ADI));


                        if (depoBO.updateDepo(newDepo) == 0) {
                            depoBO.insertDepo(newDepo);
                            count++;
                            publishProgress(count);
                        } else {
                            count++;
                            publishProgress(count);
                        }
                    }
                    Log.d(TAG, jsonObject.toString());
                    return true;
                } else if (status == 404) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObject = new JSONObject(data);
                    Log.d(TAG, jsonObject.toString());
                    return true;
                }

            } catch (IOException exception) {
                exception.printStackTrace();
                flDepoSuccess = false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                flDepoSuccess = false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.depoAktarimAdetText.setText("Depo tablosu güncelleniyor. \nGüncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (count != 0) {
                binding.depoAktarimAdetText.setText(count + " adet depo kaydı güncellendi. \nAktarım tamamlandı.\n");
                new GettingAllKullanici(0).execute("http://192.127.2.194:3000/db/kullanicilar");
            } else {
                if (!flDepoSuccess) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateDataActivity.this);
                    alert.setTitle("Dikkat")
                            .setMessage("Depo bilgileri güncellenirken hata oluştu.\n" +
                                    "Wi-fi kapalı olabilir. Lütfen kontrol ediniz.\n" +
                                    "Wi-fi bağlantısı var ise BT ekibiyle görüşünüz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UpdateDataActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                    return;
                }
            }
        }
    }

    public class GettingAllKullanici extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "DepoConnection";

        private int count;

        private boolean flKullaniciSuccess;

        public GettingAllKullanici(int count) {
            this.count = count;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            flKullaniciSuccess = true;
            try {

                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpGet);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String data = EntityUtils.toString(responseEntity);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray stokJSONArray = jsonObject.getJSONArray("data");
                    if (stokJSONArray.length() == 0) {
                        return false;
                    }

                    // Save the barkod to the database
                    for (int i = 0; i < stokJSONArray.length(); i++) {
                        JSONObject kullanici = stokJSONArray.getJSONObject(i);
                        Kullanici newKullanici = new Kullanici();

                        newKullanici.setKullanciadi(kullanici.getString(SQLiteHelper.KULLANICI_ADI));
                        newKullanici.setSifre(kullanici.getString(SQLiteHelper.SIFRE));
                        newKullanici.setAktif(kullanici.getInt(SQLiteHelper.AKTIF));

                        Depo depo = depoBO.getDepoById(kullanici.getInt(SQLiteHelper.DEPO_NO));
                        newKullanici.setDepo(depo);

                        if (kullaniciBO.updateKullanici(newKullanici) == 0) {
                            kullaniciBO.insertKullanici(newKullanici);
                            count++;
                            publishProgress(count);
                        } else {
                            count++;
                            publishProgress(count);
                        }
                    }
                    Log.d(TAG, jsonObject.toString());
                    return true;
                } else if (status == 404) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObject = new JSONObject(data);
                    Log.d(TAG, jsonObject.toString());
                    return true;
                }

            } catch (IOException exception) {
                exception.printStackTrace();
                flKullaniciSuccess = false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                flKullaniciSuccess = false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.depoAktarimAdetText.setText("Kullanıcı tablosu güncelleniyor. \nGüncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (count != 0) {
                binding.depoAktarimAdetText.setText(count + " adet kullanıcı kaydı güncellendi. \nAktarım tamamlandı.");
                db.close();
            }
            else {
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateDataActivity.this);
                alert.setTitle("Dikkat")
                        .setMessage("Kullanıcı bilgileri güncellenirken hata oluştu.\n" +
                                "Wi-fi kapalı olabilir. Lütfen kontrol ediniz.\n" +
                                "Wi-fi bağlantısı var ise BT ekibiyle görüşünüz.")
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UpdateDataActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
            }
            activeAllComponent();
        }

    }
}

