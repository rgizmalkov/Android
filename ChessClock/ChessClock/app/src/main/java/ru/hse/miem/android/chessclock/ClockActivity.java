package ru.hse.miem.android.chessclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by student on 08.04.2016.
 */
public class ClockActivity extends Activity implements View.OnClickListener {
    long blacktime;
    long whitetime;
    long timeleft = 0;
    long timeclick = 0;
    int current = -1;
    Timer clocktimer = null;
    TimerTask clocktimertask = null;
    ClockHandler myclockhandler = null;

    class ClockHandler extends Handler {
        public void handleMessage(Message m) {
            if (m.what == 0) {
                UpdateText();
            }
        }
    }

    class ClockTimerTask extends TimerTask {

        @Override
        public void run() {
            myclockhandler.sendEmptyMessage(0);
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clocklayout);

        findViewById(R.id.butwhite).setOnClickListener(this);
        findViewById(R.id.butblack).setOnClickListener(this);
        blacktime = getIntent().getLongExtra("time",0);
        whitetime = blacktime;
        UpdateText();
    }


    @Override
    public void onClick(View v) {
        if (current == -1) {
            timeleft = 0;
            clocktimer = new Timer();
            clocktimertask = new ClockTimerTask();
            myclockhandler = new ClockHandler();
            clocktimer.schedule(clocktimertask,0,100);
        } else {
            timeleft = System.currentTimeMillis() - timeclick;
        }
        timeclick = System.currentTimeMillis();
        if (v.getId() != current) {
            current = v.getId();
            if (current == R.id.butblack) {
                whitetime -= timeleft;
            } else if (current == R.id.butwhite){
                blacktime -= timeleft;
            }
        }
    }

    protected void UpdateText() {
        if (current != -1) {
            timeleft = System.currentTimeMillis() - timeclick;
        }

        long whitetime1 = whitetime, blacktime1 = blacktime;
        if (current == R.id.butblack) {
            blacktime1 -= timeleft;
        } else if (current == R.id.butwhite){
            whitetime1 -= timeleft;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Button b = (Button)findViewById(R.id.butblack);
        b.setText(sdf.format(new Date(blacktime1)));
        b = (Button)findViewById(R.id.butwhite);
        b.setText(sdf.format(new Date(whitetime1)));
    }
}
