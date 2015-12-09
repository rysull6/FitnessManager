package com.hci.ryan.fitnessmanager.ViewProfile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.hci.ryan.fitnessmanager.Common;
import com.hci.ryan.fitnessmanager.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ryan on 12/3/2015.
 */
public class ViewProfile2 extends AppCompatActivity {

    Spinner _hours;
    Spinner _goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_2);
        _hours = (Spinner) findViewById(R.id.hours_per_week);
        _goal = (Spinner) findViewById(R.id.fitness_goal);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.fitness_hours, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _hours.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.fitness_goals, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _goal.setAdapter(adapter2);
    }


    private void setItem(String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
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

    public void goBack(View view)
    {
        onBackPressed();
    }

    public void onGenerate(View view)
    {
        setItem("hours", _hours.getSelectedItem().toString());
        setItem("goal", _goal.getSelectedItem().toString());
        setRoutine();
        finish();
    }

    public void setRoutine()
    {
        final String[] cardioList = new String[] {
                "Running", "Swimming", "Push Ups", "Sit Ups"};
        final String[] strengthList = new String[] {
                "Bench Press", "Squats", "Deadlifts"};

        if(readUserInformation("goal").equals("To Improve Cardio")){
            ArrayList<String> arrayList = new ArrayList( Arrays.asList(cardioList) );
            Set<String> set = new HashSet<String>(arrayList);
            setSetItem("exerciseList" + "M", set);
            setSetItem("exerciseList" + "W", set);
            setSetItem("exerciseList" + "F", set);
            setItem("M" + "_" + "Running" + "_reps", "4");
            setItem("M" + "_" + "Swimming" + "_reps", "3");
            setItem("M" + "_" + "Push Ups" + "_reps", "6");
            setItem("M" + "_" + "Sit Ups" + "_reps", "6");

            setItem("M" + "_" + "Running" + "_sets", "2");
            setItem("M" + "_" + "Swimming" + "_sets", "2");
            setItem("M" + "_" + "Push Ups" + "_sets", "5");
            setItem("M" + "_" + "Sit Ups" + "_sets", "5");

            setItem("W" + "_" + "Running" + "_reps", "4");
            setItem("W" + "_" + "Swimming" + "_reps", "3");
            setItem("W" + "_" + "Push Ups" + "_reps", "6");
            setItem("W" + "_" + "Sit Ups" + "_reps", "6");

            setItem("W" + "_" + "Running" + "_sets", "2");
            setItem("W" + "_" + "Swimming" + "_sets", "2");
            setItem("W" + "_" + "Push Ups" + "_sets", "5");
            setItem("W" + "_" + "Sit Ups" + "_sets", "5");

            setItem("F" + "_" + "Running" + "_reps", "4");
            setItem("F" + "_" + "Swimming" + "_reps", "3");
            setItem("F" + "_" + "Push Ups" + "_reps", "6");
            setItem("F" + "_" + "Sit Ups" + "_reps", "6");

            setItem("F" + "_" + "Running" + "_sets", "2");
            setItem("F" + "_" + "Swimming" + "_sets", "2");
            setItem("F" + "_" + "Push Ups" + "_sets", "5");
            setItem("F" + "_" + "Sit Ups" + "_sets", "5");
        }
        else //if(readUserInformation("goal").equals("To Get Stringer"))
        {
            ArrayList<String> arrayList = new ArrayList( Arrays.asList(strengthList) );
            Set<String> set = new HashSet<String>(arrayList);
            setSetItem("exerciseList" + "T", set);
            setSetItem("exerciseList" + "Th", set);
            setSetItem("exerciseList" + "Sat", set);
            setItem("T" + "_" + "Bench Press" + "_reps", "5");
            setItem("T" + "_" + "Squats" + "_reps", "5");
            setItem("T" + "_" + "Push Ups" + "_reps", "5");

            setItem("Th" + "_" + "Bench Press" + "_reps", "3");
            setItem("Th" + "_" + "Squats" + "_reps", "3");
            setItem("Th" + "_" + "Deadlifts" + "_reps", "3");

            setItem("Sat" + "_" + "Bench Press" + "_reps", "1");
            setItem("Sat" + "_" + "Squats" + "_reps", "1");
            setItem("Sat" + "_" + "Deadlifts" + "_reps", "1");

            setItem("T" + "_" + "Bench Press" + "_sets", "5");
            setItem("T" + "_" + "Squats" + "_sets", "5");
            setItem("T" + "_" + "Deadlifts" + "_sets", "5");

            setItem("Th" + "_" + "Bench Press" + "_sets", "3");
            setItem("Th" + "_" + "Squats" + "_sets", "3");
            setItem("Th" + "_" + "Deadlifts" + "_sets", "3");

            setItem("Sat" + "_" + "Bench Press" + "_sets", "3");
            setItem("Sat" + "_" + "Squats" + "_sets", "3");
            setItem("Sat" + "_" + "Deadlifts" + "_sets", "3");
        }
    }

}
