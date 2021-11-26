package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import tr.com.cetinkaya.handterminal.business.concretes.KullaniciBO;
import tr.com.cetinkaya.handterminal.daos.concretes.KullaniciSQLiteDao;
import tr.com.cetinkaya.handterminal.helpers.Helper;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;

public class OtherSettingsActivity extends AppCompatActivity {

    private DownloadManager mgr = null;
    private long lastDownload = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);


        mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);
    }

    public void startLogoDownload(View v) {
        startDownload("imgs/yerli100", "yerliuretim100.png");
        startDownload("imgs/yerli120", "yerliuretim120.png");

        v.setEnabled(false);

    }

    public void startDbDownload(View v) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("tr.com.cetinkaya.handterminal", MODE_PRIVATE);
        int depoNo = sharedPreferences.getInt("depoNo",0);
        startDownload("db/"+depoNo , "BarkodEtiketDB.db");
        v.setEnabled(false);
        //findViewById(R.id.query).setEnabled(true);
    }

    public void loadDb(View v) {
        v.setEnabled(false);
        loadDatabase();
        v.setEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void startDownload(String strUri, String fileName) {
        Uri uri = Uri.parse(Helper.APK_URL + strUri);

        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/MobilEtiket/" + fileName;
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(), fileName);
        File file = new File(filepath);
        if (file.exists()) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert
                    .setTitle("Dikkat")
                    .setMessage(fileName + " dosyası silinecektir.\n" +
                            "Sunucuda bulunan dosya yüklenecektir.\n\n" +
                            "Onaylıyor musunuz?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            file.delete();

                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .mkdirs();

                            lastDownload =
                                    mgr.enqueue(new DownloadManager.Request(uri)
                                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                                    DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedOverRoaming(false)
                                            .setTitle("Demo")
                                            .setDescription("Something useful. No, really.")
                                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,
                                                    "/MobilEtiket/" + fileName));

                        }
                    })
                    .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            findViewById(R.id.yerliUretimLogoIndirButton).setEnabled(true);
                            findViewById(R.id.DatabaseIndirButton).setEnabled(true);
                        }
                    })
                    .show();
        } else {
            Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .mkdirs();

            lastDownload =
                    mgr.enqueue(new DownloadManager.Request(uri)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Demo")
                            .setDescription("Something useful. No, really.")
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,
                                    "/MobilEtiket/" + fileName));
        }

    }


    @SuppressLint("Range")
    public void queryStatus(View v) {
        Cursor c = mgr.query(new DownloadManager.Query().setFilterById(lastDownload));

        if (c == null) {
            Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show();
        } else {
            c.moveToFirst();

            Log.d(getClass().getName(), "COLUMN_ID: " +
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
            Log.d(getClass().getName(), "COLUMN_BYTES_DOWNLOADED_SO_FAR: " +
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
            Log.d(getClass().getName(), "COLUMN_LAST_MODIFIED_TIMESTAMP: " +
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
            Log.d(getClass().getName(), "COLUMN_LOCAL_URI: " +
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
            Log.d(getClass().getName(), "COLUMN_STATUS: " +
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
            Log.d(getClass().getName(), "COLUMN_REASON: " +
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));

            Toast.makeText(this, statusMessage(c), Toast.LENGTH_LONG).show();
        }
    }


    @SuppressLint("Range")
    private String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            findViewById(R.id.yerliUretimLogoIndirButton).setEnabled(true);
            findViewById(R.id.DatabaseIndirButton).setEnabled(true);
        }
    };

    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

    private void loadDatabase() {
        Toast.makeText(this, "Yedekten dönülüyor", Toast.LENGTH_LONG).show();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        KullaniciBO kullaniciBO = new KullaniciBO(new KullaniciSQLiteDao(db));

        int count = kullaniciBO.getCount();
        System.out.println(count);

        try {
            String sourceDB = String.format("%s/MobilEtiket/%s.db",
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(), SQLiteHelper.DATABASE_NAME);
            String destinationDB = getDatabasePath(SQLiteHelper.DATABASE_NAME).toString();
            Helper.loadDatabase(sourceDB, destinationDB);
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        Toast.makeText(this, "Yedekten dönme işlemi tamamlanmıştır.", Toast.LENGTH_LONG).show();
    }


    public class LoadDatabaseTast extends AsyncTask<Integer, Integer, Integer> {


        @Override
        protected Integer doInBackground(Integer... integers) {
            return null;
        }
    }

}