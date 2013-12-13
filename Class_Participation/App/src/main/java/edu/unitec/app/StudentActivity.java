package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Henry on 12-08-13.
 */
public class StudentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //course id
        Intent intent = getIntent();
        Section currentSection = (Section)intent.getSerializableExtra("Section");
        String course_name = intent.getStringExtra("course");
        setTitle(course_name);
        Log.i("Section", currentSection.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
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

    public void onclickItem(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.save_students:
                //import excel
                break;
        }
    }
}
