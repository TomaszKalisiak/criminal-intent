package com.ngworks.criminal.intent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.ngworks.criminal.intent.model.Crime;
import com.ngworks.criminal.intent.model.CrimeLab;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by TKALISIAK on 2015-08-04.
 */
public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_ID =
            "com.ngworks.criminal.intent.crime_id";

    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Crime crime;
    private EditText titleField;
    private Button dateButton;
    private Button timeButton;
    private CheckBox solvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment cf = new CrimeFragment();
        cf.setArguments(args);
        return cf;
    }
    @TargetApi(11)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(CrimeFragment.EXTRA_CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        titleField = (EditText)v.findViewById(R.id.crime_title);
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //left blank
            }
        });

        dateButton = (Button)v.findViewById(R.id.crime_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        timeButton = (Button)v.findViewById(R.id.crime_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });

        updateDateTimeButtons();

        solvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_DATE:
                crime.setDate((Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE));
                break;
            case REQUEST_TIME:
                crime.setDate((Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME));
                break;
            default:
                break;
        }

        updateDateTimeButtons();
    }

    private void updateDateTimeButtons() {
        SimpleDateFormat df = new SimpleDateFormat("cccc, LLLL dd, yyyy");
        dateButton.setText(df.format(crime.getDate()));
        df = new SimpleDateFormat("kk:mm");
        timeButton.setText(df.format(crime.getDate()));
    }
}
