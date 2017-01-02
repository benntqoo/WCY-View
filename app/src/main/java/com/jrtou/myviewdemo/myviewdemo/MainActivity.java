package com.jrtou.myviewdemo.myviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.jrtou.myviewdemo.myviewdemo.view.DashboardView;

public class MainActivity extends AppCompatActivity {
    private DashboardView mD;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mD = (DashboardView) findViewById(R.id.dashboardView);
        mSeekBar = (SeekBar) findViewById(R.id.sss);
//        mPanelView.setText("已完成");
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mD.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
