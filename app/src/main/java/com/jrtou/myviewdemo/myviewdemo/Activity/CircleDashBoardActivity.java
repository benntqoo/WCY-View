package com.jrtou.myviewdemo.myviewdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jrtou.myviewdemo.myviewdemo.R;
import com.jrtou.myviewdemo.wcylibrary.View.CircleDashboard;

public class CircleDashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_dash_board);
        final CircleDashboard CD = (CircleDashboard) findViewById(R.id.circle_dashboard);

        Button btnRandom = (Button) findViewById(R.id.circle_btn_random);
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = (int) (Math.random() * 100);
                CD.setPercent(random);
            }
        });

        final EditText edNumber = (EditText) findViewById(R.id.circle_ed_number);
        Button btnInout = (Button) findViewById(R.id.circle_btn_input);
        btnInout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNumber.getText().toString().length() > 0) {
                    int number = Integer.parseInt(edNumber.getText().toString());
                    CD.setPercent(number);
                } else {
                    Toast.makeText(getApplicationContext(), "請輸入數值", Toast.LENGTH_SHORT).show();
                }

            }
        });

        CheckBox mCheckBox = (CheckBox) findViewById(R.id.circlr_checkBox);
        mCheckBox.setChecked(CD.getAnimationMode());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CD.setAnimationMode(b);
            }
        });
    }
}
