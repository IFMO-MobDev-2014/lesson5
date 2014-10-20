package com.example.pva701.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pva701 on 18.10.14.
 */
public class NewsListPagerActivity extends FragmentActivity {
    public static final String NAME_ID = "id_clicked";
    private ViewPager viewPagerNews;
    private ArrayList <SourcesManager.Source> sources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sources = SourcesManager.getInstance(this).getSources();
        int id = getIntent().getIntExtra(NAME_ID, -1);
        if (id == -1)
            throw new RuntimeException("incorrect index of clicked source");

        viewPagerNews = new ViewPager(this);
        viewPagerNews.setId(R.id.viewPagerNews);
        setContentView(viewPagerNews);

        viewPagerNews.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return NewsListFragment.newInstance(i);
            }

            @Override
            public int getCount() {
                return sources.size();
            }
        });

        SourcesManager.Source clickedSource = sources.get(id);
        if (clickedSource.getName().equals(""))
            setTitle("News");
        else
            setTitle(clickedSource.getName());
        viewPagerNews.setCurrentItem(id);
        viewPagerNews.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                SourcesManager.Source c = sources.get(i);
                if (c.getName() != null)
                    setTitle(c.getName());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
}
