package edu.unitec.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ariel on 12-14-13.
 */
public class StudentDialog extends DialogFragment
{
    List<Participation> studentParticipationList;
    String studentName;
    double finalGrade;

    StudentDialog(List<Participation> list, String studentName, double finalGrade)
    {
        studentParticipationList = list;
        this.studentName = studentName;
        this.finalGrade = finalGrade/list.size();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_student, null);

        builder.setTitle(studentName + " (" + Double.toString(finalGrade) + "%)");
        builder.setView(view);
        builder.setPositiveButton("OK", null);

        //-----------------------------------------------------------------------------------------

        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.tableLayout);

        TableRow tableRowHead = new TableRow(view.getContext());
        tableRowHead.setBackgroundColor(Color.GRAY);

        TextView textViewGrade = new TextView(view.getContext());
        textViewGrade.setText("Grade");
        tableRowHead.addView(textViewGrade);

        TextView textViewDate = new TextView(view.getContext());
        textViewDate.setText("Date");
        tableRowHead.addView(textViewDate);

        TextView textViewComment = new TextView(view.getContext());
        textViewComment.setText("Comment");
        tableRowHead.addView(textViewComment);

        tableLayout.addView(tableRowHead);

        for (int a = 0; a < studentParticipationList.size(); a++)
        {
            TableRow tableRow = new TableRow(view.getContext());

            TextView textViewGrade2 = new TextView(view.getContext());
            textViewGrade2.setText(Double.toString(studentParticipationList.get(a).get_ParticipationGrade()));
            tableRow.addView(textViewGrade2);

            TextView textViewDate2 = new TextView(view.getContext());
            textViewDate2.setText(studentParticipationList.get(a).get_ParticipationDate());
            tableRow.addView(textViewDate2);

            TextView textViewComment2 = new TextView(view.getContext());
            textViewComment2.setText(studentParticipationList.get(a).get_ParticipationComment());
            tableRow.addView(textViewComment2);

            tableLayout.addView(tableRow);
        }

        return builder.create();
    }
}
