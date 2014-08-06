package abdelrhman.autowifi;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import abdelrhman.autowifi.service.autowifiSerivce;

public class MainFragment extends Fragment {
    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private String selectedOption;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final Spinner wifiOptionsSpinner = (Spinner)rootView.findViewById(R.id.wifi_spinner);
        final ArrayAdapter<CharSequence> wifiOptionsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.wifi_options, android.R.layout.simple_spinner_item);
        wifiOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wifiOptionsSpinner.setAdapter(wifiOptionsAdapter);

        wifiOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long l) {
                selectedOption = wifiOptionsAdapter.getItem(postion).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do Nothing
            }
        });

        Button setTimeButton = (Button) rootView.findViewById(R.id.set_time_button);
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new TimePickerFragment();
                FragmentManager fragmentManager = MainFragment.this.getActivity().getSupportFragmentManager();
                fragment.show(fragmentManager,"timepicker");
            }
        });

        Button startButton = (Button) rootView.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG, selectedOption);
                long timeDifference = TimePickerFragment.timeDifference;
                if (timeDifference <= 0){
                    // Do nothing
                    return;
                }
                // set the intent
                Notification();
                Intent intent = new Intent(getActivity(), autowifiSerivce.alarmReceiver.class);
                intent.putExtra(autowifiSerivce.SELECTED_OPTION,selectedOption);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent =  PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+timeDifference, pendingIntent);
            }
        });
        return rootView;
    }
    private void Notification(){
        // create intent to launch the app when the notification is clicked
        Intent notificationIntent = new Intent(getActivity(),MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notificationPendingIntent =  PendingIntent.getActivity(
                getActivity(), 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        String notficationString = String.format(
                getString(R.string.notification_text), selectedOption, TimePickerFragment.pickedTime);
        // the notification
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(notficationString)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(notficationString)
                .setContentIntent(notificationPendingIntent)
                .setOngoing(true);
        Notification notification = nBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getActivity().
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }
}
