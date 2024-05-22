package com.lzj.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class RateGetActivity extends AppCompatActivity implements Runnable{
    Handler handler;
    String str = "";
    double rate1 = 0.0,rate2 = 0.0,rate3 = 0.0;
    String TAG = "AAAAAA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_get);
        EditText show = findViewById(R.id.outputRate);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 10){
                    String str = msg.obj.toString();
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };
    }
    public void getRateClick(View btn){
        Thread t = new Thread(this);
        t.start();
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
                str += td1.text() + "->" + td2.text() + "\n";
                Log.i(TAG,"run:td1 = " +  td1.text() + "->" + td2.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Message msg = handler.obtainMessage(10,str);
        handler.sendMessage(msg);
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