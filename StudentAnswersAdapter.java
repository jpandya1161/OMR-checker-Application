package com.msquare.omr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StudentAnswersAdapter extends RecyclerView.Adapter<StudentAnswersAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private int optionsPerQuestion, numberofQuestions;
    ArrayList<String> arrayList;
    ArrayList<String> selectedStudents;
    HashMap<String,String> correctAnswers;
    HashMap<String,String> studentAnswers;
    HashMap<String,String> testInfo ;
    private Button buttonSubmitAnswers;
    HashMap<String,String> studentMarks;
    HashMap<String,HashMap<String, String>> saveAnswers;
    int counter=0;
    String textStudentRollNumber;


    public StudentAnswersAdapter(ArrayList<String> arrayList, HashMap<String,String> testInfo, HashMap<String,String> correctAnswers, HashMap<String,String> studentAnswers,HashMap<String,String> studentMarks, HashMap<String,HashMap<String, String>> saveAnswers,String textStudentRollNumber,Button buttonSubmitAnswers, ArrayList<String> selectedStudents,Context context, int numberOfQuestions, int optionsPerQuestion) {
        this.arrayList = arrayList;
        this.testInfo = testInfo;
        this.correctAnswers = correctAnswers;
        this.studentAnswers = studentAnswers;
        this.studentMarks = studentMarks;
        this.saveAnswers = saveAnswers;
        this.textStudentRollNumber = textStudentRollNumber;
        this.buttonSubmitAnswers = buttonSubmitAnswers;
        this.selectedStudents = selectedStudents;
        this.inflater = LayoutInflater.from(context);
        this.numberofQuestions = numberOfQuestions;
        this.optionsPerQuestion = optionsPerQuestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(optionsPerQuestion == 2){
            view = inflater.inflate(R.layout.two_option_select_student_answer, parent, false);
        }
        else if(optionsPerQuestion == 3){
            view = inflater.inflate(R.layout.three_option_select_student_answer, parent, false);
        }
        else if(optionsPerQuestion == 4){
            view = inflater.inflate(R.layout.four_option_select_student_answer, parent, false);
        }
        else if(optionsPerQuestion == 5){
            view = inflater.inflate(R.layout.five_option_select_student_answer, parent, false);
        }
        assert view != null;
        return new StudentAnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewQuestionNumber.setText(arrayList.get(position));


        holder.selectStudentAnswersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedID = holder.selectStudentAnswersRadioGroup.getCheckedRadioButtonId();
                RadioButton selected = radioGroup. findViewById(selectedID);
                studentAnswers.put(arrayList.get(holder.getAdapterPosition()),selected.getText().toString());
                //Toast.makeText(holder.selectStudentAnswersRadioGroup.getContext(),arrayList.get(holder.getAdapterPosition())+ " "+selected.getText().toString(),Toast.LENGTH_SHORT).show();

                if(Objects.equals(studentAnswers.get(arrayList.get(holder.getAdapterPosition())), correctAnswers.get(arrayList.get(holder.getAdapterPosition())))){
                    counter = counter + Integer.parseInt(testInfo.get("marks per question"));
                }
                else{
                    counter = counter - Integer.parseInt(testInfo.get("negative marks per question"));
                }
                //Toast.makeText(holder.selectStudentAnswersRadioGroup.getContext(),String.valueOf(counter),Toast.LENGTH_SHORT).show();

                saveAnswers.put(arrayList.get(holder.getAdapterPosition()),studentAnswers);
            }
        });

        saveAnswers.put(arrayList.get(position),studentAnswers);

        buttonSubmitAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(studentAnswers.keySet().size() == numberofQuestions){
                    studentMarks.put(textStudentRollNumber,String.valueOf(counter));
                    Toast.makeText(buttonSubmitAnswers.getContext(),textStudentRollNumber+" "+String.valueOf(counter),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(buttonSubmitAnswers.getContext(),StudentEntryActivity.class);
                    intent.putExtra("studentAnswers", (HashMap<String,String>) studentAnswers);
                    intent.putExtra("answers", (HashMap<String,String>) correctAnswers);
                    intent.putExtra("TestInfo",(HashMap<String,String>) testInfo);
                    intent.putExtra("studentMarks",(HashMap<String,String>) studentMarks);
                    intent.putExtra("selectedStudents", (ArrayList<String>) selectedStudents);
                    intent.putExtra("saveAnswers",(HashMap<String,HashMap<String, String>>) saveAnswers);
                    intent.putExtra("ID",textStudentRollNumber);
                    intent.putExtra("FLAG",1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    buttonSubmitAnswers.getContext().startActivity(intent);
                }
                else{
                    Toast.makeText(buttonSubmitAnswers.getContext(),"Enter all answers for student "+textStudentRollNumber,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return numberofQuestions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutContainer;
        private TextView textViewQuestionNumber;
        RadioGroup selectStudentAnswersRadioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
            selectStudentAnswersRadioGroup = (RadioGroup) itemView.findViewById(R.id.selected_student_option_radio_group);
            textViewQuestionNumber = (TextView) itemView.findViewById(R.id.question_no);
        }
    }
}
