package com.lzj.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class RateAdapter extends ArrayAdapter {

    private static final String TAG = "MyAdapter";

    public RateAdapter(@NonNull Context context, ArrayList<RateItem> list) {
        super(context, 0,list);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item,parent,false);
        }
        RateItem item = (RateItem) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);

        title.setText(item.getCurName());
        detail.setText("Rate: " + item.getCurRate());
        int defaultColor = ContextCompat.getColor(getContext(),R.color.white);
        itemView.setBackgroundColor(defaultColor);
        try{
            float rate = Float.parseFloat(item.getCurRate());
            float blueIntensity = (float) rate / 900f;
            int blueColor = Color.argb((int) (255 * blueIntensity),0,0,255);
            itemView.setBackgroundColor(blueColor);

        }catch (NumberFormatException ex){

        }
        return itemView;
    }
}
