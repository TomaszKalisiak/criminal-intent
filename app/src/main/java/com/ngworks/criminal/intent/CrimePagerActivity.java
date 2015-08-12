package com.ngworks.criminal.intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ngworks.criminal.intent.model.Crime;
import com.ngworks.criminal.intent.model.CrimeLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by TKALISIAK on 2015-08-04.
 */
public class CrimePagerActivity extends FragmentActivity {

    private ViewPager viewPager;

    private ArrayList<Crime> crimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        crimes = CrimeLab.getInstance(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = crimes.get(i);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }

            public void onPageSelected(int pos) {
                Crime crime = crimes.get(pos);
                if (crime.getTitle() != null) {
                    setTitle(crime.getTitle());
                }
            }
        });
    }
}
