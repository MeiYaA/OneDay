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
import android.support.v7.app.AlertDialog;
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

import cc.trity.floatingactionbutton.FloatingActionsMenu;
import cc.trity.floatingactionbutton.FloatingActionButton;

//写日记

public class WriteDiaryActivity extends AppCompatActivity implements View.OnClickListener{
    TextView mAddDiaryTvDate;
    EditText mAddDiaryEtTitle;
    LineEditText mAddDiaryEtContent;
    FloatingActionButton mAddDiaryFabBack;
    FloatingActionButton mAddDiaryFabAdd;
    FloatingActionsMenu mRightLabels;
    TextView mCommonTvTitle;
    LinearLayout mCommonTitleLl;
    ImageView mCommonIvBack;
    ImageView mCommonIvTest;

    private DiaryDatabaseHelper mHelper;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WriteDiaryActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, String content) {
        Intent intent = new Intent(context, WriteDiaryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        mAddDiaryTvDate = (TextView) findViewById(R.id.add_diary_tv_date);
        mAddDiaryEtTitle = (EditText)findViewById(R.id.add_diary_et_title);
        mAddDiaryEtContent = (LineEditText) findViewById(R.id.add_diary_et_content);
        mAddDiaryFabBack = (FloatingActionButton) findViewById(R.id.add_diary_fab_back);
        mAddDiaryFabAdd = (FloatingActionButton) findViewById(R.id.add_diary_fab_add);

        mRightLabels = (FloatingActionsMenu) findViewById(R.id.right_labels);
        mCommonTvTitle = (TextView) findViewById(R.id.common_tv_title);
        mCommonTitleLl = (LinearLayout)findViewById(R.id.common_title_ll);
        mCommonIvBack = (ImageView) findViewById(R.id.common_iv_back);
        mCommonIvTest = (ImageView)findViewById(R.id.common_iv_test);

        mCommonIvBack.setOnClickListener(this);
        mAddDiaryFabBack.setOnClickListener(this);
        mAddDiaryFabAdd.setOnClickListener(this);

        AppManager.getAppManager().addActivity(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        mAddDiaryEtTitle.setText(intent.getStringExtra("title"));
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        mCommonTvTitle.setText("添加日记");
        mAddDiaryTvDate.setText("今天，" + GetDate.getDate());
        mAddDiaryEtContent.setText(intent.getStringExtra("content"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                MainActivity.startActivity(this);
            case R.id.add_diary_et_title:
                break;
            case R.id.add_diary_et_content:
                break;
            case R.id.add_diary_fab_back:
                String date = GetDate.getDate().toString();
                String tag = String.valueOf(System.currentTimeMillis());
                String title = mAddDiaryEtTitle.getText().toString() + "";
                String content = mAddDiaryEtContent.getText().toString() + "";
                if (!title.equals("") || !content.equals("")) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("date", date);
                    values.put("title", title);
                    values.put("content", content);
                    values.put("tag", tag);
                    db.insert("Diary", null, values);
                    values.clear();
                }
                MainActivity.startActivity(this);
                break;
            case R.id.add_diary_fab_add:
                final String dateBack = GetDate.getDate().toString();
                final String tagBack = String.valueOf(System.currentTimeMillis());
                final String titleBack = mAddDiaryEtTitle.getText().toString();
                final String contentBack = mAddDiaryEtContent.getText().toString();
                if(!titleBack.isEmpty() || !contentBack.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date", dateBack);
                            values.put("title", titleBack);
                            values.put("content", contentBack);
                            values.put("tag", tagBack);
                            db.insert("Diary", null, values);
                            values.clear();
                            MainActivity.startActivity(WriteDiaryActivity.this);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.startActivity(WriteDiaryActivity.this);
                        }
                    }).show();
                }else{
                    MainActivity.startActivity(this);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }
}
