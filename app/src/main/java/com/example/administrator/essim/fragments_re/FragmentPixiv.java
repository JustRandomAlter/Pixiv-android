package com.example.administrator.essim.fragments_re;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities_re.BatchDownloadActivity;
import com.example.administrator.essim.activities_re.MainActivity;
import com.example.administrator.essim.activities.SearchActivity;
import com.example.administrator.essim.activities_re.PixivApp;

import org.jetbrains.annotations.NotNull;

public class FragmentPixiv extends BaseFragment {

    private static final String[] TITLES = new String[]{"Recommended", "Popular"};

    private BaseFragment[] mFragments;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_pixiv;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    View initView(View v) {
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v1 -> {
            ((MainActivity) getActivity()).getDrawer().openDrawer(Gravity.START, true);
        });
        mFragments = new BaseFragment[2];
        mFragments[0] = new FragmentRecmdIllust();
        mFragments[1] = new FragmentHotTag();
        ViewPager viewPager = v.findViewById(R.id.mViewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments[i];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @NotNull
            @Override
            public CharSequence getPageTitle(int position) {
                return TITLES[position];
            }
        });
        TabLayout tabLayout = v.findViewById(R.id.mTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    @Override
    void initData() {

    }

    @Override
    void getFirstData() {

    }

    @Override
    void getNextData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_pixiv, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(mContext, SearchActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_download) {
            PixivApp.sIllustsBeans = ((FragmentRecmdIllust) mFragments[0]).getAllIllusts();
            Intent intent = new Intent(mContext, BatchDownloadActivity.class);
            intent.putExtra("scroll dist", ((FragmentRecmdIllust) mFragments[0]).getScrollIndex());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public BaseFragment[] getFragments() {
        return mFragments;
    }

    public void setFragments(BaseFragment[] fragments) {
        mFragments = fragments;
    }
}
