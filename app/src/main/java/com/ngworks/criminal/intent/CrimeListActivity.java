package com.ngworks.criminal.intent;


import android.support.v4.app.Fragment;

/**
 * Created by TKALISIAK on 2015-08-04.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
