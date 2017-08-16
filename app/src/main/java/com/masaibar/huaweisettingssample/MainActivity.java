package com.masaibar.huaweisettingssample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_has_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = new HuaweiUtil(getApplicationContext()).hasProtectedAppSetting();
                Toast.makeText(MainActivity.this, String.valueOf(result), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button_open_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HuaweiUtil(getApplicationContext()).openProtectedSettings();
            }
        });
    }
}
