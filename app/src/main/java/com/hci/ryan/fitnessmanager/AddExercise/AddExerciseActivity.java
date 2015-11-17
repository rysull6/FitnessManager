package com.hci.ryan.fitnessmanager.AddExercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hci.ryan.fitnessmanager.ExerciseInfoDialog;
import com.hci.ryan.fitnessmanager.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ryan on 11/16/2015.
 */
public class AddExerciseActivity extends AppCompatActivity implements ExerciseInfoDialog.ExerciseInfoDialogListener {

    String currentExercise = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);
        GridView gridView = (GridView) findViewById(R.id.grid_container);

        final String[] allExercises = new String[] {
                "Bench Press", "Bicep Curls", "Pull Ups", "Preacher Curls", "Overhead Press",
                "Squats", "Leg Press", "Deadlifts", "Lat Pulldowns", "Running",
                "Swimming", "Push Ups", "Sit Ups"};;
        ArrayList<String> arrayList = new ArrayList( Arrays.asList( allExercises ) );
        MyListAdapter adapter = new MyListAdapter(this,arrayList);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                currentExercise = ((TextView) v).getText().toString();
                showDialog();
            }
        });

    }

    public void showDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ExerciseInfoDialog();
        dialog.show(getSupportFragmentManager(), "ExerciseInfoDialog");
    }


        public void goToPreviousScreen()
    {
        Intent i = new Intent();
        i.putExtra("ExerciseLabel", currentExercise);  // insert your extras here
        setResult(0, i);
        finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        goToPreviousScreen();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public class MyListAdapter extends BaseAdapter {
        // ArrayList<String> name, company, email, id, status;
        ArrayList<String> myArrayList;
        Context c;

        public MyListAdapter(Context c, ArrayList<String> list) {
            myArrayList = list;
            this.c = c;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return myArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View row = null;
            LayoutInflater inflater = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                row = inflater.inflate(R.layout.simple_grid_item_1, parent,
                        false);
            } else {
                row = convertView;
            }
            String exercise = myArrayList.get(position);
            TextView name = (TextView) row.findViewById(R.id.grid_item_label);
            name.setText(exercise);
            return row;
        }

    }

}