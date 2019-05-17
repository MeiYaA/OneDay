package com.wmj.oneday;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import cc.trity.floatingactionbutton.FloatingActionButton;

public class WriteNewDairy extends AppCompatActivity {

    private ImageView back;
    private TextView date;
    private FloatingActionButton saveNewDairy;
    private EditText title;
    private EditText content;
    Myhelper myhelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_dairy);

        back = (ImageView) findViewById(R.id.common_iv_back);
        date = (TextView)findViewById(R.id.add_diary_tv_date);
        title = (EditText)findViewById(R.id.add_diary_et_title);
        content = (EditText)findViewById(R.id.add_diary_et_content);
        saveNewDairy = (FloatingActionButton)findViewById(R.id.add_diary_fab_add);

        date.setText("今天，" + getDate());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainActivity();
            }
        });
        saveNewDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDairy();
            }
        });
    }
    class Myhelper extends SQLiteOpenHelper {
        public Myhelper(Context context){
            super(context,"Dairy.db",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE dairy(date VARCHAR(30),title VARCHAR(100),content VARCHAR(1000))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        }
    }
    public void backToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public static StringBuilder getDate(){

        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.YEAR) + "年");
        stringBuilder.append((int)(now.get(Calendar.MONTH) + 1)  + "月");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
        return stringBuilder;

    }

    public void addNewDairy()
    {
        final String tmpdate;
        final String tmptitle;
        final String tmpcontent;

        tmpdate = date.getText().toString();
        tmptitle = title.getText().toString();
        tmpcontent = content.getText().toString();

        if(!tmptitle.isEmpty() || !tmpcontent.isEmpty())
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = myhelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date",tmpdate);
                            values.put("title",tmptitle);
                            values.put("content",tmpcontent);
                            db.insert("dairy",null,values);
                            values.clear();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }

    }
}
