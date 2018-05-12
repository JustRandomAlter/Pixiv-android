package com.example.administrator.essim.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.essim.R;
import com.example.administrator.essim.adapters.PixivAdapterGrid;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.response.SearchIllustResponse;
import com.example.administrator.essim.utils.Common;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchTagActivity extends AppCompatActivity {

    private static final String[] sort = {"popular_desc", "date_desc"};
    private static final String[] arrayOfSearchType = {" 500users入り", " 1000users入り",
            " 5000users入り", " 10000users入り"};
    public String ketWords;
    private String next_url;
    private Toolbar mToolbar;
    private Context mContext;
    private boolean isBestSort;
    private int nowSearchType = -1, togo = -1;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private PixivAdapterGrid mPixivAdapter;
    private SharedPreferences mSharedPreferences;
    private AlphaAnimation alphaAnimationShowIcon;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        mContext = this;
        Intent intent = getIntent();
        ketWords = intent.getStringExtra("what is the keyword");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mToolbar = findViewById(R.id.toolbar_pixiv);
        mToolbar.setTitle(ketWords);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> finish());
        mProgressBar = findViewById(R.id.try_login);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView = findViewById(R.id.pixiv_recy);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPixivAdapter.getItemViewType(position) == 2) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
        getData(sort[1], "");
    }

    private void getData(String rankType, String usersyori) {
        isBestSort = rankType.equals(sort[0]);
        mProgressBar.setVisibility(View.VISIBLE);
        Call<SearchIllustResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getSearchIllust(ketWords + usersyori,
                        rankType,
                        "partial_match_for_tags",
                        null,
                        null,
                        mSharedPreferences.getString("Authorization", ""));
        call.enqueue(new Callback<SearchIllustResponse>() {
            @Override
            public void onResponse(Call<SearchIllustResponse> call, retrofit2.Response<SearchIllustResponse> response) {
                Reference.sSearchIllustResponse = response.body();
                next_url = Reference.sSearchIllustResponse.getNext_url();
                mPixivAdapter = new PixivAdapterGrid(Reference.sSearchIllustResponse.getIllusts(), mContext);
                mPixivAdapter.setOnItemClickListener((view, position, viewType) -> {
                    if (position == -1) {
                        getNextData();
                    } else if (viewType == 0) {
                        Intent intent = new Intent(mContext, ViewPagerActivity.class);
                        intent.putExtra("which one is selected", position);
                        intent.putExtra("all illust", (Serializable) Reference.sSearchIllustResponse.getIllusts());
                        mContext.startActivity(intent);
                    } else if (viewType == 1) {
                        if (!Reference.sSearchIllustResponse.getIllusts().get(position).isIs_bookmarked()) {
                            ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                            ((ImageView) view).startAnimation(alphaAnimationShowIcon);
                            Common.postStarIllust(position, Reference.sSearchIllustResponse.getIllusts(),
                                    mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                        } else {
                            ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            ((ImageView) view).startAnimation(alphaAnimationShowIcon);
                            Common.postUnstarIllust(position, Reference.sSearchIllustResponse.getIllusts(),
                                    mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                        }
                    }
                });
                mRecyclerView.setAdapter(mPixivAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SearchIllustResponse> call, Throwable throwable) {

            }
        });
    }

    private void getNextData() {
        if (next_url != null) {
            if (Reference.sSearchIllustResponse != null) {
                Reference.sSearchIllustResponse = null;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            Call<RecommendResponse> call = new RestClient()
                    .getRetrofit_AppAPI()
                    .create(AppApiPixivService.class)
                    .getNext(mSharedPreferences.getString("Authorization", ""), next_url);
            call.enqueue(new Callback<RecommendResponse>() {
                @Override
                public void onResponse(Call<RecommendResponse> call, retrofit2.Response<RecommendResponse> response) {
                    Reference.loadMoreData = response.body();
                    mPixivAdapter = new PixivAdapterGrid(Reference.loadMoreData.getIllusts(), mContext);
                    next_url = Reference.loadMoreData.getNext_url();
                    mPixivAdapter.setOnItemClickListener((view, position, viewType) -> {
                        if (position == -1) {
                            getNextData();
                        } else if (viewType == 0) {
                            Intent intent = new Intent(mContext, ViewPagerActivity.class);
                            intent.putExtra("which one is selected", position);
                            intent.putExtra("all illust", (Serializable) Reference.loadMoreData.getIllusts());
                            mContext.startActivity(intent);
                        } else if (viewType == 1) {
                            if (!Reference.loadMoreData.getIllusts().get(position).isIs_bookmarked()) {
                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_white_24dp);
                                ((ImageView) view).startAnimation(alphaAnimationShowIcon);
                                Common.postStarIllust(position, Reference.loadMoreData.getIllusts(),
                                        mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                            } else {
                                ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                ((ImageView) view).startAnimation(alphaAnimationShowIcon);
                                Common.postUnstarIllust(position, Reference.loadMoreData.getIllusts(),
                                        mSharedPreferences.getString("Authorization", ""), mRecyclerView);
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mPixivAdapter);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<RecommendResponse> call, Throwable throwable) {

                }
            });
        } else {
            Snackbar.make(mProgressBar, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createSearchTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("筛选结果：");
        builder.setCancelable(true);
        builder.setSingleChoiceItems(arrayOfSearchType, nowSearchType,
                (dialogInterface, i) -> {
                    temp = arrayOfSearchType[i];
                    togo = i;
                });
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            if (nowSearchType != togo) {
                nowSearchType = togo;
                getData(sort[1], arrayOfSearchType[nowSearchType]);
            }
        })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tag_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_search) {
            createSearchTypeDialog();
            return true;
        } else if (id == R.id.action_get_hot) {
            if (mSharedPreferences.getBoolean("ispremium", false)) {
                if (!isBestSort) {
                    getData(sort[0], "");
                }
            } else {
                Snackbar.make(mRecyclerView, "不是会员你他妈凑什么热闹", Snackbar.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}