package com.jrtou.myviewdemo.myviewdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.jrtou.myviewdemo.myviewdemo.R;
import com.jrtou.myviewdemo.myviewdemo.view.DashboardView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final DashboardView mD = (DashboardView) findViewById(R.id.dashboardView);
        SeekBar mSeekBar = (SeekBar) findViewById(R.id.sss);
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

        final Switch mSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch.setChecked(true);
        mSwitch.setText("指針");

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mD.setMode(isChecked);
                if (isChecked) {
                    mSwitch.setText("指針");
                } else {
                    mSwitch.setText("數字");
                }
            }
        });
    }
}
