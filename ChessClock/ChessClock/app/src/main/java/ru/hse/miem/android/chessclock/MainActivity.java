package ru.hse.miem.android.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View view) {
        Intent intent = new Intent(this, ClockActivity.class);
        long time = 0;
        switch (view.getId()){
            case R.id.but5:
                time = 300000;
                break;
            case R.id.but10:
                time = 600000;
                break;
            case R.id.but15:
                time = 900000;
                break;
        }
        intent.putExtra("time",time);
        startActivity(intent);
    }
}
