package com.magesh.rideshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class myrides extends Fragment {

    View v;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_myrides, container, false);
        tabLayout = v.findViewById(R.id.tablayoutid);
        viewPager = v.findViewById(R.id.viewpagerid);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new fragmentupcoming(),"upcoming");
        adapter.addFragment(new fragmentcompleted(),"completed");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
