package com.hci.ryan.fitnessmanager.SetupRoutine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hci.ryan.fitnessmanager.AddExercise.AddExerciseActivity;
import com.hci.ryan.fitnessmanager.Common;
import com.hci.ryan.fitnessmanager.R;
import com.hci.ryan.fitnessmanager.SetupRoutineDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class SetupRoutineActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    String dayString;
    Button _monday;
    Button _tuesday;
    Button _wednesday;
    Button _thursday;
    Button _friday;
    Button _saturday;
    Button _sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_routine);
        _monday = (Button) findViewById(R.id.monday);
        _tuesday = (Button) findViewById(R.id.tuesday);
        _wednesday = (Button) findViewById(R.id.wednesday);
        _thursday = (Button) findViewById(R.id.thursday);
        _friday = (Button) findViewById(R.id.friday);
        _saturday = (Button) findViewById(R.id.saturday);
        _sunday = (Button) findViewById(R.id.sunday);
        resetDaySelected();
        dayString = getCurrentDay();
        try {
            setArrayList(getExerciseList(dayString));
        } catch (IOException e) {
        }
    }

    public void setArrayList(final Set<String> values)
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
                } else {
                    try {
                        showDialog(item, values);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    public void showDialog(String currentExercise, Set<String> list) throws IOException {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SetupRoutineDialog(currentExercise, dayString, list);
        dialog.show(getSupportFragmentManager(), "ExerciseInfoDialog");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        try {
            setArrayList(getExerciseList(dayString));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (resultCode == RESULT_OK) {
                dayString = readUserInformation("lastDay");
                data.getStringExtra("ExerciseLabel");
                Set<String> values = null;
                try {
                    values = getExerciseList(dayString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                values.add(data.getStringExtra("ExerciseLabel"));
                setSetItem("exerciseList" + dayString, values);
                setArrayList(values);
            }
        }
    }

    public void goToAddExercise()
    {
        setItem("lastDay", dayString);
        Intent intent = new Intent(this, AddExerciseActivity.class);
        intent.putExtra("dayValue", dayString);
        startActivityForResult(intent, 0);
    }

    private String readUserInformation(String key)
    {
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString(key, "");//"No name defined" is the default value.
        return name;
    }

    private void setSetItem(String key, Set<String> value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    private void setItem(String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private Set<String> getExerciseList(String day) throws IOException {
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        Set<String> newList = new HashSet<String>();
        Set<String> list = prefs.getStringSet("exerciseList" + day, newList);//"No name defined" is the default value.
        return list;
    }

    public void clearArrayOfWeights() throws IOException {
        Set<String> set = getExerciseList(dayString);
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            setItem(dayString + "_" + iter.next() + "_weight", "");
            try
            {
                setItem(dayString + "_" + iter.next() + "_weight_actual", "");
            }
            catch (NoSuchElementException e){

            }
        }
}

    private String getCurrentDay()
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                _sunday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "S";
            case Calendar.MONDAY:
                _monday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "M";
            case Calendar.TUESDAY:
                _tuesday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "T";
            case Calendar.WEDNESDAY:
                _wednesday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "W";
            case Calendar.THURSDAY:
                _thursday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "Th";
            case Calendar.FRIDAY:
                _friday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "F";
            case Calendar.SATURDAY:
                _saturday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return "Sat";
        }
        return "";
    }

    public void clearDay(View view) throws IOException {
        Set<String> newList = new HashSet<String>();
        clearArrayOfWeights();
        setSetItem("exerciseList" + dayString, newList);
        setArrayList(newList);
    }

    public void mondayList(View view) throws IOException {
        resetDaySelected();
        _monday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "M";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void tuesdayList(View view) throws IOException {
        resetDaySelected();
        _tuesday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "T";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void wednesdayList(View view) throws IOException {
        resetDaySelected();
        _wednesday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "W";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void thursdayList(View view) throws IOException {
        resetDaySelected();
        _thursday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "Th";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void fridayList(View view) throws IOException {
        resetDaySelected();
        _friday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "F";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void saturdayList(View view) throws IOException {
        resetDaySelected();
        _saturday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "Sat";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void sundayList(View view) throws IOException {
        resetDaySelected();
        _sunday.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        dayString = "Sun";
        Set<String> dayList = getExerciseList(dayString);
        setSetItem("exerciseList" + dayString, dayList);
        setArrayList(dayList);
    }

    public void resetDaySelected()
    {
        _sunday.setBackgroundResource(android.R.drawable.btn_default);
        _monday.setBackgroundResource(android.R.drawable.btn_default);
        _tuesday.setBackgroundResource(android.R.drawable.btn_default);
        _wednesday.setBackgroundResource(android.R.drawable.btn_default);
        _thursday.setBackgroundResource(android.R.drawable.btn_default);
        _friday.setBackgroundResource(android.R.drawable.btn_default);
        _saturday.setBackgroundResource(android.R.drawable.btn_default);
    }
}
