package com.msquare.omr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentEntryAdapter extends RecyclerView.Adapter<StudentEntryAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private int numberOfStudents;
    ArrayList<String> arrayList;
    HashMap<String,String> answers;
    HashMap<String,String> studentAnswers;
    HashMap<String,String> studentMarks;
    HashMap<String,String> testInfo;
    HashMap<String,HashMap<String, String>> saveAnswers;
    Button evaluateMarks;
    ArrayList<String> selectedStudents;


    public StudentEntryAdapter(ArrayList<String> arrayList, HashMap<String,String> testInfo, HashMap<String,String> answers, HashMap<String,String> studentAnswers, HashMap<String,String> studentMarks, HashMap<String,HashMap<String, String>> saveAnswers,Button evaluateMarks, ArrayList<String> selectedStudents,Context context, int numberOfStudents) {
        this.arrayList = arrayList;
        this.testInfo = testInfo;
        this.answers = answers;
        this.studentAnswers = studentAnswers;
        this.studentMarks = studentMarks;
        this.saveAnswers = saveAnswers;
        this.evaluateMarks = evaluateMarks;
        this.selectedStudents = selectedStudents;
        this.inflater = LayoutInflater.from(context);
        this.numberOfStudents = numberOfStudents;
    }

    @NonNull
    @Override
    public StudentEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.student_roll_no_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentEntryAdapter.ViewHolder holder, int position) {
        holder.textViewStudentNumber.setText("Roll Number "+arrayList.get(position));

        if( selectedStudents.contains(arrayList.get(position)) ){
            holder.relativeLayout.setBackgroundColor(Color.GREEN);
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.relativeLayout.getContext(),StudentAnswersActivity.class);
                intent.putExtra("TestInfo", (HashMap<String,String>) testInfo);
                intent.putExtra("roll no",arrayList.get(holder.getAdapterPosition()));
                intent.putExtra("answers",(HashMap<String,String>) answers);
                intent.putExtra("studentAnswers",(HashMap<String,String>) studentAnswers);
                intent.putExtra("studentMarks",(HashMap<String,String>) studentMarks);
                intent.putExtra("saveAnswers",(HashMap<String,HashMap<String, String>>) saveAnswers);
                intent.putExtra("selectedStudents",(ArrayList<String>) selectedStudents);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.relativeLayout.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return numberOfStudents;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewStudentNumber;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentNumber = (TextView) itemView.findViewById(R.id.textView_roll_number);
            relativeLayout = itemView.findViewById(R.id.roll_number_entry);
        }
    }
}
