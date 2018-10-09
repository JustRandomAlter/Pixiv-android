package com.example.administrator.essim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.DonaterAdapter;
import com.example.administrator.essim.adapters.ViewHistoryAdapter;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.HitoModel;
import com.example.administrator.essim.response.ViewHistory;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.Constant;
import com.example.administrator.essim.utils.DensityUtil;
import com.example.administrator.essim.utils.LinearItemDecoration;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {

    private String dataType = null;
    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_donation);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.INVISIBLE);
        mImageView = findViewById(R.id.no_data);
        mRecyclerView = findViewById(R.id.donate_recy);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        dataType = getIntent().getStringExtra("dataType");
        DonaterAdapter adapter = null;
        if (dataType.equals("donation")) {
            mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
            adapter = new DonaterAdapter(Constant.donaterList, mContext);
            toolbar.setTitle("神豪列表");
            mRecyclerView.setAdapter(adapter);
        } else if (dataType.equals("libraries")) {
            mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 16.0f)));
            adapter = new DonaterAdapter(Constant.libraries, mContext);
            toolbar.setTitle("开源库列表");
            mRecyclerView.setAdapter(adapter);
        }else if (dataType.equals("history")) {
            mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dip2px(mContext, 8.0f)));
            toolbar.setTitle("浏览历史");
            refreshHistory();
        }
    }

    private void refreshHistory(){
        List<ViewHistory> list = new ArrayList<>();
        list.addAll(DataSupport.findAll(ViewHistory.class));
        if(list.size() != 0){
            mImageView.setVisibility(View.INVISIBLE);

        }else {
            mImageView.setVisibility(View.VISIBLE);
        }
        Collections.sort(list);
        ViewHistoryAdapter viewHistoryAdapter = new ViewHistoryAdapter(list, mContext);
        viewHistoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, int position, int viewType) {
                Common.getSingleIllust(mProgressBar, mContext,
                        Long.parseLong(list.get(position).getIllust_id()));
            }

            @Override
            public void onItemLongClick(@NotNull View view, int position) {

            }
        });
        mRecyclerView.setAdapter(viewHistoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(dataType.equals("history")) {
            getMenuInflater().inflate(R.menu.delete, menu);
            return true;
        }
        else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            DataSupport.deleteAll(ViewHistory.class);
            refreshHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
