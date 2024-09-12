package com.msquare.omr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentResultEntryAdapter extends RecyclerView.Adapter<StudentResultEntryAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private int numberOfStudents;
    ArrayList<String> arrayList;
    HashMap<String,String> testInfo, finalMarks;
    HashMap<String,HashMap<String, String>> saveFinalAnswers;
    HashMap<String, String> studentFinalAnswers;


    public StudentResultEntryAdapter(ArrayList<String> arrayList, HashMap<String,String> testInfo, HashMap<String,String> finalMarks, HashMap<String,HashMap<String, String>> saveFinalAnswers, HashMap<String, String> studentFinalAnswers,Context context, int numberOfStudents) {
        this.arrayList = arrayList;
        this.testInfo = testInfo;
        this.finalMarks = finalMarks;
        this.saveFinalAnswers = saveFinalAnswers;
        this.studentFinalAnswers = studentFinalAnswers;
        this.inflater = LayoutInflater.from(context);
        this.numberOfStudents = numberOfStudents;
    }


    @NonNull
    @Override
    public StudentResultEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.student_result_row, parent, false);
        return new StudentResultEntryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentResultEntryAdapter.ViewHolder holder, int position) {
        holder.textViewStudentNumber.setText(arrayList.get(position));
        holder.textViewTotalMarks.setText(finalMarks.get(String.valueOf(position+1)));



//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                studentFinalAnswers = saveFinalAnswers.get(String.valueOf(arrayList.get(holder.getAdapterPosition())));
//                Intent intent = new Intent(holder.relativeLayout.getContext(),StudentResultActivity.class);
//                intent.putExtra("testInfo", (HashMap<String,String>) testInfo);
//                intent.putExtra("rollNo",arrayList.get(holder.getAdapterPosition()));
//                intent.putExtra("studentFinalAnswers",(HashMap<String, String>) studentFinalAnswers);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                holder.relativeLayout.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return numberOfStudents;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewStudentNumber;
        private TextView textViewTotalMarks;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentNumber = (TextView) itemView.findViewById(R.id.textView_roll_number);
            textViewTotalMarks = (TextView) itemView.findViewById(R.id.textView_total_marks);
            relativeLayout = itemView.findViewById(R.id.student_result_entry);
        }
    }
}
