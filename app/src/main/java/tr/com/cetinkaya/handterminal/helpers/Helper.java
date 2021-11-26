package tr.com.cetinkaya.handterminal.helpers;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Helper {
    private static final String TAG = "HELPER";
    public static final String API_URL = "http://192.127.1.82:5000/api/mobile-tag/db/";
    public static final String APK_URL = "http://192.127.1.82:5000/mobile-tag/";



    private static void backupDatabase() {

    }

    public static void loadDatabase(String source, String destination) throws Exception {

        File inputFile = new File(source);
        if (inputFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(inputFile);
                OutputStream outputStream = new FileOutputStream(destination);
                byte[] buffer = new byte[1024];
                int length;
                while((length = fileInputStream.read(buffer))>0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                fileInputStream.close();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        } else {
            throw new Exception("Veritabanı yedekten döndürülemedi.\n" +
                    "Veritabanı dosyası/klasörü mevcut olmayabilir ya da ismi hatalı yazılmış olabilir.");
        }


    }


}
