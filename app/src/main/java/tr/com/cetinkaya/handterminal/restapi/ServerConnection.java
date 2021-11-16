package tr.com.cetinkaya.handterminal.restapi;

import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class ServerConnection extends AsyncTask<String, Void, Boolean> {
    public static String API_URL = "http://192.127.2.194:3000";

    private final String TAG = "ServerConnection";
    private String URL;
    private List<NameValuePair> params;

    public ServerConnection(String URL, List<NameValuePair> params){
        this.URL = URL;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            HttpGet httpGet = new HttpGet(urls[0]);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response =  httpClient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            if(status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);

                JSONObject jsonObject = new JSONObject(data);
                Log.d(TAG, jsonObject.toString());
                return true;
            }

        } catch(IOException exception) {
            exception.printStackTrace();
        } catch(JSONException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
