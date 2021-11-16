package tr.com.cetinkaya.handterminal.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.sewoo.port.android.BluetoothPort;


import java.io.IOException;
import java.util.Set;
import java.util.Vector;

import tr.com.cetinkaya.handterminal.printers.IPrinterAdapter;

public class BluetoothHelper {
    private static final String TAG = "BluetoothHelper";

    private static final int REQUEST_ENABLE_BT = 2;


    private ActivityResultLauncher<Intent> activityResultLauncher;

    private Context context;

    private BluetoothAdapter bluetoothAdapter;
    private Vector<BluetoothDevice> remoteDevices;
    private BluetoothPort bluetoothPort;
    private Thread btThread;

    private BroadcastReceiver disconnectReceiver;
    private BroadcastReceiver connectDevice;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver searchStart;
    private BroadcastReceiver searchFinish;

    private boolean searchflags;
    private boolean disconnectflags;


    public BluetoothHelper(Context context, BluetoothPort bluetoothPort) {
        this.context = context;
        this.bluetoothPort = bluetoothPort;

        searchflags = false;
        disconnectflags = false;

    }

    private void clearBtDevData() {
        remoteDevices = new Vector<>();
    }

    public boolean bluetoohSetup() {

        clearBtDevData();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bu cihazda Bluetooth desteği bulunmamaktadır.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        } else {
            return true;
        }

    }


    private void SearchingBTDevice()
    {
        clearBtDevData();
        bluetoothAdapter.startDiscovery();
    }

    public void startSearchBt() {
        new CheckTypesTask().execute();
    }

    public void ConnectionFailedDevice() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert
                .setTitle("Error")
                .setMessage("Bluetooth bağlantısı sona erdi.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void initBluetoothSet() {
        connectDevice = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    // Bluetooth connect
                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    try {
                        if (bluetoothPort.isConnected()) {
                            bluetoothPort.disconnect();
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }

                    if (btThread != null && btThread.isAlive()) {
                        btThread.interrupt();
                        btThread = null;
                    }

                    ConnectionFailedDevice();
                }
            }
        };

        discoveryResult = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String key;
                BluetoothDevice btDev;
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (remoteDevice != null) {
                    if (remoteDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "]";
                    } else {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "] [Paired]";
                    }
                    remoteDevices.add(remoteDevice);
                }
            }
        };

        context.registerReceiver(disconnectReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        searchStart = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        context.registerReceiver(searchStart, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));

        searchFinish = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                searchflags = true;
            }
        };

        context.registerReceiver(searchFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }


    private class CheckTypesTask extends AsyncTask<Void, Void, Void>{

        ProgressDialog asyncDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute(){
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Searching the Printer...");
            asyncDialog.setCancelable(false);
            asyncDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            searchflags = true;
                            bluetoothAdapter.cancelDiscovery();
                        }
                    });
            asyncDialog.show();
            SearchingBTDevice();
            super.onPreExecute();
        };

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                while(true)
                {
                    if(searchflags)
                        break;

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(asyncDialog.isShowing())
                asyncDialog.dismiss();

            searchflags = false;
            super.onPostExecute(result);
        };
    }



    class ConnectionBT extends AsyncTask<BluetoothDevice, Void, Integer> {

        @Override
        protected Integer doInBackground(BluetoothDevice... bluetoothDevices) {


            return null;
        }
    }
}
