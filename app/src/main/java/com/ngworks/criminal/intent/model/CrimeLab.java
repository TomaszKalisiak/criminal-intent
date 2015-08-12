package com.ngworks.criminal.intent.model;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by TKALISIAK on 2015-08-04.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private ArrayList<Crime> crimes;

    private CriminalIntentJSONSerializer serializer;

    private static CrimeLab instance;
    private Context appContext;

    private CrimeLab(Context appContext) {
        this.appContext = appContext;
        serializer = new CriminalIntentJSONSerializer(appContext, FILENAME);
        try {
            crimes = serializer.loadCrimes();
        } catch (Exception e) {
            crimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }
    }

    public static CrimeLab getInstance(Context c) {
        if (instance == null) {
            instance = new CrimeLab(c.getApplicationContext());
        }
        return  instance;
    }

    public ArrayList<Crime> getCrimes() {
        return crimes;
    }

    public boolean addCrime(Crime crime) {
        crimes.add(crime);
        return true;
    }

    public boolean saveCrimes() {
        try {
            serializer.saveCrimes(crimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }

    public Crime getCrime(UUID id) {
        for (Crime c : crimes) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
}
