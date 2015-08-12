package com.ngworks.criminal.intent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by TKALISIAK on 2015-08-05.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME = "com.ngworks.criminal.intent.time";

    private Date date;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(TimePickerFragment.EXTRA_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        date = (Date)getArguments().getSerializable(TimePickerFragment.EXTRA_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                date = new GregorianCalendar(year, month, day, hourOfDay, minute).getTime();
                getArguments().putSerializable(TimePickerFragment.EXTRA_TIME, date);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Pick time:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent i = new Intent();
        i.putExtra(TimePickerFragment.EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
