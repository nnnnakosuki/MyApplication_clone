package com.lzj.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPageAdapter extends FragmentStateAdapter {
    public MyPageAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new Fragment1();
        }else if(position==1){
            return new Fragment2();
        }else{
            return new Fragment3();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
