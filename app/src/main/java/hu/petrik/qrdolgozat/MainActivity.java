package hu.petrik.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button scanButton, kiirButton;
    private TextView szovegTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator i = new IntentIntegrator(MainActivity.this);
                i.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);

                i.initiateScan();
            }
        });
    }

    public void init() {
        scanButton = findViewById(R.id.scanButton);
        kiirButton = findViewById(R.id.kiirButton);
        szovegTextView = findViewById(R.id.szovegTextView);
    }
}