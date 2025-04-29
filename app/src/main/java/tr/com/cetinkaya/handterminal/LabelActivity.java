package tr.com.cetinkaya.handterminal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import tr.com.cetinkaya.handterminal.business.concretes.BarkodBO;
import tr.com.cetinkaya.handterminal.business.concretes.DepoBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.controllers.concretes.LabelController;
import tr.com.cetinkaya.handterminal.daos.concretes.BarkodSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.DepoSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.StokSatisFiyatSQLiteDao;
import tr.com.cetinkaya.handterminal.databinding.ActivityLabelBinding;
import tr.com.cetinkaya.handterminal.dtos.LabelDto;
import tr.com.cetinkaya.handterminal.helpers.BarkodTipi;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.printers.SewooPrinterAdapter;

public class LabelActivity extends AppCompatActivity {
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp";
    private static final String fileName = dir + "/BTPrinter";

    int count = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private ActivityLabelBinding binding;
    private BarkodTipi barkodTipi;
    private int etiketDepoNo = 24;
    private int depoNo;
    private SharedPreferences sharedPreferences;

    SQLiteHelper sqLiteHelper;
    SQLiteDatabase db;
    private LabelController labelController;
    LabelDto labelDto;

    private BroadcastReceiver connectDevice;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothPort bluetoothPort;
    private Thread btThread;

    private boolean disconnectflags;

    private String str_SavedBT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLabelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("tr.com.cetinkaya.handterminal", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        barkodTipi = (BarkodTipi) intent.getSerializableExtra("barkodTipi");
        depoNo = sharedPreferences.getInt("depoNo", 1);
        setTitle(barkodTipi);

        sqLiteHelper = new SQLiteHelper(LabelActivity.this);
        db = sqLiteHelper.getReadableDatabase();


        BarkodBO barkodBO = new BarkodBO(new BarkodSQLiteDao(db));
        DepoBO depoBO = new DepoBO(new DepoSQLiteDao(db));
        StokSatisFiyatBO stokSatisFiyatBO = new StokSatisFiyatBO(new StokSatisFiyatSQLiteDao(db));
        labelController = new LabelController(barkodBO, depoBO, stokSatisFiyatBO);


        loadSettingFile();

        bluetoothPort = BluetoothPort.getInstance();

        Init_BluetoothSet();


        binding.barkodText.requestFocus();

        binding.barkodText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getStokInformation();
                    return true;
                }
                return false;
            }
        });

    }

    private void loadSettingFile() {
        String line;
        BufferedReader fReader;
        try {
            fReader = new BufferedReader(new FileReader(fileName));
            while ((line = fReader.readLine()) != null) {
                str_SavedBT = line;
                break;
            }
            fReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public void ConnectionFailedDevice() {


        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert
                .setTitle("Dikkat")
                .setMessage("Bluetooh bağlanatısı koptu. Bağlantıyı kontrol ediniz ve tekrar deneyiniz.")
                .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LabelActivity.this, ClothesMenuActivity.class);
                        if (barkodTipi.equals(BarkodTipi.INDIRIMLI_RAF) || barkodTipi.equals(BarkodTipi.INDIRIMSIZ_RAF) || barkodTipi.equals(BarkodTipi.ZUCCACIYE_RAF)) {
                            intent = new Intent(LabelActivity.this, GroceryMenuActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }

    private void bluetoothSetup() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            try {
                btConn(mBluetoothAdapter.getRemoteDevice(str_SavedBT));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void Init_BluetoothSet() {
        bluetoothSetup();

        connectDevice = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    //Toast.makeText(getApplicationContext(), "BlueTooth Connect", Toast.LENGTH_SHORT).show();
                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    try {
                        if (bluetoothPort.isConnected())
                            bluetoothPort.disconnect();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if ((btThread != null) && (btThread.isAlive())) {
                        btThread.interrupt();
                        btThread = null;
                    }

                    ConnectionFailedDevice();

                    //Toast.makeText(getApplicationContext(), "BlueTooth Disconnect", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    private void btConn(final BluetoothDevice btDev) throws IOException {
        new connBT().execute(btDev);
    }

    class connBT extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(LabelActivity.this);
        AlertDialog.Builder alert = new AlertDialog.Builder(LabelActivity.this);

        String str_temp = "";

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Yazıcıya bağlanıyor.");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;

            try {

                bluetoothPort.connect(params[0]);
                str_temp = params[0].getAddress();

                retVal = Integer.valueOf(0);
            } catch (IOException e) {
                e.printStackTrace();
                retVal = Integer.valueOf(-1);
            }

            return retVal;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (dialog.isShowing())
                dialog.dismiss();

            if (result.intValue() == 0)    // Connection success.
            {
                RequestHandler rh = new RequestHandler();
                btThread = new Thread(rh);
                btThread.start();

                str_SavedBT = str_temp;

                registerReceiver(connectDevice, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
                registerReceiver(connectDevice, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));

                /*Intent in = new Intent(PrinterSettingActivity.this, HomeActivity.class);
                in.putExtra("Connection", "BlueTooth");
                startActivity(in);*/
            } else    // Connection failed.
            {
                alert
                        .setTitle("Error")
                        .setMessage("Yazıcı bağlantısı sağlanamadı. Lütfen tekrar girişi yapınız.\n\n" +
                                "Durum devam ederse Aylar > Yazıcı Ayarları Menüsüne girerek yazıcı seçiniz ve tekrar deneyiniz.")
                        .setNegativeButton("Tamam", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(LabelActivity.this, ClothesMenuActivity.class);
                                if (barkodTipi.equals(BarkodTipi.INDIRIMLI_RAF) || barkodTipi.equals(BarkodTipi.INDIRIMSIZ_RAF)) {
                                    intent = new Intent(LabelActivity.this, GroceryMenuActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
            }
            super.onPostExecute(result);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        try {

            if (bluetoothPort.isConnected()) {
                bluetoothPort.disconnect();
                unregisterReceiver(connectDevice);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ((btThread != null) && (btThread.isAlive())) {
            btThread.interrupt();
            btThread = null;
        }
    }


    private void getStokInformation() {

        labelDto = labelController.getLabel(binding.barkodText.getText().toString(), etiketDepoNo, depoNo);


        if (labelDto != null) {
            binding.stokKodutext.setText(labelDto.getStokKodu());
            binding.stokAdiText.setText(labelDto.getStokAdi());
            binding.bedenText.setText(labelDto.getBeden());
            binding.satisFiyatiText.setText(String.format("%.2f TL", labelDto.getSatisFiyati()));
            binding.etiketFiyatText.setText(String.format("%.2f TL", labelDto.getEtiketFiyati()));
            binding.taksitliFiyatText.setText(String.format("%.2f TL", labelDto.getTaksitFiyati()));
        }

        if (barkodTipi == BarkodTipi.INDIRIMSIZ_RAF) {
            binding.birimText.setText(String.format("%.2f TL/%s", labelDto.getBirimFiyati(), labelDto.getBirimAdi()));
        } else if (barkodTipi == BarkodTipi.INDIRIMLI_RAF) {
            binding.birimText.setText(String.format("%.2f TL/%s", labelDto.getBirimFiyati(), labelDto.getBirimAdi()));
        } else if (barkodTipi == BarkodTipi.ZUCCACIYE_RAF) {
            binding.birimText.setText(String.format("%.2f TL/%s", labelDto.getBirimFiyati(), labelDto.getBirimAdi()));
        }

        binding.barkodText.selectAll();


    }

    private void setTitle(BarkodTipi barkodTipi) {
        String title = "";
        switch (barkodTipi) {
            case BARKODSUZ_KIRMIZI_INDIRIM:
                title = "Kırmızı Bsuz. İndirim Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case BARKODLU_KIRMIZI_INDIRIM:
                title = "Kırmızı Blu. İndirim Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case BARKODSUZ_BEYAZ:
                title = "Beyaz Bsuz. Fiyat Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.etiketLabel.setVisibility(View.GONE);
                binding.etiketFiyatText.setVisibility(View.GONE);
                break;
            case BARKODLU_BEYAZ:
                title = "Beyaz Blu. Fiyat Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.etiketLabel.setVisibility(View.GONE);
                binding.etiketFiyatText.setVisibility(View.GONE);
                break;
            case DIKEY_BARKODLU_BEYAZ:
                title = "Dikey Beyaz Blu. Etiket";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.etiketLabel.setVisibility(View.GONE);
                binding.etiketFiyatText.setVisibility(View.GONE);
                break;
            case INDIRIMLI_RAF:
                title = "İndirimli Raf Etiketi";
                binding.bedenText.setVisibility(View.GONE);
                binding.bedenLabel.setVisibility(View.GONE);
                binding.taksitLabel.setVisibility(View.GONE);
                binding.taksitliFiyatText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case INDIRIMSIZ_RAF:
                title = "Raf Etiketi";
                binding.bedenText.setVisibility(View.GONE);
                binding.bedenLabel.setVisibility(View.GONE);
                binding.taksitLabel.setVisibility(View.GONE);
                binding.taksitliFiyatText.setVisibility(View.GONE);
                binding.etiketLabel.setVisibility(View.GONE);
                binding.etiketFiyatText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;

            case ZUCCACIYE_RAF:
                title = "Züccaciye Raf Etiketi";
                binding.bedenText.setVisibility(View.GONE);
                binding.bedenLabel.setVisibility(View.GONE);
                binding.taksitLabel.setVisibility(View.GONE);
                binding.taksitliFiyatText.setVisibility(View.GONE);
                binding.etiketLabel.setVisibility(View.GONE);
                binding.etiketFiyatText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;

            case BARKODSUZ_TAKSITLI_FIYAT:
                title = "Barkodsuz Taks. Satış Fiyatı";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case BARKODLU_TAKSITLI_FIYAT:
                title = "Barkodlu Taks. Satış Fiyatı";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case BARKODLU_KIRMIZI_TAKSITLI_FIYAT:
                title = "Kırmızı Barkodlu Taks. Satış Fiyatı";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case BARKODSUZ_KIRMIZI_TAKSITLI_FIYAT:
                title = "Kırmızı Barkodsuz Taks. Satış Fiyatı";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
            case BARKODSUZ_KIRMIZI_50:
                title = "Kırmızı Barkodsuz %50 İndirim";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.printDoubleLableCheckBox.setVisibility(View.GONE);
                break;
        }
        binding.labelActivityTitleText.setText(title);
    }

    public void closeLabelActivity(View view) {
        Intent intent = new Intent(this, ClothesMenuActivity.class);
        if (barkodTipi.equals(BarkodTipi.INDIRIMLI_RAF) || barkodTipi.equals(BarkodTipi.INDIRIMSIZ_RAF) || barkodTipi.equals(BarkodTipi.ZUCCACIYE_RAF)) {
            intent = new Intent(this, GroceryMenuActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ClothesMenuActivity.class);
        if (barkodTipi.equals(BarkodTipi.INDIRIMLI_RAF) || barkodTipi.equals(BarkodTipi.INDIRIMSIZ_RAF) || barkodTipi.equals(BarkodTipi.ZUCCACIYE_RAF)) {
            intent = new Intent(this, GroceryMenuActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void printLabel(View view) {
        SewooPrinterAdapter printer = new SewooPrinterAdapter();
        int re_val = 0;
        int input_count = 1;

        final LinearLayout linear_popup = (LinearLayout) View.inflate(this, R.layout.input_layout, null);
        final EditText edit_input = (EditText) linear_popup.findViewById(R.id.EditTextPopup);

        edit_input.setText(Integer.toString(count));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert
                .setTitle("Etiket Sayısı")
                .setView(linear_popup)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String input_data = edit_input.getText().toString();
                        count = check_data(input_data);

                        switch (barkodTipi) {
                            case BARKODSUZ_KIRMIZI_INDIRIM:
                                if (binding.printDoubleLableCheckBox.isChecked()) {
                                } else {
                                    printer.printBarkodsuzKirmiziEtiket(labelDto, count);
                                }

                                break;
                            case BARKODLU_KIRMIZI_INDIRIM:
                                printer.printBarkodluKirmiziEtiket(labelDto, count);
                                break;
                            case BARKODSUZ_BEYAZ:
                                if (binding.printDoubleLableCheckBox.isChecked()) {
                                    count = (int) Math.ceil(count / 2.0);
                                    printer.printCiftBarkodsuzBeyazEtiket(labelDto, count);
                                } else {
                                    printer.printBarkodsuzBeyazEtiket(labelDto, count);
                                }
                                break;
                            case BARKODLU_BEYAZ:
                                if (binding.printDoubleLableCheckBox.isChecked()) {
                                    count = (int) Math.ceil(count / 2.0);
                                    printer.printCiftBarkodluBeyazEtiket(labelDto, count);
                                } else {
                                    printer.printBarkodluBeyazEtiket(labelDto, count);
                                }
                                break;
                            case DIKEY_BARKODLU_BEYAZ:
                                printer.printDikeyBarkodluBeyazEtiket(labelDto, count);
                                break;

                            case DIKEY_BARKODLU_KIRMIZI_INDIRIM:
                                printer.printDikeyKirmiziBarkodluTaksitliEtiket(labelDto, count);
                                break;
                            case BARKODSUZ_TAKSITLI_FIYAT:
                                printer.printBarkodsuzTaksitliEtiket(labelDto, count);
                                break;
                            case BARKODLU_TAKSITLI_FIYAT:
                                printer.printBarkodluTaksitliEtiket(labelDto, count);
                                break;
                            case BARKODLU_KIRMIZI_TAKSITLI_FIYAT:
                                printer.printKirmiziBarkodluTaksitliEtiket(labelDto, count);
                                break;
                            case BARKODSUZ_KIRMIZI_TAKSITLI_FIYAT:
                                printer.printKirimiziBarkodsuzTaksitliEtiket(labelDto, count);
                                break;
                            case INDIRIMSIZ_RAF:
                                printer.printIndirimsizRafEtiket(labelDto, count);
                                break;
                            case ZUCCACIYE_RAF:
                                printer.printZuccaciyeRafEtiket(labelDto, count);
                                break;
                            case INDIRIMLI_RAF:
                                printer.printIndirimliRafEtiket(labelDto, count);
                                break;
                            case BARKODSUZ_KIRMIZI_50:
                                printer.printIndirimliRafEtiket_50(labelDto, count);
                                break;
                            case BARKODLU_KIRMIZI_50:
                                printer.printBarkodluKirmiziEtiket_50(labelDto, count);
                                break;
                            case BARKODLU_KIRMIZI_TAKSITLI_50:
                                printer.printKirmiziBarkodluTaksitliEtiket_50(labelDto, count);
                                break;
                            case BARKODSUZ_KIRMIZI_TAKSITLI_50:
                                printer.printKirimiziBarkodsuzTaksitliEtiket_50(labelDto, count);
                                break;

                        }


                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();


    }


    public int check_data(String str) {
        int input_num;

        if (str.equals(""))
            input_num = 1;
        else
            input_num = Integer.parseInt(str);

        return input_num;
    }
}