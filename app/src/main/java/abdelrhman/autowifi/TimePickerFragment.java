package abdelrhman.autowifi;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abdelrhman on 8/6/14.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static String LOG_TAG = TimePickerFragment.class.getSimpleName();
    private int mHour;
    private int mMinute;
    public static long timeDifference = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get time on the current moment
        final Calendar calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        // create picker with current time as default value
        return new TimePickerDialog(getActivity(),this,mHour,mMinute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        // calculate the time difference
        String currentTime = String.format("%d:%d", mHour, mMinute);
        String pickedTime = String.format("%d:%d", hour, minute);
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
        Log.v(LOG_TAG,Long.toString(timeDifference / (1000 * 60)) + " minutes");
    }


}
