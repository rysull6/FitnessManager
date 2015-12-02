package com.hci.ryan.fitnessmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hci.ryan.fitnessmanager.LogWorkout.LogWorkoutActivity;
import com.hci.ryan.fitnessmanager.SetupRoutine.SetupRoutineActivity;
import com.hci.ryan.fitnessmanager.ViewProfile.ViewProfileActivity;
import com.hci.ryan.fitnessmanager.ViewProgress.ViewProgressActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button _setUpButton;
    Button _workoutButton;
    Button _progressButton;
    Button _profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initStorage();
        } catch (IOException e) {
        }
        setContentView(R.layout.activity_main);
        _setUpButton = (Button)findViewById(R.id.setup);
        _workoutButton = (Button)findViewById(R.id.workout);
        _progressButton = (Button)findViewById(R.id.progress);
        _profileButton = (Button)findViewById(R.id.profile);
    }

    public void goToSetup(View view)
    {
        Intent intent = new Intent(this, SetupRoutineActivity.class);
        startActivity(intent);
    }


    public void goToWorkout(View view)
    {
        Intent intent = new Intent(this, LogWorkoutActivity.class);
        startActivity(intent);
    }

    public void goToProgress(View view)
    {
        Intent intent = new Intent(this, ViewProgressActivity.class);
        startActivity(intent);
    }


    public void goToProfile(View view)
    {
        Intent intent = new Intent(this, ViewProfileActivity.class);
        startActivity(intent);
    }

    private void initStorage() throws IOException {
        if(!isStorageInitialized()) {
            SharedPreferences.Editor editor = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("name", "");
            editor.putString("height", "");
            editor.putString("level", "Beginner");
            editor.putString("weight", "");
            editor.apply();
        }
    }

    private boolean isStorageInitialized() throws IOException{
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", null);//"No name defined" is the default value.
        return name != null;
    }

}
