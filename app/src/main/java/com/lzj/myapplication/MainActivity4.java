package com.lzj.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity4 extends AppCompatActivity {
    private Intent to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        to = getIntent();
        double ord_dollar = to.getDoubleExtra("dollarRate",0.14);
        double ord_euro = to.getDoubleExtra("euroRate",0.92);
        double ord_won = to.getDoubleExtra("wonRate",186.92);
        EditText edit_dollar = findViewById(R.id.edit_dollarRateNew);
        EditText edit_euro = findViewById(R.id.edit_euroRateNew);
        EditText edit_won = findViewById(R.id.edit_wonRateNew);

        edit_dollar.setText(String.valueOf(ord_dollar));
        edit_euro.setText(String.valueOf(ord_euro));
        edit_won.setText(String.valueOf(ord_won));
    }
    public void clickBack(View btn)
    {
        EditText edit_dollar = findViewById(R.id.edit_dollarRateNew);
        EditText edit_euro = findViewById(R.id.edit_euroRateNew);
        EditText edit_won = findViewById(R.id.edit_wonRateNew);

        double rate1 = Double.parseDouble(edit_dollar.getText().toString());
        double rate2 = Double.parseDouble(edit_euro.getText().toString());
        double rate3 = Double.parseDouble(edit_won.getText().toString());

        Bundle bdl = new Bundle();
        bdl.putDouble("newRate1",rate1);
        bdl.putDouble("newRate2",rate2);
        bdl.putDouble("newRate3",rate3);
        to.putExtras(bdl);
        setResult(100,to);
        finish();
    }
}