package com.lzj.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListActivity extends ListActivity {

    String TAG = "MyList";
    Handler handler;
    List<String> list = new ArrayList<>();
    private ArrayList<HashMap<String,String>> listItems;
    private SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initListView();
//
//        setListAdapter(listItemAdapter);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 10){
                    listItems =  (ArrayList<HashMap<String,String>>)msg.obj;
                    listItemAdapter = new SimpleAdapter(MyListActivity.this,listItems,R.layout.activity_list_item,
                            new String[] {"ItemTitle","Price"},new int[] {R.id.itemTitle,R.id.itemDetail}  );
                     setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };

        Thread thread = new Thread(()->{
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
            try {
                Document doc = Jsoup.connect("https://www.huilvzaixian.com/").get();
                Element li = doc.getElementsByTag("li").get(6);
                Log.i(TAG,"RUN:" + li.text());
                Elements hrefs = doc.select("div.today-box.today-box2 li.history-urls a[href]");
                for(Element href : hrefs) {
                    Log.i(TAG,"run:" + href.text());
                    String str = href.text();
                    String mon = str.split(" ")[1];
                    String rate = str.split(" ")[2];
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("ItemTitle","Rate: " + mon);
                    map.put("Price","detail" + rate);
                    list.add(map);
                    Log.i(TAG, "run:" + mon + "->" + rate);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Message msg = handler.obtainMessage(10,list);
            handler.sendMessage(msg);
        });
        thread.start();
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//        ListAdapter adapter = new SimpleAdapter(this,list,R.layout.activity_list_item,
//                new String[] {"ItemTitle","Price"},new int[] {R.id.textView1,R.id.textView2}  );
        setListAdapter(adapter);

    }

    private void initListView(){
        listItems = new ArrayList<HashMap<String,String>>();
        for(int i = 0;i < 10;i ++ ){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle","Rate: " + i);
            map.put("Price","detail" + i);
            listItems.add(map);
        }

        listItemAdapter = new SimpleAdapter(this,listItems,R.layout.activity_list_item,
                new String[] {"ItemTitle","Price"},new int[] {R.id.itemTitle,R.id.itemDetail}  );
    }


}