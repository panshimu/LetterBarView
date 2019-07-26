package com.miaozij.letterbarview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = findViewById(R.id.tv);
        LetterBarView letterBarView = findViewById(R.id.lb);
        letterBarView.setOnSelectedLetterListener(new LetterBarView.OnSelectedLetterListener() {
            @Override
            public void selected(String letter) {
                if(!TextUtils.isEmpty(letter)) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(letter);
                }else {
                    tv.setVisibility(View.GONE);
                }
            }
        });
    }
}
