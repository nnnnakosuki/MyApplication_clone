package com.lzj.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity3 extends AppCompatActivity implements Runnable {
    double rate1 = 0.14;
    double rate2 = 0.92;
    double rate3 = 186.92;
    Handler handler;
    String TAG = "AAAAAA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 10){
                    String str = msg.obj.toString();
                    Toast.makeText(MainActivity3.this,"msg=" + str,Toast.LENGTH_LONG).show();
                }
                super.handleMessage(msg);
            }
        };

        Thread t = new Thread(this);
        t.start();

    }
    public void click_rate(View btn){
        int id_label = btn.getId();
        EditText in = findViewById(R.id.input);
        TextView out = findViewById(R.id.output);
        String str = in.getText().toString();
        if(str.length() == 0){
            Toast.makeText(this,"noting you input,please input again with number",Toast.LENGTH_LONG).show();
        } else {

            if(id_label == R.id.btn_dollar) {
                double ans = Double.parseDouble(in.getText().toString()) * rate1;
                out.setText(String.valueOf(ans));
            } else if(id_label == R.id.btn_euro) {
                double ans = Double.parseDouble(in.getText().toString()) * rate2;
                out.setText(String.valueOf(ans));
            } else {
                double ans = Double.parseDouble(in.getText().toString()) * rate3;
                out.setText(String.valueOf(ans));
            }
        }
    }
    public void clickTo(View btn){
        Intent to = new Intent(this, MainActivity4.class);
        to.putExtra("dollarRate",rate1);
        to.putExtra("euroRate",rate2);
        to.putExtra("wonRate",rate3);
        startActivityForResult(to,2);  //这里是跳转到Main4然后进行操作
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == 100){
            Bundle bdl = data.getExtras();
            rate1 = bdl.getDouble("newRate1",0.14);
            rate2 = bdl.getDouble("newRate2",0.92);
            rate3 = bdl.getDouble("newRate3",186.92);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        URL url = null;
        String html = "";
        try {
//            url = new URL("https://www.boc.cn/sourcedb/whpj/");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//
//            html = inputStream2String(in);
//            Log.i(TAG, "run:html=" + html);
            Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
            Element tables = doc.getElementsByTag("table").get(1);
            Elements rows = tables.getElementsByTag("tr");
            rows.remove(0);
            for(Element row : rows){
                Log.i(TAG,"run: row=" + row);
                Elements tds = row.getElementsByTag("td");
                Element td1 = tds.get(0);
                Element td2 = tds.get(5);
                Log.i(TAG,"run:td1 = " +  td1.text() + "->" + td2.text());
                if(td1.text().equals("美元")){
                    rate1 = Double.parseDouble(td2.text());
//                    Log.i(TAG,"run:dollar = " + td2);
                }
                if(td1.text().equals("欧元")){
                    rate2 = Double.parseDouble(td2.text());
//                    Log.i(TAG,"run:euro = " + td2);
                }
                if(td1.text().equals("韩国元")){
                    rate3 = Double.parseDouble(td2.text());
//                    Log.i(TAG,"run:won = " + td2);
                }
            }
            Log.i(TAG,"run:dollar = " + rate1);
            Log.i(TAG,"run:euro = " + rate2);
            Log.i(TAG,"run:won = " + rate3);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Message message = handler.obtainMessage(10, html);
        handler.sendMessage(message);
    }
    private String inputStream2String(InputStream inputStream)throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"utf-8");
        while(true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}