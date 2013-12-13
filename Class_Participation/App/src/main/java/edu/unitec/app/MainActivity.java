package edu.unitec.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ListView;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        showCoursesNames();
        //populateListView();
        //ClickCallback();
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

    public List<Section> getCurrentSectionsList()
    {
        int year = getCurrentYear();
        int quarter = getCurrentQuarter();
        int semester = getCurrentSemester();

        List<Section> sectionsList = new ArrayList<Section>();

        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor cursorSectionIdAndCourseId = db.rawQuery("SELECT SectionId, CourseId FROM section WHERE SectionQuarter = " +
                                       quarter + " AND SectionYear = " + year, null);

        if ( cursorSectionIdAndCourseId.moveToFirst() )
        {
            do
            {
                Section section = new Section(cursorSectionIdAndCourseId.getInt(0), cursorSectionIdAndCourseId.getInt(1),
                                              quarter, semester, year);

                sectionsList.add(section);

            } while ( cursorSectionIdAndCourseId.moveToNext() );
        }

        db.close();

        return sectionsList;
    }

    public List<String> getCurrentCoursesNamesList()
    {
        List<Section> sectionsList = getCurrentSectionsList();
        List<String> coursesNamesList = new ArrayList<String>();

        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        for (int a = 0; a < sectionsList.size(); a++)
        {
            Cursor cursorCourseName = db.rawQuery("SELECT CourseName FROM course WHERE CourseId = " +
                                                    sectionsList.get(a).get_CourseId(), null);

            if ( cursorCourseName.moveToFirst() )
            {
                coursesNamesList.add(cursorCourseName.getString(0));
            }
        }

        db.close();

        return coursesNamesList;
    }

    public void showCoursesNames()
    {
        final ListView listview = (ListView) findViewById(R.id.listView);
        final List<String> coursesNamesList;

        try
        {
            coursesNamesList = getCurrentCoursesNamesList();

            if( !coursesNamesList.isEmpty() )
            {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_list_item_1, coursesNamesList);

                listview.setAdapter(adapter);

                /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {

                        // final String item = (String) parent.getItemAtPosition(position);
                        startActivity(new Intent(view.getContext(), StudentActivity.class));
                    }
                });*/
            }
        }

        catch(Exception e)
        {
        }
    }

    //creating the listView
    /*private void populateListView()
    {
        ArrayAdapter<Course> adapter = new MyListAdapter();
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }

    //event clicking on one item of the listview
    private void ClickCallback()
    {
        ListView listview = (ListView) findViewById(R.id.listView);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                Intent intent = new Intent(view.getContext(), StudentActivity.class);
                intent.putExtra("Id_course", ""+listCourse.get(position).getCourseId());
                startActivity(intent);
            }
        });
     }*/



    //class myAdapter for my personal style listView
    /*public class MyListAdapter extends ArrayAdapter<Course>
    {
        public MyListAdapter()
        {
            super( MainActivity.this, R.layout.item_listview, listCourse);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent )
        {
            View itemView = convertView;

            //if itemView is null we create a new one
            if(itemView == null )
            {
                itemView = getLayoutInflater().inflate(R.layout.item_listview, parent, false);
            }

            //find the course to work with and the section
            try
            {
                //staring the current course and section
                Course currentCourse = listCourse.get(position);

                //Section currentSection = listSection.get(position);
                //fill the view

                //section id view
                //TextView item_code = (TextView) itemView.findViewById(R.id.item_sectionId);
                //item_code.setText(""+currentSection.get_SectionId());

                //course name view
                TextView item_name = (TextView) itemView.findViewById(R.id.item_course_name);
                item_name.setText(""+currentCourse.getCourseName());

                //course code view
                TextView item_code = (TextView) itemView.findViewById(R.id.item_code_course);
                item_code.setText(""+currentCourse.getCourseCode());

            }

            catch(Exception e)
            {
            }

            return itemView;
        }
    }*/

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
