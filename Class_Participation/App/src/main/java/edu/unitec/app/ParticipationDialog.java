package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ariel on 12-14-13.
 */
public class ParticipationDialog extends DialogFragment
{
    int studentSectionId;
    String studentName;

    ParticipationDialog(int studentSectionId, String studentName)
    {
        this.studentSectionId = studentSectionId;
        this.studentName = studentName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_participation, null);

        builder.setTitle(studentName);
        builder.setView(view);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                int grade = 0;

                if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 1 )
                {
                    grade = 20;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 2 )
                {
                    grade = 40;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 3 )
                {
                    grade = 60;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 4 )
                {
                    grade = 80;
                }

                else if ( ((RatingBar)view.findViewById(R.id.ratingBar)).getRating() == 5 )
                {
                    grade = 100;
                }


                try
                {
                    //-------------------------Save the participation-----------------------------------

                    String date = new SimpleDateFormat("dd-MM-yyy").format(new Date());
                    String comment = ((EditText)view.findViewById(R.id.editTextComment)).getText().toString();

                    //Database
                    SQLiteDatabase db = SQLiteDatabase.openDatabase("Participation", null, 0);

                    db.execSQL("INSERT INTO participationstudent(StudentSectionId, ParticipationGrade, ParticipationDate, ParticipationComment) VALUES(" +
                            studentSectionId + ", " + grade + ", '" + date + "', '" + comment + "')");

                    //-----------------------------------Update the final note---------------------------

                    int studentSectionFinal = 0;

                    Cursor cursorStudentSectionFinal = db.rawQuery("SELECT StudentSectionFinal FROM studentSection WHERE StudentSectionId = " +
                            studentSectionId, null);

                    if ( cursorStudentSectionFinal.moveToFirst() )
                    {
                        studentSectionFinal = cursorStudentSectionFinal.getInt(0);
                    }

                    studentSectionFinal += grade;

                    db.execSQL("UPDATE studentSection SET StudentSectionFinal =  " + studentSectionFinal + " WHERE StudentSectionId = " +
                            studentSectionId);

                    db.close();
                }

                catch (Exception e)
                {
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                ParticipationDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}