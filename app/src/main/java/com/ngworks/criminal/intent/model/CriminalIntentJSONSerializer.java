package com.ngworks.criminal.intent.model;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by TKALISIAK on 2015-08-11.
 */
public class CriminalIntentJSONSerializer {
    private Context context;
    private String fileName;

    public CriminalIntentJSONSerializer(Context c, String f){
        context = c;
        fileName = f;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException, ParseException{
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            InputStream in;
           /* if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(context.getExternalFilesDir(null).toString() + "/" + fileName);
                in = new FileInputStream(file);
            } else {*/
            in = context.openFileInput(fileName);
          //  }
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }

        } catch (FileNotFoundException e) {
            //Ignore this since it hits on app setup
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes)  throws IOException, JSONException{
        JSONArray jsonArray = new JSONArray();
        for (Crime crime : crimes) {
            jsonArray.put(crime.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = context
                    .openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
