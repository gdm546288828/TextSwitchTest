package com.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.quanzi.tvswitch.OnTextSwitchSelectChangedListener;
import com.quanzi.tvswitch.TextSwitch;

public class MainActivity extends AppCompatActivity {

    TextSwitch textSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSwitch = findViewById(R.id.textSwitch);
        textSwitch.setOnTextSwitchSelectChangedListener(new OnTextSwitchSelectChangedListener() {
            @Override
            public void onTextSwitchSelectChanged(boolean state) {
                if (state)
                    Toast.makeText(MainActivity.this, "开启", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }
}