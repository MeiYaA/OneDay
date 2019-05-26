package com.wmj.oneday.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wmj.oneday.LineEditText;
import com.wmj.oneday.MainActivity;
import com.wmj.oneday.R;
import com.wmj.oneday.another.AppManager;
import com.wmj.oneday.another.GetDate;
import com.wmj.oneday.database.DiaryDatabaseHelper;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

//更新日记

public class UpdateDiaryActivity extends AppCompatActivity implements View.OnClickListener{
    TextView mUpdateDiaryTvDate;
    EditText mUpdateDiaryEtTitle;
    LineEditText mUpdateDiaryEtContent;
    FloatingActionButton mUpdateDiaryFabBack;
    FloatingActionButton mUpdateDiaryFabAdd;
    FloatingActionButton mUpdateDiaryFabDelete;
    FloatingActionsMenu mRightLabels;
    TextView mCommonTvTitle;
    LinearLayout mCommonTitleLl;
    ImageView mCommonIvBack;
    ImageView mCommonIvTest;
    TextView mTvTag;

    private DiaryDatabaseHelper mHelper;

    public static void startActivity(Context context, String title, String content, String tag) {
        Intent intent = new Intent(context, UpdateDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("tag", tag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diary);

        mUpdateDiaryTvDate = (TextView)findViewById(R.id.update_diary_tv_date);
        mUpdateDiaryEtTitle = (EditText)findViewById(R.id.update_diary_et_title);
        mUpdateDiaryEtContent = (LineEditText)findViewById(R.id.update_diary_et_content);
        mUpdateDiaryFabBack = (FloatingActionButton)findViewById(R.id.update_diary_fab_back);
        mUpdateDiaryFabBack.setOnClickListener(this);
        mUpdateDiaryFabAdd = (FloatingActionButton)findViewById(R.id.update_diary_fab_add);
        mUpdateDiaryFabAdd.setOnClickListener(this);
        mUpdateDiaryFabDelete = (FloatingActionButton)findViewById(R.id.update_diary_fab_delete);
        mUpdateDiaryFabDelete.setOnClickListener(this);
        mRightLabels = (FloatingActionsMenu)findViewById(R.id.right_labels);
        mCommonTvTitle = (TextView)findViewById(R.id.common_tv_title);
        mCommonTitleLl = (LinearLayout)findViewById(R.id.common_title_ll);
        mCommonIvBack = (ImageView)findViewById(R.id.common_iv_back);
        mCommonIvBack.setOnClickListener(this);
        mCommonIvTest = (ImageView) findViewById(R.id.common_iv_test);
        mTvTag = (TextView)findViewById(R.id.update_diary_tv_tag);

        AppManager.getAppManager().addActivity(this);

        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        initTitle();
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        Intent intent = getIntent();
        mUpdateDiaryTvDate.setText("今天，" + GetDate.getDate());
        mUpdateDiaryEtTitle.setText(intent.getStringExtra("title"));
        mUpdateDiaryEtContent.setText(intent.getStringExtra("content"));
        mTvTag.setText(intent.getStringExtra("tag"));



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                MainActivity.startActivity(this);
            case R.id.update_diary_tv_date:
                break;
            case R.id.update_diary_et_title:
                break;
            case R.id.update_diary_et_content:
                break;
            case R.id.update_diary_fab_back:
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        String title = mUpdateDiaryEtTitle.getText().toString();
                        String tag = mTvTag.getText().toString();
                        SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                        dbDelete.delete("Diary", "tag = ?", new String[]{tag});
                        MainActivity.startActivity(UpdateDiaryActivity.this);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.update_diary_fab_add:
                SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                String title = mUpdateDiaryEtTitle.getText().toString();
                String content = mUpdateDiaryEtContent.getText().toString();
                valuesUpdate.put("title", title);
                valuesUpdate.put("content", content);
                dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{title});
                dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{content});
                MainActivity.startActivity(this);
                break;
            case R.id.update_diary_fab_delete:
                MainActivity.startActivity(this);
                break;
        }
    }

    private void initTitle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mCommonTvTitle.setText("修改日记");
    }

//    @OnClick(R.id.common_tv_title)
//    public void onClick() {
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }
}
