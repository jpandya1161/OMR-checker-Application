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

public class SelectAnswersAdapter extends RecyclerView.Adapter<SelectAnswersAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private int optionsPerQuestion, numberofQuestions;
    ArrayList<String> arrayList;
    HashMap<String,String> answers = new HashMap<>();
    HashMap<String,String> studentAnswers = new HashMap<>();
    HashMap<String,String> studentMarks = new HashMap<>();
    HashMap<String,String> testInfo ;
    ArrayList<String> selectedStudents;
    private Button buttonStudentEntry;

    public SelectAnswersAdapter(ArrayList<String> arrayList,HashMap<String,String> testInfo,Button buttonStudentEntry, ArrayList<String> selectedStudents,Context context, int numberOfQuestions, int optionsPerQuestion) {
        this.arrayList = arrayList;
        this.testInfo = testInfo;
        this.buttonStudentEntry = buttonStudentEntry;
        this.selectedStudents = selectedStudents;
        this.inflater = LayoutInflater.from(context);
        this.numberofQuestions = numberOfQuestions;
        this.optionsPerQuestion = optionsPerQuestion;
    }

    @NonNull
    @Override
    public SelectAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view = null;

        if(optionsPerQuestion == 2){
            view = inflater.inflate(R.layout.two_option_select_answer, parent, false);
        }
        else if(optionsPerQuestion == 3){
            view = inflater.inflate(R.layout.three_option_select_answer, parent, false);
        }
        else if(optionsPerQuestion == 4){
            view = inflater.inflate(R.layout.four_option_select_answer, parent, false);
        }
        else if(optionsPerQuestion == 5){
            view = inflater.inflate(R.layout.five_option_select_answer, parent, false);
        }
        assert view != null;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.textViewQuestionNumber.setText(arrayList.get(position));

        holder.selectAnswersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedID = holder.selectAnswersRadioGroup.getCheckedRadioButtonId();
                RadioButton selected = radioGroup. findViewById(selectedID);
                answers.put(arrayList.get(holder.getAdapterPosition()),selected.getText().toString());
                Toast.makeText(holder.selectAnswersRadioGroup.getContext(),arrayList.get(holder.getAdapterPosition())+ " "+selected.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        buttonStudentEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(answers.keySet().size() == numberofQuestions){
                    Intent intent = new Intent(buttonStudentEntry.getContext(),StudentEntryActivity.class);
                    intent.putExtra("answers", (HashMap<String,String>) answers);
                    intent.putExtra("studentAnswers", (HashMap<String,String>) studentAnswers);
                    intent.putExtra("TestInfo",(HashMap<String,String>) testInfo);
                    intent.putExtra("studentMarks", (HashMap<String,String>) studentMarks);
                    intent.putExtra("selectedStudents", (ArrayList<String>) selectedStudents);
                    intent.putExtra("FLAG",0);
                    buttonStudentEntry.getContext().startActivity(intent);
                }
                else{
                    Toast.makeText(holder.selectAnswersRadioGroup.getContext(),"Select all answers",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return numberofQuestions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout linearLayoutContainer;
        private TextView textViewQuestionNumber;
        RadioGroup selectAnswersRadioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutContainer = (LinearLayout) itemView.findViewById(R.id.linear_layout_container);
            selectAnswersRadioGroup = (RadioGroup) itemView.findViewById(R.id.selected_option_radio_group);
            textViewQuestionNumber = (TextView) itemView.findViewById(R.id.question_no);


        }

    }
}
