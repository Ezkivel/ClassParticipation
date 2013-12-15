package edu.unitec.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Henry on 12-08-13.
 */
public class StudentActivity extends Activity
{
    final int ACTIVITY_CHOOSE_FILE = 1;
    private Section currentSection = new Section();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        currentSection = (Section)intent.getSerializableExtra("Section");
        String course_name = intent.getStringExtra("Course");
        setTitle(course_name);

        populateListView();
        ClickCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        MenuItem a = menu.findItem(R.id.save_students);
        if(!getCurrentStudentNamesList().isEmpty() ){
            a.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //startActivity(new Intent(this,MainActivity.class));
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onclickItem(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.save_students:
                //import excel
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("file/*");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                break;

            case R.id.item_newParticipation:
                showParticipationDialog();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_CHOOSE_FILE:
                if (resultCode == RESULT_OK){
                    try{
                        Uri uri = data.getData();
                        String filePath = uri.getPath();
                        Log.i("path", filePath);

                        //reading the path of the file
                        ReadWriteFileManager file = new ReadWriteFileManager();
                        List<Student> student = file.readFromFile(this, filePath);

                        List<Integer> list1 = getAllStudentIDList();
                        List<Integer> list2 = getCurrentStudentIdList();

                        DatabaseHandler bd = new DatabaseHandler(this);
                        //validate
                        if( getCurrentStudentNamesList().isEmpty() ){

                            if( !list1.containsAll( list2 ) ){
                                for (Student aStudent : student) {
                                    bd.addStudent(aStudent);
                                    bd.addStudentTable(aStudent, currentSection);
                                }
                                this.recreate();
                            }else{
                                for (Student aStudent : student) {
                                    bd.addStudentTable(aStudent, currentSection);
                                }
                                this.recreate();
                                Log.i("student","students list already exist");
                            }
                        }else{
                            Log.i("Nothing","do nothing");
                        }
                    }catch(Exception ignored){
                    }
                }
                break;
        }
    }

    public List<Integer> getAllStudentIDList()
    {
        List<Integer> StudentList = new ArrayList<Integer>();
        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorStudent = db.rawQuery("SELECT StudentId FROM student ORDER BY StudentId ASC", null);
            if ( cursorStudent.moveToFirst() )
            {
                do
                {
                    StudentList.add(cursorStudent.getInt(0));
                } while ( cursorStudent.moveToNext() );
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return StudentList;
    }

    public List<Integer> getCurrentStudentSectionIdList()
    {
        List<Integer> StudentSectionIdList = new ArrayList<Integer>();

        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            Cursor cursorStudentSectionId = db.rawQuery("SELECT StudentSectionId FROM studentSection WHERE SectionId = " +
                    currentSection.get_SectionId() + " ORDER BY SectionId ASC", null);

            if ( cursorStudentSectionId.moveToFirst() )
            {
                do
                {
                    StudentSectionIdList.add(cursorStudentSectionId.getInt(0));

                } while ( cursorStudentSectionId.moveToNext() );
            }

            db.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return StudentSectionIdList;
    }

    public List<Integer> getCurrentStudentIdList()
    {
        List<Integer> StudentIdList = new ArrayList<Integer>();
        try
        {
            SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            Cursor cursorSectionId = db.rawQuery("SELECT StudentId FROM studentSection WHERE SectionId = " +
                    currentSection.get_SectionId() + " ORDER BY SectionId ASC", null);
            if ( cursorSectionId.moveToFirst() )
            {
                do
                {
                    StudentIdList.add(cursorSectionId.getInt(0));

                } while ( cursorSectionId.moveToNext() );
            }

            db.close();

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
        return StudentIdList;
    }


    public List<String> getCurrentStudentNamesList()
    {
        List<Integer> studentId = getCurrentStudentIdList();
        List<String> studentNamesList = new ArrayList<String>();
        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        for (Integer aStudentId : studentId) {
            Cursor cursorStudentName = db.rawQuery("SELECT StudentName FROM student WHERE StudentId = " +
                    aStudentId + " ORDER BY StudentId ASC", null);

            if (cursorStudentName.moveToFirst()) {
                studentNamesList.add(cursorStudentName.getString(0));
            }
            cursorStudentName.close();
        }
        db.close();
        return studentNamesList;
    }

    //creating the listView
    private void populateListView()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getCurrentStudentNamesList());
        ListView listview = (ListView) findViewById(R.id.listView_student);
        listview.setAdapter(adapter);
    }

    public int getMinValueIndex(int[] array)
    {
        int minValue = array[0];
        int index = 0;
        int i;

        for(i = 1; i < array.length; i++)
        {
            if(array[i] < minValue)
            {
                minValue = array[i];
                index = i;
            }
        }
        return index;
    }

    public int selectStudent()
    {
        List<Integer> studentSectionIdList = getCurrentStudentSectionIdList();
        int studentSectionIdCounters[] = new int[studentSectionIdList.size()];

        SQLiteDatabase db = openOrCreateDatabase("Participation", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        for (int a = 0; a < studentSectionIdList.size(); a++)
        {
            Cursor cursor = db.rawQuery("SELECT * FROM participationstudent WHERE StudentSectionId = " +
                    studentSectionIdList.get(a), null);

            studentSectionIdCounters[a] = cursor.getCount();
        }

        db.close();

        //---------------------------------------------------------------------------------------------

        int studentIndex = 0;

        Random random = new Random();
        int randomValue = random.nextInt(6 - 1) + 1;

        //Less student participation
        if ( ( randomValue == 1 ) || ( randomValue == 2 ) || ( randomValue == 3 ) )
        {
            studentIndex = getMinValueIndex(studentSectionIdCounters);
        }

        //Random student
        else
        {
            studentIndex = random.nextInt(studentSectionIdList.size());
        }

        return studentIndex;
    }

    public void showParticipationDialog()
    {
        if ( getCurrentStudentNamesList().size() > 0 )
        {
            int studentIndex = selectStudent();

            ParticipationDialog dialog = new ParticipationDialog(getCurrentStudentSectionIdList().get(studentIndex),
                    getCurrentStudentNamesList().get(studentIndex));

            dialog.show(getFragmentManager(), "dialog_participation");
        }
    }

    //event clicking on one item of the list view
    private void ClickCallback()
    {
        ListView listview = (ListView) findViewById(R.id.listView_student);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
            }
        });
    }
}
