package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

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

public class LabelActivity extends AppCompatActivity {
    private final String TAG = "LabelActivity";

    private ActivityLabelBinding binding;
    private BarkodTipi barkodTipi;
    private SharedPreferences sharedPreferences;

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    private BarkodBO barkodBO;
    private DepoBO depoBO;
    private StokSatisFiyatBO stokSatisFiyatBO;

    LabelController labelController;
    private LabelDto labelDto;

    private int etiketDepoNo = 24;
    private int depoNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLabelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("tr.com.cetinkaya.handterminal", Context.MODE_PRIVATE);
        depoNo = sharedPreferences.getInt("depoNo", 1);

        Intent intent = getIntent();
        barkodTipi = (BarkodTipi) intent.getSerializableExtra("barkodTipi");
        setTitle(barkodTipi);

        binding.barkodText.requestFocus();

        initDatabase();

        binding.barkodText.setOnKeyListener(new View.OnKeyListener() {
            AlertDialog.Builder alert = new AlertDialog.Builder(LabelActivity.this);

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                String message = "";

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    clearFields();
                    LabelDto labelDto = getLabelInformation(binding.barkodText.getText().toString());
                    if (labelDto != null) {
                        if (isLabelGrocery()) {
                            if (labelDto.isReyonGrocery()) {
                                fillLabelInformation(labelDto);
                                return true;
                            }
                            message = "Bu bölümden sadece market etiketleri için çıktı alınabilir.\n\n" +
                                    "Okutulan barkodun market etiketi olduğuna emin iseniz, lütfen " +
                                    "bilgi işlem departmanına barkod numarası ile durumu bildiriniz.";
                        } else if (isLabelClothes()) {
                            if (labelDto.isReyonClothes()) {
                                fillLabelInformation(labelDto);
                                return true;
                            }
                            message = "Bu bölümden sadece giyim etiketleri için çıktı alınabilir.\n\n" +
                                    "Okutulan barkodun market etiketi olduğuna emin iseniz, lütfen " +
                                    "biilgi işlem departmanına barkod numarası ile durumu bildiriniz.";
                        }
                    } else {
                        message =
                                "Barkod numarasına ait etiket bilgisi bulunamadı. " +
                                        "Lütfen güncelleme yaptıktan sonra tekrar deneyiniz.\n\n" +
                                        "Sorun devam ederse bilgi işlem departmanına barkod numarası ile durumu bildiriniz.";
                    }
                    alert.setTitle("Dikkat")
                            .setMessage(message)
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    binding.barkodText.setText("");
                                }
                            }).show();
                }

                return false;
            }
        });

    }

    private void initDatabase() {
        sqLiteHelper = new SQLiteHelper(LabelActivity.this);
        db = sqLiteHelper.getReadableDatabase();
        barkodBO = new BarkodBO(new BarkodSQLiteDao(db));
        depoBO = new DepoBO(new DepoSQLiteDao(db));
        stokSatisFiyatBO = new StokSatisFiyatBO(new StokSatisFiyatSQLiteDao(db));
        labelController = new LabelController(barkodBO, depoBO, stokSatisFiyatBO);
    }

    private void clearFields() {
        binding.stokKodutext.setText("");
        binding.stokAdiText.setText("");
        binding.bedenText.setText("");
        binding.satisText.setText("");
        binding.indirimText.setText("");
        binding.taksitText.setText("");
        binding.birimText.setText("");
    }

    private boolean isLabelGrocery() {
        return barkodTipi.equals(BarkodTipi.INDIRIMSIZ_RAF) || barkodTipi.equals(BarkodTipi.INDIRIMLI_RAF);
    }

    private boolean isLabelClothes() {
        return barkodTipi.equals(BarkodTipi.BARKODSUZ_BEYAZ) || barkodTipi.equals(BarkodTipi.BARKODLU_BEYAZ)
                || barkodTipi.equals(BarkodTipi.BARKODSUZ_KIRMIZI_INDIRIM) || barkodTipi.equals(BarkodTipi.BARKODLU_KIRMIZI_INDIRIM);
    }

    private LabelDto getLabelInformation(String barkod) {
        if (barkod.isEmpty()) {
            return null;
        }
        return labelController.getLabel(barkod, etiketDepoNo, depoNo);
    }

    private void fillLabelInformation(LabelDto labelDto) {
        binding.stokKodutext.setText(labelDto.getStokKodu());
        binding.stokAdiText.setText(labelDto.getStokAdi());
        binding.bedenText.setText(labelDto.getBeden());
        binding.satisText.setText(
                String.format("%.2f TL", labelDto.getSatisFiyati()));
        binding.indirimText.setText(
                String.format("%.2f TL", labelDto.getIndirimFiyati()));
        binding.taksitText.setText(
                String.format("%.2f", labelDto.getTaksitFiyati()));
        binding.birimText.setText(
                String.format("%.2f TL / %s",
                        labelDto.getBirimFiyati(), labelDto.getBirimAdi()));
        binding.barkodText.selectAll();
    }

    private void setTitle(BarkodTipi barkodTipi) {
        String title = "";
        switch (barkodTipi) {
            case BARKODSUZ_KIRMIZI_INDIRIM:
                title = "Kırmızı Bsuz. İndirim Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                break;
            case BARKODLU_KIRMIZI_INDIRIM:
                title = "Kırmızı Blu. İndirim Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                break;
            case BARKODSUZ_BEYAZ:
                title = "Beyaz Bsuz. Fiyat Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.indirimLabel.setVisibility(View.GONE);
                binding.indirimText.setVisibility(View.GONE);
                break;
            case BARKODLU_BEYAZ:
                title = "Beyaz Blu. Fiyat Etiketi";
                binding.birimLabel.setVisibility(View.GONE);
                binding.birimText.setVisibility(View.GONE);
                binding.indirimLabel.setVisibility(View.GONE);
                binding.indirimText.setVisibility(View.GONE);
                break;
            case INDIRIMLI_RAF:
                title = "İndirimli Raf Etiketi";
                binding.bedenText.setVisibility(View.GONE);
                binding.bedenLabel.setVisibility(View.GONE);
                binding.taksitLabel.setVisibility(View.GONE);
                binding.taksitText.setVisibility(View.GONE);
                break;
            case INDIRIMSIZ_RAF:
                title = "Raf Etiketi";
                binding.bedenText.setVisibility(View.GONE);
                binding.bedenLabel.setVisibility(View.GONE);
                binding.taksitLabel.setVisibility(View.GONE);
                binding.taksitText.setVisibility(View.GONE);
                binding.indirimLabel.setVisibility(View.GONE);
                binding.indirimText.setVisibility(View.GONE);
                break;
        }
        binding.labelActivityTitleText.setText(title);
    }

    public void closeLabelActivity(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}