package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tr.com.cetinkaya.handterminal.databinding.ActivityGroceryMenuBinding;
import tr.com.cetinkaya.handterminal.helpers.BarkodTipi;
import tr.com.cetinkaya.handterminal.helpers.BluetoothHelper;

public class GroceryMenuActivity extends AppCompatActivity {
    private ActivityGroceryMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_menu);

    }

    public void openLabelPage(View view) {


        Intent intent = new Intent(this, LabelActivity.class);
        BarkodTipi barkodTipi;
        switch (view.getId()) {
            case R.id.buttonShelfWithDiscount:
                barkodTipi = BarkodTipi.INDIRIMLI_RAF;
                break;
            default:
                barkodTipi = BarkodTipi.INDIRIMSIZ_RAF;
                break;
        }
        intent.putExtra("barkodTipi", barkodTipi);
        startActivity(intent);


    }

    public void closeActivity(View view) {
        finish();
    }
}