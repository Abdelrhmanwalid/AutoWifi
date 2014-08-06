package abdelrhman.autowifi.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import abdelrhman.autowifi.MainFragment;

public class autowifiSerivce extends IntentService {

    public autowifiSerivce() {
        super("autowifiSerivce");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String selectedOption = intent.getStringExtra(MainFragment.SELECTED_OPTION);
        boolean enable = selectedOption.equals("On");
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public static class reciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String selectedOption = intent.getStringExtra(MainFragment.SELECTED_OPTION);
            Intent serviceIntent = new Intent(context, autowifiSerivce.class);
            serviceIntent.putExtra(MainFragment.SELECTED_OPTION, selectedOption);
            context.startService(intent);
        }
    }
}