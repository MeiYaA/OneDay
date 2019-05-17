package com.wmj.oneday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton writeDairy;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeDairy = (FloatingActionButton) findViewById(R.id.main_fab_enter_edit);
        writeDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewDairy();
            }
        });

        date = (TextView) findViewById(R.id.main_tv_date);
        date.setText(getDate());
    }

    public void writeNewDairy(){
        Intent intent = new Intent(this,WriteNewDairy.class);
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
}
