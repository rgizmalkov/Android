package ru.hse.miem.android.xo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

import java.util.Collection;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private int[][] mas = new int[3][3];
    private int[][] mas_id = {{R.id.b1, R.id.b2, R.id.b3}, {R.id.b4, R.id.b5, R.id.b6}, {R.id.b7, R.id.b8, R.id.b9}} ;
    private int gamer = 0;
    private WifiP2pManager mng;
    private Channel cn;
    private BroadcastReceiver bro;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mng = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        cn = mng.initialize(this, getMainLooper(), null);
        bro = new MyBroadcastReciever(mng, cn);
        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        init();
    }

    @Override
    protected void onResume(){
        registerReceiver(bro, filter);
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(bro);
    }

    private void init(){
        for (int _i = 0; _i < 9; _i++){
            mas[_i / 3][_i % 3] = -1;
            ((Button)findViewById(mas_id[_i / 3][_i % 3])).setText("");
        }
    }

    public void show_list(String []_list){
        Intent _i = new Intent(this, DevicesList.class);
        _i.putExtra("names", _list);
        startActivity(_i);
    }

    public void onClick2(View _v){
        mng.discoverPeers(cn, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View _v){
        for (int _i = 0; _i < 9; _i++) {
            if (mas_id[_i / 3][_i % 3] == _v.getId()) {
                mas[_i / 3][_i % 3] = gamer;
                ((Button)_v).setText((gamer == 0) ? "X" : "0");
                break;
            }
        }
    }

    class MyBroadcastReciever extends BroadcastReceiver{
        private WifiP2pManager manager;
        private Channel cn;
        public MyBroadcastReciever(WifiP2pManager _manager, Channel _cn){
            manager = _manager;
            cn = _cn;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            String _action = intent.getAction();
            if (_action.equals(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)){
                int _state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (_state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    Toast.makeText(MainActivity.this, "Wifi P2p is Enabled", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Wifi P2p is Disabled", Toast.LENGTH_SHORT).show();
                }
            }
            if (_action.equals(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)){
                if (manager != null){
                    Toast.makeText(MainActivity.this, "requesting peers", Toast.LENGTH_SHORT).show();
                    manager.requestPeers(cn, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            Iterator<WifiP2pDevice> _i = peers.getDeviceList().iterator();
                            String []_names = new String[peers.getDeviceList().size()];
                            int _j = 0;
                            while (_i.hasNext()){
                                _names[_j++] = _i.next().deviceName.toString();
                                //Toast.makeText(MainActivity.this, _i.next().deviceName.toString(), Toast.LENGTH_SHORT).show();
                            }
                            MainActivity.this.show_list(_names);
                        }
                    });
                }
            }
        }
    }
}
