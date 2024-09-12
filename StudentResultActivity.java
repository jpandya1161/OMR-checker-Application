package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView studentRollNumber;
    String textStudentRollNumber;
    String textNumberOfQuestions, textNumberOfOptions;
    private ArrayList<String> arrayList = new ArrayList<>();
    private HashMap<String, String> testInfo;
    HashMap<String, String> studentFinalAnswers;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        studentRollNumber = findViewById(R.id.textView_student_result_head);
        recyclerView = findViewById(R.id.student_answer_recycler);

        Intent intent = getIntent();
        testInfo = (HashMap<String,String>) intent.getSerializableExtra("testInfo");
        textStudentRollNumber = intent.getStringExtra("rollNo");
        studentFinalAnswers = (HashMap<String, String>) intent.getSerializableExtra("studentFinalAnswers");



        studentRollNumber.setText("Roll Number "+textStudentRollNumber);

        textNumberOfOptions = testInfo.get("options");
        textNumberOfQuestions = testInfo.get("questions");

        for(int i=0; i<Integer.parseInt(textNumberOfQuestions);i++){
            arrayList.add(String.valueOf(i+1));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StudentResultAdapter studentResultAdapter = new StudentResultAdapter(arrayList,testInfo, studentFinalAnswers,textStudentRollNumber,this,Integer.parseInt(textNumberOfQuestions),Integer.parseInt(textNumberOfOptions));
        recyclerView.setAdapter(studentResultAdapter);
    }

}