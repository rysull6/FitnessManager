package com.hci.ryan.fitnessmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Ryan on 11/17/2015.
 */
public class ExerciseInfoDialog extends android.support.v4.app.DialogFragment {

    String exercise;
    String dayString;
    EditText sets;
    EditText reps;
    EditText weight;
    public ExerciseInfoDialog(String exercise, String dayString) {
        this.exercise = exercise;
        this.dayString = dayString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exercise_info_dialog, container, false);
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        weight = (EditText) v.findViewById(R.id.dialog_weight);
        reps = (EditText) v.findViewById(R.id.dialog_reps);
        sets = (EditText) v.findViewById(R.id.dialog_sets);

        weight.setText(readUserInformation(dayString + "_" + exercise + "_weight"));
        reps.setText(readUserInformation(dayString + "_" + exercise + "_reps"));
        sets.setText(readUserInformation(dayString + "_" + exercise + "_sets"));

        // Watch for button clicks.
        Button pos = (Button)v.findViewById(R.id.posButton);
        pos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                setItem(dayString + "_" + exercise + "_reps", reps.getText().toString());
                setItem(dayString + "_" + exercise + "_sets", sets.getText().toString());
                setItem(dayString + "_" + exercise + "_weight", weight.getText().toString());
                Intent i = new Intent();
                i.putExtra("ExerciseLabel", exercise);  // insert your extras here
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
            }
        });

        Button neg = (Button)v.findViewById(R.id.negButton);
        neg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ExerciseInfoDialog.this.getDialog().cancel();
            }
        });

        return v;
    }


    private String readUserInformation(String key)
    {
        SharedPreferences prefs = getActivity().getSharedPreferences(Common.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(key, "");//"No name defined" is the default value.
        return name;
    }


    private void setItem(String key, String value)
    {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Common.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
