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
import android.widget.TextView;

/**
 * Created by Ryan on 11/17/2015.
 */
public class ExerciseActualDialog extends android.support.v4.app.DialogFragment {

    String exercise;
    String dayString;
    TextView goalSets;
    TextView goalReps;
    TextView goalWeight;
    EditText actualSets;
    EditText actualReps;
    EditText actualWeight;

    public ExerciseActualDialog(String exercise, String dayString) {
        this.exercise = exercise;
        this.dayString = dayString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exercise_actual_dialog, container, false);
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        goalWeight = (TextView) v.findViewById(R.id.goalWeight);
        goalReps = (TextView) v.findViewById(R.id.goalReps);
        goalSets = (TextView) v.findViewById(R.id.goalSets);
        actualWeight = (EditText) v.findViewById(R.id.dialog_weight_actual);
        actualReps = (EditText) v.findViewById(R.id.dialog_reps_actual);
        actualSets = (EditText) v.findViewById(R.id.dialog_sets_actual);

        goalWeight.setText(readUserInformation(dayString + "_" + exercise + "_weight"));
        goalReps.setText(readUserInformation(dayString + "_" + exercise + "_reps"));
        goalSets.setText(readUserInformation(dayString + "_" + exercise + "_sets"));

        actualWeight.setText(readUserInformation(dayString + "_" + exercise + "_weight_actual"));
        actualReps.setText(readUserInformation(dayString + "_" + exercise + "_reps_actual"));
        actualSets.setText(readUserInformation(dayString + "_" + exercise + "_sets_actual"));

        // Watch for button clicks.
        Button pos = (Button)v.findViewById(R.id.posButton_actual);
        pos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                setItem(dayString + "_" + exercise + "_reps_actual", actualReps.getText().toString());
                setItem(dayString + "_" + exercise + "_sets_actual", actualSets.getText().toString());
                setItem(dayString + "_" + exercise + "_weight_actual", actualWeight.getText().toString());
                ExerciseActualDialog.this.getDialog().cancel();
            }
        });

        Button neg = (Button)v.findViewById(R.id.negButton_actual);
        neg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ExerciseActualDialog.this.getDialog().cancel();
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
