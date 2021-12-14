package org.ailiartsua.mysummary1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.io.Serializable;

public class TimeActivity extends AppCompatActivity implements Serializable {
    TimePicker reserveTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        SeekBar seekBar = findViewById(R.id.seekBar1);

        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);

        final int[] settime = {0};

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                settime[0] = seekBar.getProgress();
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {//예약설정 버튼 눌렀을때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeActivity.this, MainActivity.class); // 라이트 액티비티 >> 메인액티비티
                intent.putExtra("time", settime[0]);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {//돌아가기 버튼 눌렀을때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeActivity.this, MainActivity.class);
                intent.putExtra("time_m", 1); //키값 형태 시간 종류의 데이터
                setResult(Activity.RESULT_OK, intent); //시스템쪽으로 전달
                finish();
            }
        });

    }
}