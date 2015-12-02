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

public class ViewProfileActivity extends AppCompatActivity {
    EditText _userName;
    EditText _userHeight;
    EditText _userWeight;
    Spinner _userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        _userName = (EditText) findViewById(R.id.name);
        _userHeight = (EditText) findViewById(R.id.height);
        _userWeight = (EditText) findViewById(R.id.weight);
        _userLevel = (Spinner) findViewById(R.id.level);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.fitness_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _userLevel.setAdapter(adapter);
        setUserInformation();
    }

    private void setUserInformation()
    {
        String name = readUserInformation("name");
        _userName.setText(name);
        String height = readUserInformation("height");
        _userHeight.setText(height);
        String weight = readUserInformation("weight");
        _userWeight.setText(weight);
        String level = readUserInformation("level");
        _userLevel.setSelection(getPosition(level));
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

    public void onSave(View view)
    {
        setItem("name", _userName.getText().toString());
        setItem("height", _userHeight.getText().toString());
        setItem("weight", _userWeight.getText().toString());
        setItem("level", _userLevel.getSelectedItem().toString());
        finish();
    }

    public int getPosition(String stringLevel)
    {
        if (stringLevel.equals("Beginner"))
        {
            return 0;
        }
        else if(stringLevel.equals("Intermediate"))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }


}
