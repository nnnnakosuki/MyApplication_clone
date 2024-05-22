package com.lzj.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    int ans = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View v){
        setContentView(R.layout.activity_main);
        TextView out1 = findViewById(R.id.score);
//        int ans1 = Integer.parseInt(out1.getText().toString());
        ans += 1;
        out1.setText(String.valueOf(ans));
        Log.i("main","click1:");
    }
    public void click2(View v){
        setContentView(R.layout.activity_main);
        TextView out2 = findViewById(R.id.score);
//        int ans2 = Integer.parseInt(out2.getText().toString());
        ans += 2;
        out2.setText(String.valueOf(ans));
    }
    public void click3(View v){
        setContentView(R.layout.activity_main);
        TextView out3 = findViewById(R.id.score);
//        int ans = Integer.parseInt(out3.getText().toString());
        ans += 3;
        out3.setText(String.valueOf(ans));
    }
    public void click4(View v){
        setContentView(R.layout.activity_main);
        TextView out = findViewById(R.id.score);
        ans = 0;
        out.setText(String.valueOf(ans));
    }
}