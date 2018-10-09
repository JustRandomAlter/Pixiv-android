package com.example.administrator.essim.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.MyImagePicker;
import com.example.administrator.essim.network.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideCacheUtil;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SettingsActivity extends BaseActivity {

    private static final String[] arrayOfFileNameType = {"作品id_p数.jpeg", "作品id_p数.png",
            "作品标题_作品id_p数.jpeg", "作品标题_作品id_p数.png"};
    private int fileNameStyle;
    private Context mContext;
    private Activity mActivity;
    private ProgressBar mProgressBar;
    private RelativeLayout mRelativeLayout, mRelativeLayout2, mRelativeLayout3,
            mRelativeLayout4, mRelativeLayout5, mRelativeLayout6, mRelativeLayout7, mRelativeLayout8,
            mRelativeLayout9, mRelativeLayout10;
    private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6, mTextView7,
            mTextView8, mTextView9, mTextView10, mTextView11, mTextView12, mTextView13, mTextView14;
    private StorageChooser.Builder builder = new StorageChooser.Builder();
    private StorageChooser chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mActivity = this;

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_about);
        nestedScrollView.startAnimation(animation);

        SharedPreferences.Editor editor = Common.getLocalDataSet().edit();
        Toolbar toolbar = findViewById(R.id.toolbar_pixiv);
        toolbar.setNavigationOnClickListener(view -> finish());
        Switch aSwitch = findViewById(R.id.setting_switch);
        mProgressBar = findViewById(R.id.mProgressbar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextView = findViewById(R.id.user_real_name);
        mRelativeLayout2 = findViewById(R.id.username);
        mTextView2 = findViewById(R.id.user_account_real);
        mRelativeLayout3 = findViewById(R.id.user_account);
        mRelativeLayout4 = findViewById(R.id.password);
        mTextView3 = findViewById(R.id.password_real);
        mTextView4 = findViewById(R.id.quit);
        mTextView5 = findViewById(R.id.real_path_real);
        mRelativeLayout5 = findViewById(R.id.real_path);
        mRelativeLayout = findViewById(R.id.rela_2);
        mTextView7 = findViewById(R.id.text_has_permission);
        mTextView8 = findViewById(R.id.app_detail);
        mTextView9 = findViewById(R.id.clear_cache);
        mTextView10 = findViewById(R.id.cache_size_real);
        mRelativeLayout6 = findViewById(R.id.cache_size);
        mRelativeLayout7 = findViewById(R.id.set_header);
        mRelativeLayout8 = findViewById(R.id.set_color);
        mTextView13 = findViewById(R.id.set_file_name_real);
        mRelativeLayout9 = findViewById(R.id.set_file_name);
        mRelativeLayout10 = findViewById(R.id.set_email);
        mTextView6 = findViewById(R.id.real_email);
        aSwitch.setChecked(Common.getLocalDataSet().getBoolean("is_origin_pic", false));
        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("is_origin_pic", b);
            editor.apply();
        });
        mTextView.setText(Common.getLocalDataSet().getString("username", ""));
        mRelativeLayout2.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView.getText().toString());
            return true;
        });
        mTextView2.setText(Common.getLocalDataSet().getString("useraccount", ""));
        mRelativeLayout3.setOnLongClickListener(view -> {
            Common.copyMessage(mContext, mTextView2.getText().toString());
            return true;
        });
        mTextView3.setText(Common.getLocalDataSet().getString("password", ""));
        mRelativeLayout4.setOnLongClickListener(view -> {
            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", mTextView3.getText().toString());
            assert cm != null;
            cm.setPrimaryClip(mClipData);
            TastyToast.makeText(mContext, "密码已复制到剪切板~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            return true;
        });
        mTextView4.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        mTextView5.setText(Common.getLocalDataSet().getString("download_path", "/storage/emulated/0/PixivPictures"));
        mRelativeLayout.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            mActivity.startActivityForResult(i, 1);
            TastyToast.makeText(mContext, "请进入可插拔sd卡根目录，然后点击'确定'", Toast.LENGTH_LONG, TastyToast.DEFAULT).show();
        });
        mTextView7.setText(Common.getLocalDataSet().getString("treeUri", "no sd card").equals("no sd card") ?
                "无SD卡读写权限或无SD卡" : "已获取SD卡读写权限");
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            mTextView8.setText(String.format(getString(R.string.app_detail), pi.versionName, pi.versionCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRelativeLayout6.setOnClickListener(v -> {
            GlideCacheUtil.getInstance().clearImageAllCache(mContext);
            Snackbar.make(mRelativeLayout6, "本地缓存已清空~", Snackbar.LENGTH_SHORT).show();
            mTextView10.setText(R.string.zero_size);
        });
        mTextView10.setText(GlideCacheUtil.getInstance().getCacheSize(mContext));
        MyImagePicker imagePicker = new RxImagePicker.Builder()
                .with(this)
                .build()
                .create(MyImagePicker.class);
        mRelativeLayout7.setOnClickListener(v -> imagePicker
                .openGallery()
                .subscribe(result -> {
                    //获取到被选中图片的uri，保存到本地
                    editor.putString("header_img_path", result.getUri().toString());
                    editor.apply();
                }));
        mRelativeLayout8.setOnClickListener(v ->
                imagePicker.openGallery().subscribe(result -> {
                    File file = new File(Common.getRealFilePath(mContext, result.getUri()));
                    changeHeadImage(file);
                }));
        mTextView13.setText(arrayOfFileNameType[Common.getLocalDataSet().getInt("file_name_style", 0)]);
        mRelativeLayout9.setOnClickListener(v -> setFileNameStyle());
        mRelativeLayout10.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SignEmailActivity.class);
            mContext.startActivity(intent);

        });
        if (Common.getLocalDataSet().getString("email", "").length() != 0) {
            mTextView6.setText(Common.getLocalDataSet().getString("email", ""));
        }

        //初始化路径选择对话框
        Content c = new Content();
        c.setCreateLabel("Create");
        c.setInternalStorageText("内置存储");
        c.setCancelLabel("取消");
        c.setSelectLabel("就决定是你了");
        c.setOverviewHeading("选择存储器");
        builder.withActivity(this)
                .withFragmentManager(getFragmentManager())
                .setMemoryBarHeight(1.5f)
                .disableMultiSelect()
                .withContent(c);

        builder.allowCustomPath(true);
        builder.setType(StorageChooser.DIRECTORY_CHOOSER);
        chooser = builder.build();
        chooser.setOnSelectListener(path -> {
            //如果选出的路径不是机身自带路径，且未设置SD卡权限，则报错
            if (!path.contains("emulated") && Common.getLocalDataSet().getString("treeUri", "no sd card").equals("no sd card")) {
                Snackbar.make(mTextView, "请先配置SD卡的读写权限!", Snackbar.LENGTH_SHORT).show();
            } else {
                editor.putString("download_path", path);
                editor.apply();
                mTextView5.setText(path);
            }
        });
        chooser.setOnCancelListener(() ->
                TastyToast.makeText(mContext, "取消选择", Toast.LENGTH_SHORT, TastyToast.CONFUSING).show());
        //为路径选择Textview设置点击事件
        mRelativeLayout5.setOnClickListener(view -> chooser.show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //申请读写权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 199);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            assert treeUri != null;
            SharedPreferences.Editor editor = Common.getLocalDataSet().edit();
            if (":".equals(treeUri.getPath().substring(treeUri.getPath().length() - 1)) && !treeUri.getPath().contains("primary")) {
                editor.putString("treeUri", treeUri.toString());
                mTextView7.setText(R.string.has_sd_permission);
            } else {
                editor.putString("treeUri", "no sd card");
                mTextView7.setText(R.string.no_sd_permission);
            }
            editor.apply();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeHeadImage(File file) {
        mProgressBar.setVisibility(View.VISIBLE);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("profile_image", file.getName(), photoRequestBody);
        Call<ResponseBody> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .changeHeadImg(Common.getLocalDataSet().getString("Authorization", ""), photo,
                        RequestBody.create(null, file.getName()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(mTextView12, "我也不知道修改成功了没，打开个人主页看看吧...", Snackbar.LENGTH_LONG)
                        .setAction("去看看", v -> {
                            Intent intent = new Intent(mContext, UserDetailActivity.class);
                            intent.putExtra("user id", Common.getLocalDataSet().getInt("userid", 0));
                            startActivity(intent);
                        }).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
            }
        });
    }

    private void setFileNameStyle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("文件命名方式：");
        builder.setCancelable(true);
        builder.setSingleChoiceItems(arrayOfFileNameType, Common.getLocalDataSet().getInt("file_name_style", 0),
                (dialogInterface, i) -> {
                    if (fileNameStyle != i) {
                        fileNameStyle = i;
                    }
                });
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            if (fileNameStyle != Common.getLocalDataSet().getInt("file_name_style", 0)) {
                SharedPreferences.Editor editor = Common.getLocalDataSet().edit();
                editor.putInt("file_name_style", fileNameStyle);
                editor.apply();
            }
        })
                .setNegativeButton("取消", (dialogInterface, i) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chooser.setOnSelectListener(null);
        chooser.setOnCancelListener(null);
    }
}
