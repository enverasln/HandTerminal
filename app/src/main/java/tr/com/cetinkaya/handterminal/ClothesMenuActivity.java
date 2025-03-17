package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tr.com.cetinkaya.handterminal.databinding.ActivityClothesMenuBinding;
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
                barkodTipi = BarkodTipi.BARKODSUZ_TAKSITLI_FIYAT;
                break;
            case R.id.buttonInstalmentPriceTagWithBarcode:
                barkodTipi = BarkodTipi.BARKODLU_TAKSITLI_FIYAT;
                break;
            case R.id.buttonInstalmentPriceRedTagWithBarcode:
                barkodTipi = BarkodTipi.BARKODLU_KIRMIZI_TAKSITLI_FIYAT;
                break;
            case R.id.buttonInstalmentPriceRedTagWithoutBarcode:
                barkodTipi = BarkodTipi.BARKODSUZ_KIRMIZI_TAKSITLI_FIYAT;
                break;
            case R.id.buttonInstalmentPriceRedTagWithoutBarcode_50:
                barkodTipi = BarkodTipi.BARKODSUZ_KIRMIZI_50;
                break;
            case R.id.buttonInstalmentPriceRedTagWithBarcode_50:
                barkodTipi = BarkodTipi.BARKODLU_KIRMIZI_50;
                break;



            case R.id.buttonInstalmentPriceRedTagWithBarcode_51:
                barkodTipi = BarkodTipi.BARKODLU_KIRMIZI_TAKSITLI_50;
                break;
            case R.id.buttonInstalmentPriceRedTagWithoutBarcode_51:
                barkodTipi = BarkodTipi.BARKODSUZ_KIRMIZI_TAKSITLI_50;
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