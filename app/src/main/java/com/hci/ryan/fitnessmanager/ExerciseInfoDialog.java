package com.hci.ryan.fitnessmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by Ryan on 11/17/2015.
 */
public class ExerciseInfoDialog extends android.support.v4.app.DialogFragment {

    String exercise;
    public ExerciseInfoDialog(String exercise) {
        this.exercise = exercise;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.exercise_info_dialog, null))
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent();
                            i.putExtra("ExerciseLabel", exercise);  // insert your extras here
                            getActivity().setResult(Activity.RESULT_OK, i);
                            getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExerciseInfoDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
