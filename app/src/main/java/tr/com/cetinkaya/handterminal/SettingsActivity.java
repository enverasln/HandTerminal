package tr.com.cetinkaya.handterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tr.com.cetinkaya.handterminal.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void closeSettingsActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void openPrinterSettingsActivity(View view) {
        Intent intent = new Intent(this, PrinterSettingActivity.class);
        startActivity(intent);
        finish();
    }

    public void openUpdateDataSettingsActivity(View view) {
        Intent intent = new Intent(this, UpdateDataActivity.class);
        startActivity(intent);
        finish();
    }

    public void openDatabaseSettingsActivity(View view) {
        Intent intent = new Intent(this, OtherSettingsActivity.class);
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