package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import tr.com.cetinkaya.handterminal.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sharedPreferences = this.getSharedPreferences("tr.com.cetinkaya.handterminal", MODE_PRIVATE);
        binding.homePageTitleText.setText("Mobil Etiket V27.4- " + sharedPreferences.getString("userDepo",""));

    }

    public void buttonPrintClothesOnClick(View view) {
        Intent intent = new Intent(this, ClothesMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonPrintGroceryOnClick(View view) {
        Intent intent = new Intent(this, GroceryMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }


}