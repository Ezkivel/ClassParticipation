package edu.unitec.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry on 12-02-13.
 */
public class MainActivity extends Activity {

    private List<Course> listCourse;
    private List<Section> listSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        populateCourseList_SectionList();
        populateListView();
        ClickCallback();
    }
    //filling the list course and list section
    private void populateCourseList_SectionList(){
        DatabaseHandler base = new DatabaseHandler(this);
        try{
            listCourse = base.getAllCourses();
           // listSection = base.getAllSections();
        }catch(Exception e){
        }
    }
    //creating the listView
    private void populateListView(){
        ArrayAdapter<Course> adapter = new MyListAdapter();
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }
    //event clicking on one item of the listview
    private void ClickCallback(){
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
               Intent intent = new Intent(view.getContext(), StudentActivity.class);
               intent.putExtra("Id_course", ""+listCourse.get(position).getCourseId());
               startActivity(intent);
            }
        });
    }
    //class myAdapter for my personal style listView
    public class MyListAdapter extends ArrayAdapter<Course> {
       public MyListAdapter(){
         super( MainActivity.this, R.layout.item_listview, listCourse);
       }
        @Override
        public View getView(int position, View convertView, ViewGroup parent ){
            View itemView = convertView;
            //if itemView is null we create a new one
            if(itemView == null ){
                itemView = getLayoutInflater().inflate(R.layout.item_listview, parent, false);
            }
            //find the course to work with and the section
            try{
               //staring the current course and section
                /*ariel validar aka el anho o lo podes hacer en populateCourseList_SectionList creo que en
                populateCourseList_SectionList seria menor*/

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

            }catch(Exception e){
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
