package abdelrhman.autowifi.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class autowifiSerivce extends IntentService {
    private static final String LOG_TAG = autowifiSerivce.class.getSimpleName();
    public static final String SELECTED_OPTION = "so";
    public autowifiSerivce() {
        super("autowifiSerivce");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager = (NotificationManager) this.
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(101);
        String selectedOption = intent.getStringExtra(SELECTED_OPTION);
        boolean enable = selectedOption.equals("On");
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public static class alarmReceiver extends BroadcastReceiver{
        private static final String LOG_TAG = alarmReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(LOG_TAG, "Received " + intent.getStringExtra(SELECTED_OPTION));
            Intent serviceIntent = new Intent(context, autowifiSerivce.class);
            serviceIntent.putExtra(SELECTED_OPTION,
                    intent.getStringExtra(SELECTED_OPTION));
            context.startService(serviceIntent);
        }
    }
}