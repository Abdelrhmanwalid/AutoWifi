package abdelrhman.autowifi;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static String LOG_TAG = TimePickerFragment.class.getSimpleName();
    private int mHour;
    private int mMinute;
    public static long timeDifference = 0;
    public static String pickedTime;
    final Calendar calendar = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get time on the current moment
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        // create picker with current time as default value
        return new TimePickerDialog(getActivity(),this,mHour,mMinute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        // calculate the time difference
        String currentTime = String.format("%d:%d", mHour, mMinute);
        pickedTime = String.format("%d:%d", hour, minute);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date current = null;
        Date picked = null;
        try {
            current = simpleDateFormat.parse(currentTime);
            picked = simpleDateFormat.parse(pickedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeDifference = picked.getTime() - current.getTime();
        int seconed = calendar.get(Calendar.SECOND);
        pickedTime = String.format("%d:%d:%d", hour, minute, seconed);
        Toast toast = Toast.makeText(getActivity(),
                Long.toString(timeDifference / (1000 * 60)) + " minutes",Toast.LENGTH_SHORT);
        toast.show();
        Log.v(LOG_TAG,Long.toString(timeDifference / (1000 * 60)) + " minutes");
    }


}
