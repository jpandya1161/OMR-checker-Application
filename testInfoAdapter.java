package com.msquare.omr;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class testInfoAdapter extends RecyclerView.Adapter<testInfoAdapter.myviewholder>{

    ArrayList<testInfoModel> testInfoList;

    public testInfoAdapter(ArrayList<testInfoModel> testInfoList) {
        this.testInfoList = testInfoList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testinformationrow, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        holder.subjectName.setText(testInfoList.get(position).getSubject());
        holder.examName.setText(testInfoList.get(position).getExamName());
        holder.standard.setText(testInfoList.get(position).getStandard());
        holder.division.setText(testInfoList.get(position).getDivision());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = testInfoList.get(holder.getAdapterPosition()).getSubject() +"_"+ testInfoList.get(holder.getAdapterPosition()).getExamName() +"_"+ testInfoList.get(holder.getAdapterPosition()).getStandard() +"_"+ testInfoList.get(holder.getAdapterPosition()).getDivision() +"_StudentMarks";
                //Toast.makeText(holder.relativeLayout.getContext(),testInfoList.get(holder.getAdapterPosition()).getSubject() +"_"+ testInfoList.get(holder.getAdapterPosition()).getExamName() +"_"+ testInfoList.get(holder.getAdapterPosition()).getStandard() +"_"+ testInfoList.get(holder.getAdapterPosition()).getDivision() +"_StudentMarks", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.relativeLayout.getContext(), ViewTestResultPDF.class);
                intent.putExtra("fileName", fileName);
                intent.putExtra("standard", testInfoList.get(holder.getAdapterPosition()).getStandard());
                intent.putExtra("division",testInfoList.get(holder.getAdapterPosition()).getDivision());
                intent.putExtra("subject", testInfoList.get(holder.getAdapterPosition()).getSubject());
                intent.putExtra("exam name", testInfoList.get(holder.getAdapterPosition()).getExamName());
                intent.putExtra("students", testInfoList.get(holder.getAdapterPosition()).getTotalStudents());
                intent.putExtra("questions",testInfoList.get(holder.getAdapterPosition()).getTotalQuestions());
                intent.putExtra("marks per question", testInfoList.get(holder.getAdapterPosition()).getMarksPerQuestion());
                intent.putExtra("negative marks per question",testInfoList.get(holder.getAdapterPosition()).getNegativeMarks());
                intent.putExtra("options",testInfoList.get(holder.getAdapterPosition()).getOptions());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.relativeLayout.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return testInfoList.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{

        TextView examName,subjectName, standard, division;
        RelativeLayout relativeLayout;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.StudentInfoEntry);
            subjectName = itemView.findViewById(R.id.subjectName);
            examName = itemView.findViewById(R.id.examName);
            standard = itemView.findViewById(R.id.standard);
            division = itemView.findViewById(R.id.division);
        }
    }

}
