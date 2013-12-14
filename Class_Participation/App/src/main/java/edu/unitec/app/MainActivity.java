package edu.unitec.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
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
        populateListView();
        ClickCallback();
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
        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorSectionIdAndCourseId = db.rawQuery("SELECT SectionId, CourseId FROM section WHERE SectionQuarter = " +
                    quarter + " AND SectionYear = " + year + " ORDER BY SectionId ASC", null);

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
        }

        catch (Exception ignored)
        {
        }
        return sectionsList;
    }

    public List<String> getCurrentCoursesNamesList()
    {
        List<Section> sectionsList = getCurrentSectionsList();
        List<String> coursesNamesList = new ArrayList<String>();
        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        for (Section aSectionsList : sectionsList) {
            Cursor cursorCourseName = db.rawQuery("SELECT CourseName FROM course WHERE CourseId = " +
                    aSectionsList.get_CourseId() + " ORDER BY CourseId ASC", null);

            if (cursorCourseName.moveToFirst()) {
                coursesNamesList.add(cursorCourseName.getString(0));
            }
            cursorCourseName.close();
        }
        db.close();
        return coursesNamesList;
    }

    //creating the listView
    private void populateListView()
    {
        ArrayAdapter<String> adapter = new MyListAdapter();
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }

    //event clicking on one item of the list view
    private void ClickCallback()
    {
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                ListView listview = (ListView) findViewById(R.id.listView);
                Intent intent = new Intent(view.getContext(), StudentActivity.class);
                intent.putExtra("Section", getCurrentSectionsList().get(position));
                intent.putExtra("Course", listview.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
     }

    //class myAdapter for my personal style listView
    public class MyListAdapter extends ArrayAdapter<String>
    {
        public MyListAdapter()
        {
            super( MainActivity.this, R.layout.item_listview, getCurrentCoursesNamesList());
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
                List<Section> sectionsList = getCurrentSectionsList();

                //course name view
                TextView item_name = (TextView)itemView.findViewById(R.id.item_course_name);
                item_name.setText("" + getCurrentCoursesNamesList().get(position));

                //section year view
                TextView item_year = (TextView)itemView.findViewById(R.id.item_year);
                item_year.setText("" + sectionsList.get(position).get_SectionYear());

                //section quarter view
                TextView item_quarter = (TextView)itemView.findViewById(R.id.item_quarter);
                item_quarter.setText("Quarter: " + sectionsList.get(position).get_SectionQuarter());

                //section id view
                TextView item_IdSection = (TextView)itemView.findViewById(R.id.item_idSection);
                item_IdSection.setText("Section id: " + sectionsList.get(position).get_SectionId());
            }

            catch(Exception e)
            {
            }

            return itemView;
        }
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
