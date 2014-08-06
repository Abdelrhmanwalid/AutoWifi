package abdelrhman.autowifi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    public static String SELECTED_OPTION;

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
                Log.v(LOG_TAG, "started");
                long timeDifference = TimePickerFragment.timeDifference;
                if (timeDifference == 0){
                    // Do nothing
                    return;
                }
                Intent intent = new Intent(getActivity(), autowifiSerivce.reciver.class);
                intent.putExtra(SELECTED_OPTION,selectedOption);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+timeDifference, pendingIntent);
            }
        });
        return rootView;
    }
}
