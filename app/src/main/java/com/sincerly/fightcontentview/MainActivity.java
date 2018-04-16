package com.sincerly.fightcontentview;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sincerly.fightcontentview.adapter.ViewPagerFragmentAdapter;
import com.sincerly.fightcontentview.fragment.FragmentA;
import com.sincerly.fightcontentview.fragment.FragmentB;
import com.sincerly.fightcontentview.fragment.FragmentC;
import com.sincerly.fightcontentview.ui.TViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TViewPager v = findViewById(R.id.viewPager);
        List<Fragment> list = new ArrayList<>();
        list.add(new FragmentA());
        list.add(new FragmentB());
        list.add(new FragmentC());
        List<String> titles=new ArrayList<>();
        titles.add("重庆时时彩");
        titles.add("新疆时时彩");
        titles.add("天津时时彩");
        v.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(),list,titles));
        v.setOffscreenPageLimit(3);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(v);
    }
}
