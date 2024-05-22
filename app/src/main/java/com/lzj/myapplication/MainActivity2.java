package com.lzj.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.OutputStream;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        TextView out1 = findViewById(R.id.teamAScore);
        TextView out2 = findViewById(R.id.teamBScore);
        int ans1 = Integer.parseInt(out1.getText().toString());
        int ans2 = Integer.parseInt(out2.getText().toString());
        outState.putInt("ans1",ans1);
        outState.putInt("ans2",ans2);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        TextView out1 = findViewById(R.id.teamAScore);
        TextView out2 = findViewById(R.id.teamBScore);
        int ans1 = savedInstanceState.getInt("ans1",0);
        int ans2 = savedInstanceState.getInt("ans2",0);
        out1.setText(ans1);
        out2.setText(ans2);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void score(View btn){
        int btnN = btn.getId();
        TextView out1 = findViewById(R.id.teamAScore);
        TextView out2 = findViewById(R.id.teamBScore);
        if(btnN == R.id.btn_score3){
            int ans = Integer.parseInt(out1.getText().toString()) + 3;
            out1.setText(String.valueOf(ans));
        }
        if(btnN == R.id.btn_score2){
            int ans = Integer.parseInt(out1.getText().toString()) + 2;
            out1.setText(String.valueOf(ans));
        }
        if(btnN == R.id.btn_score1){
            int ans = Integer.parseInt(out1.getText().toString()) + 1;
            out1.setText(String.valueOf(ans));
        }
        if(btnN == R.id.btn_score6){
            int ans = Integer.parseInt(out2.getText().toString()) + 3;
            out2.setText(String.valueOf(ans));
        }
        if(btnN == R.id.btn_score5){
            int ans = Integer.parseInt(out2.getText().toString()) + 2;
            out2.setText(String.valueOf(ans));
        }
        if(btnN == R.id.btn_score4){
            int ans = Integer.parseInt(out2.getText().toString()) + 1;
            out2.setText(String.valueOf(ans));
        }
    }
}