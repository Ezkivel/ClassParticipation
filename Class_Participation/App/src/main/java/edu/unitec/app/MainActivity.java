package edu.unitec.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        final ListView listview = (ListView) findViewById(R.id.listView);
        DatabaseHandler base = new DatabaseHandler(this);
        final List<String> list;
        try{
            list = base.getAllName_Courses();
            //list = getCurrentSectionCoursesName();
            if( !list.isEmpty() ){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {

                       // final String item = (String) parent.getItemAtPosition(position);
                        startActivity(new Intent(view.getContext(), StudentActivity.class));
                    }
                });
            }
        }catch(Exception e){
        }
    }

    public int getCurrentYear()
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public int getCurrentSemester()
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); //January == 0

        //June == 5
        if ( month <= 5 )
        {
            return 1;
        }

        return 2;
    }

    public int getCurrentQuarter()
    {
        Calendar calendar = Calendar.getInstance();

        int currentMonth = calendar.get(Calendar.MONTH); //January == 0
        int currentSemester = getCurrentSemester();
        int currentQuarter = 0;

        if ( currentSemester == 1 )
        {
            if ( currentMonth <= 2 )//January(0), February(1), March(2)
            {
                currentQuarter = 1;
            }

            //else
            currentQuarter = 2;
        }

        else if ( currentSemester == 2 )
        {
            if ( currentMonth <= 8 ) //(July(6), August(7), September(8)
            {
                currentQuarter = 3;
            }

            //else
            currentQuarter = 4;
        }

        return currentQuarter;
    }

    public List<String> getCurrentSectionCoursesName()
    {
        int year = getCurrentYear();
        int semester = getCurrentSemester();
        int quarter = getCurrentQuarter();
        List<String> coursesNameList = new ArrayList<String>();

        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor cursorCoursesId = db.rawQuery("SELECT CourseId FROM section WHERE SectionQuarter = " +
                                             quarter + " AND SectionYear = " + year, null);

        if ( cursorCoursesId.moveToFirst() )
        {
            while ( cursorCoursesId.moveToNext() )
            {
                Cursor cursorCoursesName = db.rawQuery("SELECT CourseName FROM course WHERE CourseId = " +
                                                 cursorCoursesId.getInt(0), null);

                if ( cursorCoursesName.moveToFirst() )
                {
                    coursesNameList.add(cursorCoursesName.getString(0));
                }
            }
        }

        db.close();

        return coursesNameList;
    }

    public void onclickItem(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.course:
                startActivity(new Intent(this,CourseActivity.class));
                break;
            case R.id.section:
                startActivity(new Intent(this,SectionActivity.class));
                break;
            case R.id.about:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
            return true;
        }else if(id == R.id.course){
            return true;
        }else if(id == R.id.section){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
