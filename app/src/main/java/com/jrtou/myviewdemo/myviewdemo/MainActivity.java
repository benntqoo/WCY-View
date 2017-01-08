package com.jrtou.myviewdemo.myviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jrtou.myviewdemo.myviewdemo.Activity.CircleDashBoardActivity;
import com.jrtou.myviewdemo.myviewdemo.Activity.DashboardActivity;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private String[] mSTRArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSTRArray = getResources().getStringArray(R.array.root);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSTRArray));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this, DashboardActivity.class);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, CircleDashBoardActivity.class);
                        break;
                }

                startActivity(intent);
            }
        });
    }


}
