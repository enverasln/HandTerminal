package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;


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

        sqLiteHelper = new SQLiteHelper(this);
        db = sqLiteHelper.getWritableDatabase();
        stokBO = new StokBO(new StokSQLiteDao(db));
        barkodBO = new BarkodBO(new BarkodSQLiteDao(db));

        makeInvisibleInformationLabel();
    }

    private void makeInvisibleInformationLabel() {
        binding.stokAktarimAdetLabel.setVisibility(View.GONE);
        binding.stokAktarimAdetText.setText("");
        binding.barkodAktarimAdetLabel.setVisibility(View.GONE);
        binding.fiyatAktarimAdetLabel.setVisibility(View.GONE);
        binding.depoAktarimAdetLabel.setVisibility(View.GONE);
    }

    public void closeActivity(View view) {
        finish();
    }

    public void updateData(View view) {
        makeInvisibleInformationLabel();
        if (binding.stokCheckBox.isChecked()) {

            String stoLastupDate = stokBO.getLastupDate();
            new GettingAllStok(stoLastupDate, 0, 0).execute("http://192.127.2.194:3000/db/stoklar");
        } else if(binding.barkodCheckBox.isChecked()) {
            //String stoLastupDate = barkodBO.getLastupDate();
            //new GettingAllBarkod(stoLastupDate, 0, 0).execute("http://192.127.2.194:3000/db/barkodlar");
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
            if(binding.stokCheckBox.isChecked()) {
                binding.stokAktarimAdetLabel.setVisibility(View.VISIBLE);
                binding.stokAktarimAdetText.setVisibility(View.VISIBLE);
            }
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
            binding.stokAktarimAdetText.setText( values[0] + " adet stok güncellendi.");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllStok(lastUpdate, ++page, count).execute("http://192.127.2.194:3000/db/stoklar");
            } else {
                binding.stokAktarimAdetText.setText(binding.stokAktarimAdetText.getText().toString() + " Aktarım tamamlandı.");
                if(binding.barkodCheckBox.isChecked()) {
                    //new GettingAllBarkod(lastUpdate, 0, 0).execute("http://192.127.2.194:3000/db/barkodlar");
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

                    // Save the stok to the database
                    for (int i = 0; i < stokJSONArray.length(); i++) {
                        Barkod newBarkod = new Barkod();
                        JSONObject barkod = stokJSONArray.getJSONObject(i);
                        newBarkod.setBar_guid(barkod.getString("bar_guid"));


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
            binding.countText.setText("Count: " + values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                new GettingAllStok(lastUpdate, ++page, count).execute("http://192.127.2.194:3000/db/stoklar");
            } else {
                Log.d(TAG, new Date().toString());
            }

        }
    }





    /*private void getAllStockCount(VolleyCallBack callback, String lastupDate) {
        String url = String.format("%s/db/stoklar/toplam-sayi", Helper.API_URL);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sto_lastup_date", lastupDate);
            final String mRequestBody = jsonBody.toString();

            StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Cevap", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        callback.onSuccess(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Cevap", error.toString());
                }
            }) {
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
        } catch (JSONException exception) {
            exception.printStackTrace();
        }


    }*/

    /*private void getAllStokByLastupDate(VolleyCallBack callBack, String lastupDate, int pageNumber) {
        final List<Stok> stokList = new ArrayList<>();
        String url = String.format("%s/db/stoklar", Helper.API_URL);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sto_lastup_date", lastupDate);
            jsonBody.put("page_number", 1);
            final String mRequestBody = jsonBody.toString();

            StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.e("Cevap", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray stokJsonArray = jsonObject.getJSONArray("data");
                        callBack.onSuccess(stokJsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Cevap", error.toString());
                }
            }) {
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
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

    }*/

    /*private int getAllStockCount(String lastupDate) {
        String url = String.format("%s/db/stoklar/toplam-sayi", Helper.API_URL);
        final int totalCount[] = {0};
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sto_lastup_date", lastupDate);
            final String mRequestBody = jsonBody.toString();

            StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Cevap", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        totalCount[0] = jsonArray.getJSONObject(0).getInt("toplamSayi");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Cevap", error.toString());
                }
            }) {
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
        } catch (JSONException exception) {
            exception.printStackTrace();
        }


        return totalCount[0];
    }

    private List<Stok> getAllStokByLastupDate(VolleyCallBack callBack, String lastupDate, int pageNumber) {
        binding.countText.setText("Güncelleme Başadı");
        final List<Stok> stokList = new ArrayList<>();
        String url = String.format("%s/db/stoklar", Helper.API_URL);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("sto_lastup_date", lastupDate);
            jsonBody.put("page_number", 1);
            final String mRequestBody = jsonBody.toString();

            StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.e("Cevap", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        List<Stok> stokList = new ArrayList<>();
                        JSONArray stokJsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < stokJsonArray.length(); i++) {
                            JSONObject stok = stokJsonArray.getJSONObject(i);
                            SQLiteHelper sqLiteHelper = new SQLiteHelper(UpdateDataActivity.this);
                            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                            StokBO stokBO = new StokBO(new StokSQLiteDao(db));
                            Stok newStok = new Stok();
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
                            newStok.setSto_create_date(stok.getString("sto_create_date"));
                            newStok.setSto_lastup_date(stok.getString("sto_lastup_date"));
                            stokList.add(newStok);
                        }
                        callBack.onSuccess(stokList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Cevap", error.toString());
                }
            }) {
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
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return stokList;

    }*/


    /*private class GetAllStok extends AsyncTask<List<Stok>, Integer, Boolean> {

        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(List<Stok>... params) {
            count= 0;
            for (Stok stok : params[0]) {
                count++;
                if(stokBO.updateStok(stok) == 0) {
                    stokBO.insertStok(stok);
                }
                //onProgressUpdate(count);
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            binding.countText.setText("Başarılı");
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            binding.countText.setText( Integer.toString(values[0]));

            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            System.out.println("İptal Edildi.");
            super.onCancelled();
        }
    }


    public interface VolleyCallBack {
        void onSuccess(List<Stok> stokList);
    }*/
}