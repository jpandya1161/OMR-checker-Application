package com.msquare.omr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StudentResultAdapter extends RecyclerView.Adapter<StudentResultAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private int optionsPerQuestion, numberofQuestions;
    ArrayList<String> arrayList;
    HashMap<String,String> testInfo ;
    String textStudentRollNumber;
    HashMap<String, String> studentFinalAnswers;



    public StudentResultAdapter(ArrayList<String> arrayList, HashMap<String,String> testInfo, HashMap<String, String> studentFinalAnswers ,String textStudentRollNumber, Context context, int numberOfQuestions, int optionsPerQuestion) {
        this.arrayList = arrayList;
        this.testInfo = testInfo;
        this.studentFinalAnswers = studentFinalAnswers;
        this.textStudentRollNumber = textStudentRollNumber;
        this.inflater = LayoutInflater.from(context);
        this.numberofQuestions = numberOfQuestions;
        this.optionsPerQuestion = optionsPerQuestion;
    }


    @NonNull
    @Override
    public StudentResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(optionsPerQuestion == 2){
            view = inflater.inflate(R.layout.two_option_show_student_answer, parent, false);
        }
        else if(optionsPerQuestion == 3){
            view = inflater.inflate(R.layout.three_option_show_student_answer, parent, false);
        }
        else if(optionsPerQuestion == 4){
            view = inflater.inflate(R.layout.four_option_show_student_answer, parent, false);
        }
        else if(optionsPerQuestion == 5){
            view = inflater.inflate(R.layout.five_option_show_student_answer, parent, false);
        }
        assert view != null;
        return new StudentResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentResultAdapter.ViewHolder holder, int position) {

        holder.textViewQuestionNumber.setText(arrayList.get(position));

        String optionID = studentFinalAnswers.get(String.valueOf(arrayList.get(position)));

        if(Objects.equals(optionID, "A")){
            holder.showStudentAnswersRadioGroup.check(R.id.option_A);
        }
        else if(Objects.equals(optionID, "B")){
            holder.showStudentAnswersRadioGroup.check(R.id.option_B);
        }
        else if(Objects.equals(optionID, "C")){
            holder.showStudentAnswersRadioGroup.check(R.id.option_C);
        }
        else if(Objects.equals(optionID, "D")){
            holder.showStudentAnswersRadioGroup.check(R.id.option_D);
        }

    }

    @Override
    public int getItemCount() {
        return numberofQuestions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayoutContainer;
        private TextView textViewQuestionNumber;
        RadioGroup showStudentAnswersRadioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
            showStudentAnswersRadioGroup = (RadioGroup) itemView.findViewById(R.id.show_student_option_radio_group);
            textViewQuestionNumber = (TextView) itemView.findViewById(R.id.question_no);
        }
    }
}
