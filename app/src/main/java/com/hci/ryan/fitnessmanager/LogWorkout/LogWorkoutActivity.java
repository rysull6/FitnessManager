package com.hci.ryan.fitnessmanager.LogWorkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hci.ryan.fitnessmanager.AddExercise.AddExerciseActivity;
import com.hci.ryan.fitnessmanager.Common;
import com.hci.ryan.fitnessmanager.ExerciseActualDialog;
import com.hci.ryan.fitnessmanager.ExerciseInfoDialog;
import com.hci.ryan.fitnessmanager.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LogWorkoutActivity extends AppCompatActivity {

    String dayString;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);
        dayString = getCurrentDay();
        try {
            setArrayList(getExerciseList(dayString));
        } catch (IOException e) {
        }
    }

    private void setArrayList(final Set<String> values)
    {
        final ListView listview = (ListView) findViewById(R.id.list_container);
        final ArrayList<String> list = new ArrayList<String>();
        Iterator<String> iter = values.iterator();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        list.add(0, "New Exercise");
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                if (item.equals("New Exercise")) {
                    goToAddExercise();
                }
                else {
                    showDialog(item);
                }
            }

        });
    }

    public void showDialog(String currentExercise) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ExerciseActualDialog(currentExercise, dayString);
        dialog.show(getSupportFragmentManager(), "ExerciseInfoDialog");
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 0) {
            if( resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                data.getStringExtra("ExerciseLabel");
                Set<String> values = null;
                try {
                    values = getExerciseList(getCurrentDay());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                values.add(data.getStringExtra("ExerciseLabel"));
                setSetItem("exerciseList" + getCurrentDay(), values);
                setArrayList(values);
            }
        }
    }

    public void goToAddExercise()
    {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        intent.putExtra("dayValue", dayString);
        startActivityForResult(intent, 0);
    }

    public void onComplete(View view)
    {
        finish();
    }

    private String readUserInformation(String key)
    {
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString(key, "");//"No name defined" is the default value.
        return name;
    }

    private void setItem(String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void setSetItem(String key, Set<String> value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    private Set<String> getExerciseList(String day) throws IOException {
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        Set<String> newList = new HashSet<String>();
        Set<String> list = prefs.getStringSet("exerciseList" + day, newList);//"No name defined" is the default value.
        return list;
    }

    private String getCurrentDay()
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                return "S";
            case Calendar.MONDAY:
                return "M";
            case Calendar.TUESDAY:
                return "T";
            case Calendar.WEDNESDAY:
                return "W";
            case Calendar.THURSDAY:
                return "Th";
            case Calendar.FRIDAY:
                return "F";
            case Calendar.SATURDAY:
                return "Sat";
        }
        return "";
    }

}