package com.ngworks.criminal.intent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ngworks.criminal.intent.model.Crime;
import com.ngworks.criminal.intent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by TKALISIAK on 2015-08-04.
 */
public class CrimeListFragment extends ListFragment {

    private static final String TAG = CrimeListFragment.class.toString();

    private boolean subtitleVisible;

    private ArrayList<Crime> crimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.crimes_title);
        crimes = CrimeLab.getInstance(getActivity()).getCrimes();
        CrimeAdapter crimeAdapter = new CrimeAdapter(crimes);
        setListAdapter(crimeAdapter);

        setHasOptionsMenu(true);

        setRetainInstance(true);
        subtitleVisible = true;
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (subtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }

        Button addCrimeBtn = (Button)v.findViewById(R.id.add_crime_btn);
        addCrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowCrime();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                return createAndShowCrime();
            case R.id.menu_item_show_subtitle:
                return presentSubtitle(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = ((CrimeAdapter)(getListAdapter())).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked!");
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    private boolean createAndShowCrime() {
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivityForResult(i, 0);
        return true;
    }

    private boolean presentSubtitle(MenuItem item) {
        if(getActivity().getActionBar().getSubtitle() == null) {
            getActivity().getActionBar().setSubtitle(R.string.subtitle);
            item.setTitle(R.string.hide_subtitle);
            subtitleVisible = true;
        } else {
            getActivity().getActionBar().setSubtitle(null);
            item.setTitle(R.string.show_subtitle);
            subtitleVisible = false;
        }
        return subtitleVisible;
    }


    private class CrimeAdapter extends ArrayAdapter<Crime> {


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }
            Crime c = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);

            SimpleDateFormat df = new SimpleDateFormat("cccc, LLLL dd, yyyy");
            dateTextView.setText(df.format(c.getDate()));

            TextView timeTextView = (TextView)convertView.findViewById(R.id.crime_list_item_timeTextView);
            df.applyPattern("kk:mm");
            timeTextView.setText(df.format(c.getDate()));

            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());
            return convertView;
        }

        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }
    }
}
