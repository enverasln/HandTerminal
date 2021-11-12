package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;


import tr.com.cetinkaya.handterminal.business.concretes.BarkodBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokBO;
import tr.com.cetinkaya.handterminal.daos.concretes.BarkodSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.StokSQLiteDao;
import tr.com.cetinkaya.handterminal.databinding.ActivityUpdateDataBinding;

import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Stok;


public class UpdateDataActivity extends AppCompatActivity {
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;
    private StokBO stokBO;
    private BarkodBO barkodBO;
    private final String TAG = "UpdateDataActivity";
    int count = 0;
    private ActivityUpdateDataBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sharedPreferences = this.getSharedPreferences("tr.com.cetinkaya.handterminal", MODE_PRIVATE);
        binding.magazaAdiTitleText.setText(sharedPreferences.getString("userDepo", ""));

        sqLiteHelper = new SQLiteHelper(this);
        db = sqLiteHelper.getWritableDatabase();
        stokBO = new StokBO(new StokSQLiteDao(db));
        barkodBO = new BarkodBO(new BarkodSQLiteDao(db));

        makeInvisibleInformationLabel();
    }

    private void makeInvisibleInformationLabel() {
        binding.aktarimDurumuLabel.setVisibility(View.GONE);
        binding.stokAktarimAdetText.setVisibility(View.GONE);
        binding.barkodAktarimAdetText.setVisibility(View.GONE);
        binding.fiyatAktarimAdetText.setVisibility(View.GONE);
        binding.depoAktarimAdetText.setVisibility(View.GONE);
    }

    public void closeActivity(View view) {
        finish();
    }

    public void updateData(View view) {

        makeInvisibleInformationLabel();
        binding.aktarimDurumuLabel.setVisibility(View.VISIBLE);

        if (binding.stokCheckBox.isChecked()) {
            binding.stokAktarimAdetText.setVisibility(View.VISIBLE);
        }

        if(binding.barkodCheckBox.isChecked()) {
            binding.barkodAktarimAdetText.setVisibility(View.VISIBLE);
        }

        if (binding.stokCheckBox.isChecked()) {

            String stoLastupDate = stokBO.getLastupDate();
            new GettingAllStok(stoLastupDate, 0, 0).execute("http://192.127.2.194:3000/db/stoklar");
        } else if (binding.barkodCheckBox.isChecked()) {
            String stoLastupDate = barkodBO.getLastupDate();
            new GettingAllBarkod(stoLastupDate, 0, 0).execute("http://192.127.2.194:3000/db/barkodlar");
        } else if (binding.fiyatCheckBox.isChecked()) {

        }

    }

    public class GettingAllStok extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "ServerConnection";

        private int page;
        private int count;
        private String lastUpdate;
        private String jsonStr;

        public GettingAllStok(String lastUpdate, int page, int count) {
            this.lastUpdate = lastUpdate;
            this.count = count;
            this.page = page;
            this.jsonStr = "{" +
                    "\"sto_lastup_date\": \"" + lastUpdate + "\"," +
                    "\"page_number\": " + page + "}";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

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
                return false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                return false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.stokAktarimAdetText.setText("Stok tablosu güncelleniyor. Güncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllStok(lastUpdate, ++page, count).execute("http://192.127.2.194:3000/db/stoklar");
            } else {
                if (count != 0) {
                    binding.stokAktarimAdetText.setText(count + " adet stok kaydı güncellendi. Aktarım tamamlandı.");
                } else {
                    binding.stokAktarimAdetText.setText("Stok tablosu hali hazırda güncel durumdadır.");
                }

                if (binding.barkodCheckBox.isChecked()) {
                    new GettingAllBarkod(lastUpdate, 0, 0).execute("http://192.127.2.194:3000/db/barkodlar");
                }
            }

        }


    }

    public class GettingAllBarkod extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "ServerConnection";

        private int page;
        private int count;
        private String lastUpdate;
        private String jsonStr;

        public GettingAllBarkod(String lastUpdate, int page, int count) {
            this.lastUpdate = lastUpdate;
            this.count = count;
            this.page = page;
            this.jsonStr = "{" +
                    "\"bar_lastup_date\": \"" + lastUpdate + "\"," +
                    "\"page_number\": " + page + "}";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

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
                return false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                return false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.barkodAktarimAdetText.setText("Barkod tablosu güncelleniyor. Güncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllBarkod(lastUpdate, ++page, count).execute("http://192.127.2.194:3000/db/barkodlar");
            } else {
                if (count != 0) {
                    binding.barkodAktarimAdetText.setText(count + " adet barkod kaydı güncellendi. Aktarım tamamlandı.");
                } else {
                    binding.barkodAktarimAdetText.setText("Barkod tablosu hali hazırda güncel durumdadır.");
                }


            }

        }
    }

    public class GettingAllEtiketFiyat extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "ServerConnection";

        private int page;
        private int count;
        private String lastUpdate;
        private String jsonStr;

        public GettingAllEtiketFiyat(String lastUpdate, int page, int count) {
            this.lastUpdate = lastUpdate;
            this.count = count;
            this.page = page;
            this.jsonStr = "{" +
                    "\"bar_lastup_date\": \"" + lastUpdate + "\"," +
                    "\"page_number\": " + page + "}";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

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
                return false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                return false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.barkodAktarimAdetText.setText("Barkod tablosu güncelleniyor. Güncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllBarkod(lastUpdate, ++page, count).execute("http://192.127.2.194:3000/db/barkodlar");
            } else {
                if (count != 0) {
                    binding.barkodAktarimAdetText.setText(count + " adet barkod kaydı güncellendi. Aktarım tamamlandı.");
                } else {
                    binding.barkodAktarimAdetText.setText("Barkod tablosu hali hazırda güncel durumdadır.");
                }


            }

        }
    }

    public class GettingAllIndirimFiyat extends AsyncTask<String, Integer, Boolean> {
        private final String TAG = "ServerConnection";

        private int page;
        private int count;
        private String lastUpdate;
        private String jsonStr;

        public GettingAllIndirimFiyat(String lastUpdate, int page, int count) {
            this.lastUpdate = lastUpdate;
            this.count = count;
            this.page = page;
            this.jsonStr = "{" +
                    "\"bar_lastup_date\": \"" + lastUpdate + "\"," +
                    "\"page_number\": " + page + "}";
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

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
                return false;
            } catch (JSONException exception) {
                exception.printStackTrace();
                return false;
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.barkodAktarimAdetText.setText("Barkod tablosu güncelleniyor. Güncellenen kayıt sayısı: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllBarkod(lastUpdate, ++page, count).execute("http://192.127.2.194:3000/db/barkodlar");
            } else {
                if (count != 0) {
                    binding.barkodAktarimAdetText.setText(count + " adet barkod kaydı güncellendi. Aktarım tamamlandı.");
                } else {
                    binding.barkodAktarimAdetText.setText("Barkod tablosu hali hazırda güncel durumdadır.");
                }


            }

        }
    }

}