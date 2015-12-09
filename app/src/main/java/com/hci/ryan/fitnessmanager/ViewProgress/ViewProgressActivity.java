package com.hci.ryan.fitnessmanager.ViewProgress;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hci.ryan.fitnessmanager.Common;
import com.hci.ryan.fitnessmanager.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ViewProgressActivity extends AppCompatActivity {

    Spinner _dateRange;
    Spinner _exerciseRange;
    String[] dayArray = {"S", "M", "T", "W", "Th", "F", "Sat"};
    Set<String> allList;
    TextView _noProgress;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_progress);
        _exerciseRange = (Spinner) findViewById(R.id.exerciseRange);
        _dateRange = (Spinner) findViewById(R.id.dateRange);
        _noProgress = (TextView) findViewById(R.id.noProgress);
        graph = (GraphView) findViewById(R.id.graph);

        ArrayList<String> exerciesList = getAllExercises();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exerciesList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _exerciseRange.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.date_range, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _dateRange.setAdapter(adapter2);
        _dateRange.setOnItemSelectedListener(new RangeClickListener());
        _exerciseRange.setOnItemSelectedListener(new ExerciseClickListener());
        if(_exerciseRange.getSelectedItem() != null)
        {
            setWeeklyGraph();
        }
        else
        {
            _dateRange.setVisibility(View.GONE);
            _exerciseRange.setVisibility(View.GONE);
            _noProgress.setVisibility(View.VISIBLE);
            graph.setVisibility(View.GONE);
        }
    }

    public void setDailyGraph()
    {
        graph.removeAllSeries();
        int actualWeight = 0;
        String actualWeightString = readUserInformation(getCurrentDay() + "_" + _exerciseRange.getSelectedItem().toString() + "_weight_actual");
        int goalWeight = 0;
        String goalWeightString = readUserInformation(getCurrentDay() + "_" + _exerciseRange.getSelectedItem().toString() + "_weight");

        if (actualWeightString != "")
        {
            actualWeight = Integer.parseInt(actualWeightString);
        }
        if (goalWeightString != "")
        {
            goalWeight = Integer.parseInt(goalWeightString);
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(3, goalWeight),
                new DataPoint(4, 0)
        });
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(4, actualWeight)
        });
        series.setColor(Color.GREEN);
        series2.setColor(Color.RED);
        graph.addSeries(series2);
        graph.addSeries(series);

        series.setTitle("Goal Weight");
        series2.setTitle("Actual Weight");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return "";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    public int[] getArrayOfWeights()
    {
        int [] list = new int[7];
        for(int x = 0; x<7; x++)
        {
            String temp = readUserInformation(dayArray[x] + "_" + _exerciseRange.getSelectedItem().toString() + "_weight");
            if(temp == "")
            {
                if (x > 0)
                {
                    list[x] = list[x-1];
                }
                else
                    list[x] = 0;
            }
            else{
                list[x] =  Integer.parseInt(temp);
            }
        }
        return list;
    }

    public int[] getArrayOfActualWeights()
    {
        int [] list = new int[7];
        for(int x = 0; x<7; x++)
        {
            String temp = readUserInformation(dayArray[x] + "_" + _exerciseRange.getSelectedItem().toString() + "_weight_actual");
            if(temp == "")
            {
                if (x > 0)
                {
                    list[x] = list[x-1];
                }
                else
                    list[x] = 0;
            }
            else{
                list[x] =  Integer.parseInt(temp);
            }
        }
        return list;
    }

    public void setWeeklyGraph(){
        graph.removeAllSeries();
        int [] list = getArrayOfWeights();
        int [] actual = getArrayOfActualWeights();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, list[0]),
                new DataPoint(1, list[1]),
                new DataPoint(2, list[2]),
                new DataPoint(3, list[3]),
                new DataPoint(4, list[4]),
                new DataPoint(5, list[5]),
                new DataPoint(6, list[6])

        });
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, actual[0]),
                new DataPoint(1, actual[1]),
                new DataPoint(2, actual[2]),
                new DataPoint(3, actual[3]),
                new DataPoint(4, actual[4]),
                new DataPoint(5, actual[5]),
                new DataPoint(6, actual[6])
        });
        series.setColor(Color.GREEN);
        series2.setColor(Color.RED);
        graph.addSeries(series);
        graph.addSeries(series2);
        series.setTitle("Goal Weight");
        series2.setTitle("Actual Weight");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    if (value == 0)
                    {
                        return "S";
                    }
                    if (value == 1)
                    {
                        return "M";
                    }
                    if (value == 2)
                    {
                        return "T";
                    }
                    if (value == 3)
                    {
                        return "W";
                    }
                    if (value == 4)
                    {
                        return "Th";
                    }
                    if (value == 5)
                    {
                        return "F";
                    }
                    if (value == 6)
                    {
                        return "Sat";
                    }
                    if (value == 7)
                    {
                        return "";
                    }
                    if (value == 8)
                    {
                        return "";
                    }
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    public ArrayList<String> getAllExercises(){
        allList = new HashSet<String>();
        for(int x = 0; x < 7; x++)
        {
            Set<String> tempList = null;
            try {
                tempList = getExerciseList(dayArray[x]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            allList.addAll(tempList);
        }
        final ArrayList<String> list = new ArrayList<String>();
        Iterator<String> iter = allList.iterator();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    private Set<String> getExerciseList(String day) throws IOException {
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        Set<String> newList = new HashSet<String>();
        //newList.add("New Exercise");
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

    private String readUserInformation(String key)
    {
        SharedPreferences prefs = getSharedPreferences(Common.MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString(key, "");//"No name defined" is the default value.
        return name;
    }

    public class RangeClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0)
            {
                setWeeklyGraph();
            }
            else
            {
                setDailyGraph();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class ExerciseClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(_dateRange.getSelectedItemPosition() == 0)
            {
                setWeeklyGraph();
            }
            else
            {
                setDailyGraph();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
