package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Henry on 12-01-13.
 */
public class CourseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onclickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                EditText course_code = (EditText) findViewById(R.id.course_code);
                EditText course_name = (EditText) findViewById(R.id.course_name);
                EditText course_description = (EditText) findViewById(R.id.course_description);
                String course_cod = course_code.getText().toString();
                String course_nam = course_name.getText().toString();
                String course_desp = course_description.getText().toString();

                Log.i("Info1",course_cod);
                Log.i("Info2",course_nam);
                Log.i("Info3",course_desp);

                if( !course_cod.isEmpty() && !course_nam.isEmpty() && !course_desp.isEmpty() ){
                    /*Course course = new Course();
                    DatabaseHandler admin = new DatabaseHandler(this);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    course.setCourseCode(course_code.getText().toString());
                    course.setCourseName(course_name.getText().toString());
                    course.setCourseDescription(course_description.getText().toString());
                    admin.addCourse(course);
                    bd.close();
                    Context context = getApplicationContext();
                    CharSequence text = "Success!!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();/*
                    //still an error 
                }else {
                    Context context = getApplicationContext();
                    CharSequence text = "ERROR!!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        startActivity(new Intent(this,MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
