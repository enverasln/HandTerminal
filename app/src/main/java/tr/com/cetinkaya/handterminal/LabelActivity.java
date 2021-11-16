package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import tr.com.cetinkaya.handterminal.business.concretes.BarkodBO;
import tr.com.cetinkaya.handterminal.business.concretes.DepoBO;
import tr.com.cetinkaya.handterminal.business.concretes.StokSatisFiyatBO;
import tr.com.cetinkaya.handterminal.daos.concretes.BarkodSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.DepoSQLiteDao;
import tr.com.cetinkaya.handterminal.daos.concretes.StokSatisFiyatSQLiteDao;
import tr.com.cetinkaya.handterminal.databinding.ActivityLabelBinding;
import tr.com.cetinkaya.handterminal.helpers.BarkodTipi;
import tr.com.cetinkaya.handterminal.helpers.SQLiteHelper;
import tr.com.cetinkaya.handterminal.models.Barkod;
import tr.com.cetinkaya.handterminal.models.Depo;
import tr.com.cetinkaya.handterminal.models.StokSatisFiyat;

public class LabelActivity extends AppCompatActivity {
    private ActivityLabelBinding binding;
    private BarkodTipi barkodTipi;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLabelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("tr.com.cetinkaya.handterminal", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        barkodTipi = (BarkodTipi) intent.getSerializableExtra("barkodTipi");
        setTitle(barkodTipi);

        binding.barkodText.requestFocus();

        binding.barkodText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getStokInformation();
                    return true;
                }
                return false;
            }
        });

    }

    private void getStokInformation() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(LabelActivity.this);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

        BarkodBO barkodBO = new BarkodBO(new BarkodSQLiteDao(db));
        DepoBO depoBO = new DepoBO(new DepoSQLiteDao(db));
        StokSatisFiyatBO stokSatisFiyatBO = new StokSatisFiyatBO(new StokSatisFiyatSQLiteDao(db));

        Barkod barkod = barkodBO.getBarkodWithBarkod(binding.barkodText.getText().toString());

        int depoNo = sharedPreferences.getInt("depoNo", 1);
        Depo indirimDepo = depoBO.getDepoById(24);
        Depo etiketDepo = depoBO.getDepoById(depoNo);

        StokSatisFiyat indirimliFiyat = stokSatisFiyatBO.getIndirimliFiyat(barkod, indirimDepo);
        StokSatisFiyat etiketFiyati = stokSatisFiyatBO.getEtiketFiyat(barkod, etiketDepo);
        StokSatisFiyat taksitliFiyati = stokSatisFiyatBO.getTakstiliFiyat(barkod, etiketDepo);

        if(barkod != null) {
            binding.stokKodutext.setText(barkod.getStok().getSto_kod());
            binding.stokAdiText.setText(barkod.getStok().getSto_isim());
            binding.bedenText.setText(barkod.getBar_bedennumarasi());
        }
        if(indirimliFiyat != null) {
            binding.indirimText.setText(indirimliFiyat.getSfiyat_fiyati() + " TL");
        }

        if(etiketFiyati != null) {
            binding.satisText.setText(etiketFiyati.getSfiyat_fiyati() + " TL");
        }

        if(taksitliFiyati != null) {
            binding.taksitText.setText(taksitliFiyati.getSfiyat_fiyati() + " TL");
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
}