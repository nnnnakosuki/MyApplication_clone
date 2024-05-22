package com.lzj.myapplication;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyListActivity2 extends AppCompatActivity implements AdapterView.OnItemLongClickListener, Runnable {
    private static final String TAG = "MyList";
    private Handler handler;
    ListView mylist5;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    SharedPreferences sp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_rate_list);

        sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List", "lastRateDateStr=" + logDate);

        setContentView(R.layout.activity_my_list2);

        mylist5 = findViewById(R.id.mylist5);
        @SuppressLint("WrongViewCast")
        ProgressBar bar = findViewById(R.id.progressBar);
        TextView nodata = findViewById(R.id.nodata);
        ArrayList<HashMap<String, String>> listitems = new ArrayList<HashMap<String, String>>();
//        for(int i = 0;i < 30;i ++ ){
//            HashMap<String,String> map = new HashMap<String, String>();
//            map.put("ItemTitle","Rate: " + i);
//            map.put("ItemDetail","Detail: " + i);
//            listitems.add(map);
//        }
        MyAdapter myAdapter = new MyAdapter(this, R.layout.activity_list_item, listitems);

        mylist5.setAdapter(myAdapter);
        mylist5.setEmptyView(nodata);

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 7) {
                    ArrayList<RateItem> list2 = (ArrayList<RateItem>) msg.obj;
                    RateAdapter adapter = new RateAdapter(MyListActivity2.this, list2);

                    mylist5.setAdapter(adapter);

                    bar.setVisibility(View.GONE);
                    nodata.setText("No Data");

                    RateManager manager = new RateManager(MyListActivity2.this);
//                    for(RateItem rateItem : list2){
//                        RateItem item = new RateItem(rateItem.getCurName(), rateItem.getCurRate());
//                        manager.add(item);
//                    }
//                    RateItem item = new RateItem("测试币种", "333f");
//                    manager.add(item);
                }
                super.handleMessage(msg);
            }
        };
        Thread thread = new Thread(this);
        thread.start();
//        MyRateTask myTask = new MyRateTask(handler);
//        Thread t3 = new Thread(myTask);
//        t3.start();

        mylist5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: 单击" + position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MyListActivity2.this);
                builder.setTitle("提示")
                        .setMessage("请确定是否删除当前数据")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "onClick: 对话框事件处理");
                                myAdapter.remove(mylist5.getItemAtPosition(position));
                            }
                        }).setNegativeButton("否", null);
                builder.create().show();
            }
        });
        mylist5.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> rowdata = (HashMap<String, String>) mylist5.getItemAtPosition(position);
        Log.i(TAG, "onItemLongClick: " + rowdata.get("ItemTitle"));
        return true;
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "utf-8");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    @Override
    public void run() {
        Log.i("List", "run...");
        List<String> retList = new ArrayList<String>();
        List<RateItem> rateList = new ArrayList<RateItem>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run", "curDateStr:" + curDateStr + " logDate:" + logDate);
        if (curDateStr.equals(logDate)) {
            //如果相等，则不从网络中获取数据
            Log.i("run", "日期相等，从数据库中获取数据");
            RateManager dbManager = new RateManager(MyListActivity2.this);
            for (RateItem rateItem : dbManager.listAll()) {
                retList.add(rateItem.getCurName() + "=>" + rateItem.getCurRate());
                rateList.add(new RateItem(rateItem.getCurName(), rateItem.getCurRate()));
            }
        } else {
            Log.i("run", "日期不相等，从网络中获取在线数据");
            //获取网络数据
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
                    rateList.add(item);
                    Log.i(TAG, "run:" + mon + "->" + rate);
                }
                RateManager dbManager = new RateManager(MyListActivity2.this);
                dbManager.deleteAll();
                Log.i("db", "删除所有记录");
                dbManager.addAll(rateList);
                Log.i("db", "添加新记录集");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //更新记录日期
            sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY, curDateStr);
            edit.commit();
            Log.i("run", "更新日期结束：" + curDateStr);
        }
        Message msg = handler.obtainMessage(7, rateList);
        handler.sendMessage(msg);
    }
}