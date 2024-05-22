package com.lzj.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MyRateTask implements Runnable{
    public static final String TAG = "MyTask";
    private Handler handler;

    public MyRateTask(Handler handler){
        this.handler = handler;
    }

//    public void setHandler(Handler handler) {
//        this.handler = handler;
//    }

    @Override

    public void run() {
        Log.i(TAG,"run:   ");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        Bundle retbundle = new Bundle();
        ArrayList<RateItem> relist = new ArrayList<RateItem>();
        try {
            Document doc = Jsoup.connect("https://www.huilvzaixian.com/").get();
            Element li = doc.getElementsByTag("li").get(6);
            Log.i(TAG,"RUN:" + li.text());
            Elements hrefs = doc.select("div.today-box.today-box2 li.history-urls a[href]");
            for(Element href : hrefs) {
//                Log.i(TAG,"run:" + href.text());
                String str = href.text();
                String mon = str.split(" ")[1];
                String rate = str.split(" ")[2];
                RateItem item = new RateItem(mon,rate);
                relist.add(item);
                Log.i(TAG, "run:" + mon + "->" + rate);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Message msg = handler.obtainMessage(7,relist);
        handler.sendMessage(msg);
    }
}
