package com.wmj.oneday;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmj.oneday.Diary.Diary;
import com.wmj.oneday.another.AppManager;
import com.wmj.oneday.another.GetDate;
import com.wmj.oneday.another.SpHelper;
import com.wmj.oneday.database.DiaryDatabaseHelper;
import com.wmj.oneday.event.StartUpdateDiaryEvent;
import com.wmj.oneday.ui.DiaryAdapter;
import com.wmj.oneday.ui.StatusBarCompat;
import com.wmj.oneday.ui.UpdateDiaryActivity;
import com.wmj.oneday.ui.WriteDiaryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    ImageView mCommonIvBack;
    TextView mCommonTvTitle;
    ImageView mCommonIvTest;
    LinearLayout mCommonTitleLl;
    ImageView mMainIvCircle;
    TextView mMainTvDate;
    TextView mMainTvContent;
    LinearLayout mItemLlControl;

    RecyclerView mMainRvShowDiary;
    FloatingActionButton mMainFabEnterEdit;
    RelativeLayout mMainRlMain;
    LinearLayout mItemFirst;
    LinearLayout mMainLlMain;

    private List<Diary> mDiaryBeanList;

    private DiaryDatabaseHelper mHelper;

    private static String IS_WRITE = "true";

    private int mEditPosition = -1;

    /**
     * 标识今天是否已经写了日记
     */
    private boolean isWrite = false;
    private static TextView mTvTest;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommonIvBack = (ImageView)findViewById(R.id.common_iv_back);
        mCommonTvTitle = (TextView)findViewById(R.id.common_tv_title);
        mCommonIvTest = (ImageView)findViewById(R.id.common_iv_test);
        mCommonTitleLl = (LinearLayout)findViewById(R.id.common_title_ll);
        mMainIvCircle = (ImageView)findViewById(R.id.main_iv_circle);
        mMainTvDate = (TextView)findViewById(R.id.main_tv_date);
        mMainTvContent = (TextView)findViewById(R.id.main_tv_content);
        mItemLlControl = (LinearLayout)findViewById(R.id.item_ll_control);

        mMainRvShowDiary = (RecyclerView)findViewById(R.id.main_rv_show_diary);
        mMainFabEnterEdit = (FloatingActionButton)findViewById(R.id.main_fab_enter_edit);
        mMainRlMain = (RelativeLayout)findViewById(R.id.main_rl_main);
        mItemFirst = (LinearLayout)findViewById(R.id.item_first);
        mMainLlMain = (LinearLayout)findViewById(R.id.main_ll_main);

        mMainFabEnterEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteDiaryActivity.startActivity(v.getContext());
            }
        });

        AppManager.getAppManager().addActivity(this);

        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        EventBus.getDefault().register(this);
        SpHelper spHelper = SpHelper.getInstance(this);
        getDiaryBeanList();
        initTitle();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        mMainRvShowDiary.setAdapter(new DiaryAdapter(this, mDiaryBeanList));
    }

    private void initTitle() {
        mMainTvDate.setText("今天，" + GetDate.getDate());
        mCommonTvTitle.setText("日记");
        mCommonIvBack.setVisibility(View.INVISIBLE);
        mCommonIvTest.setVisibility(View.INVISIBLE);

    }

    /*
    * 检索数据库里是否有与今天日期一致的日记，如果没有就显示为日记
    * */

    private List<Diary> getDiaryBeanList() {

        mDiaryBeanList = new ArrayList<>();
        List<Diary> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                mDiaryBeanList.add(new Diary(date, title, content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    @Subscribe
    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        UpdateDiaryActivity.startActivity(this, title, content, tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}
