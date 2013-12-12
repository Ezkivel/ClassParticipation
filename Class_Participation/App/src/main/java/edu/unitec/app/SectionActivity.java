package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Henry on 12-02-13.
 */
public class SectionActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        loadYears();
        loadCourses();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    public void onclickItem(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.save:
                saveSection();
                break;
        }
    }

    public void loadYears()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        List<String> yearList = new ArrayList<String>();

        for (int a = -3; a <= 3; a++)
        {
            yearList.add(Integer.toString(year + a));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner)findViewById(R.id.spinnerYear)).setAdapter(dataAdapter);

        ((Spinner)findViewById(R.id.spinnerYear)).setSelection(3);
    }

    public void loadCourses()
    {
        List<String> courseList = new DatabaseHandler(this).getAllName_Courses();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ((Spinner)findViewById(R.id.spinnerCourse)).setAdapter(dataAdapter);
    }

    public void saveSection()
    {
        //If there are no courses
        if ( ((Spinner)findViewById(R.id.spinnerCourse)).getSelectedItemPosition() < 0 )
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Empty Course");
            alert.setMessage("There are no courses");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        else
        {
            int quarter = 0, semester = 0;
            int year = Integer.parseInt(((Spinner)findViewById(R.id.spinnerYear)).getSelectedItem().toString());
            int course = ((Spinner)findViewById(R.id.spinnerCourse)).getSelectedItemPosition() + 1;

            //Checks which quarter was selected
            if ( ((RadioButton)findViewById(R.id.radioButtonQuarter1)).isSelected() )
            {
                quarter = 1;
            }

            else if ( ((RadioButton)findViewById(R.id.radioButtonQuarter2)).isSelected() )
            {
                quarter = 2;
            }

            else if ( ((RadioButton)findViewById(R.id.radioButtonQuarter3)).isSelected() )
            {
                quarter = 3;
            }

            else
            {
                quarter = 4;
            }

            //Checks which semester was selected
            if ( ((RadioButton)findViewById(R.id.radioButtonSemester1)).isSelected() )
            {
                semester = 1;
            }

            else
            {
                semester = 2;
            }

            //Database
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            db.execSQL("INSERT INTO section(CourseId, SectionQuarter, SectionSemester, SectionYear) VALUES(" +
            course + ", " + quarter + ", " + semester + ", " + year + ")");

            db.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        startActivity(new Intent(this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
