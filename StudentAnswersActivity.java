package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentAnswersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView studentRollNumber;
    String textStudentRollNumber;
    String textNumberOfQuestions, textNumberOfOptions;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Button buttonSubmitAnswers;
    private HashMap<String, String> testInfo;
    private HashMap<String, String> correctAnswers;
    private HashMap<String, String> studentAnswers;
    private HashMap<String, String> studentMarks;
    private HashMap<String,HashMap<String, String>> saveAnswers;
    private AdView adView;
    ArrayList<String> selectedStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_answers);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        studentRollNumber = findViewById(R.id.textView_student_answers_head);
        recyclerView = findViewById(R.id.student_answer_recycler);
        buttonSubmitAnswers = findViewById(R.id.button_submit_answers);

        Intent intent = getIntent();
        testInfo = (HashMap<String,String>) intent.getSerializableExtra("TestInfo");
        correctAnswers = (HashMap<String,String>) intent.getSerializableExtra("answers");
        studentAnswers = (HashMap<String,String>) intent.getSerializableExtra("studentAnswers");
        studentMarks = (HashMap<String,String>) intent.getSerializableExtra("studentMarks");
        saveAnswers = (HashMap<String,HashMap<String, String>>) intent.getSerializableExtra("saveAnswers");
        selectedStudents = (ArrayList<String>) intent.getSerializableExtra("selectedStudents");


        textStudentRollNumber = intent.getStringExtra("roll no");
        studentRollNumber.setText("Roll Number "+textStudentRollNumber);

        textNumberOfOptions = testInfo.get("options");
        textNumberOfQuestions = testInfo.get("questions");

        for(int i=0; i<Integer.parseInt(textNumberOfQuestions);i++){
            arrayList.add(String.valueOf(i+1));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StudentAnswersAdapter studentAnswersAdapter = new StudentAnswersAdapter(arrayList,testInfo,correctAnswers,studentAnswers, studentMarks, saveAnswers,textStudentRollNumber,buttonSubmitAnswers, selectedStudents,this,Integer.parseInt(textNumberOfQuestions),Integer.parseInt(textNumberOfOptions));
        recyclerView.setAdapter(studentAnswersAdapter);


    }
}