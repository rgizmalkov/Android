package ru.hse.miem.android.xo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by student on 15.04.2016.
 */
public class DevicesList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_activity);
        String _names[] = getIntent().getStringArrayExtra("names");
        ListView _lv = ((ListView) findViewById(R.id.listView));
        ArrayAdapter<String> _arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_names);
        _lv.setAdapter(_arr);
    }
}
