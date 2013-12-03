package edu.unitec.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

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
    }

    public void onclickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.save:
                Course course = new Course();
                EditText course_code = (EditText) findViewById(R.id.course_code);
                EditText course_name = (EditText) findViewById(R.id.course_name);
                EditText course_description = (EditText) findViewById(R.id.course_description);
                course.setCourseCode(course_code.getText().toString());
                course.setCourseName(course_name.getText().toString());
                course.setCourseDescription(course_description.getText().toString());
                Context context = null;
                DatabaseHandler bd = new DatabaseHandler(context);
                bd.addCourse(course);

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
        int id = item.getItemId();
        if (id == R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
