package com.jrtou.myviewdemo.myviewdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.jrtou.myviewdemo.myviewdemo.R;
import com.jrtou.myviewdemo.wcylibrary.View.WaterWaveDashBoard;

public class WaterWaveDashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_wave_dash_board);

        final WaterWaveDashBoard mWater = (WaterWaveDashBoard) findViewById(R.id.waterWaveDashBoard);


        SeekBar mSeekBar = (SeekBar) findViewById(R.id.waterWaveDashBoard_seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mWater.setPercent(progress);
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
