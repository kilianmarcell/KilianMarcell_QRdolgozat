package hu.petrik.qrdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button scanButton, kiirButton;
    private TextView szovegTextView;
    private boolean writePermission;
    private static final String fajlnev = "scannedCodes.csv";

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

        kiirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writePermission) {
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String fileSzoveg = szovegTextView.getText().toString() + ", " + format.format(date);

                    try {
                        FileOutputStream fileOutputStream = openFileOutput(fajlnev, MODE_PRIVATE);
                        fileOutputStream.write(fileSzoveg.getBytes());

                        Toast.makeText(MainActivity.this, "Sikeresen elmentve: " + getFilesDir() + "/scannedCodes.csv", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "A f??jlba??r??shoz enged??lyezze a f??jl hozz??f??r??st!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            String s = szovegTextView.getText().toString();

            szovegTextView.setText((result.getContents()));
            try {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
                Log.d("URI ERROR", e.toString());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init() {
        scanButton = findViewById(R.id.scanButton);
        kiirButton = findViewById(R.id.kiirButton);
        szovegTextView = findViewById(R.id.szovegTextView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                writePermission = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            writePermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}