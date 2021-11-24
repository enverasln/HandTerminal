package tr.com.cetinkaya.handterminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import tr.com.cetinkaya.handterminal.databinding.ActivityClothesMenuBinding;
import tr.com.cetinkaya.handterminal.databinding.ActivityMainBinding;
import tr.com.cetinkaya.handterminal.helpers.BarkodTipi;

public class ClothesMenuActivity extends AppCompatActivity  {
    private ActivityClothesMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClothesMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

    public void openLabelPage(View view) {
        Intent intent = new Intent(this, LabelActivity.class);
        BarkodTipi barkodTipi;
        switch (view.getId()) {
            case R.id.buttonRedDiscountWithoutBarcode:
                barkodTipi = BarkodTipi.BARKODSUZ_KIRMIZI_INDIRIM;
                break;
            case R.id.buttonRedDiscountWithBarcode:
                barkodTipi = BarkodTipi.BARKODLU_KIRMIZI_INDIRIM;
                break;
            case R.id.buttonWhiteWithoutBarcode:
                barkodTipi = BarkodTipi.BARKODSUZ_BEYAZ;
                break;
            case R.id.buttonInstalmentPriceTag:
                barkodTipi = BarkodTipi.TAKSITLI_FIYAT;
                break;
            default:
                barkodTipi = BarkodTipi.BARKODLU_BEYAZ;
                break;
        }
        intent.putExtra("barkodTipi", barkodTipi);
        startActivity(intent);
        finish();
    }

    public void closeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}