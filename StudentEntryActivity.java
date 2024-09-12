package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentEntryActivity extends AppCompatActivity {

    TextView header;
    String subjectName,examName,totalStudents, standard, division;
    RecyclerView recyclerView;
    private ArrayList<String> arrayList = new ArrayList<>();
    Button evaluateMarks;
    HashMap<String, String> testInfo,correctAnswers,studentAnswers,studentMarks;
    HashMap<String, String> finalMarks = new HashMap<>();
    HashMap<String,HashMap<String, String>> saveAnswers = new HashMap<>();
    HashMap<String,HashMap<String, String>> saveFinalAnswers = new HashMap<>();
    HashMap<String, String> studentFinalAnswers = new HashMap<>();
    ArrayList<String> selectedStudents;
    String id;
    int flag=0;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_entry);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        header = findViewById(R.id.textView_student_entry_head);
        recyclerView = findViewById(R.id.student_entry_recycler);
        evaluateMarks = findViewById(R.id.button_evaluate);

        Intent intent = getIntent();
        testInfo = (HashMap<String,String>) intent.getSerializableExtra("TestInfo");
        correctAnswers = (HashMap<String,String>) intent.getSerializableExtra("answers");
        studentAnswers = (HashMap<String,String>) intent.getSerializableExtra("studentAnswers");
        studentMarks = (HashMap<String,String>) intent.getSerializableExtra("studentMarks");

        flag = intent.getIntExtra("FLAG",0);

        selectedStudents = (ArrayList<String>) intent.getSerializableExtra("selectedStudents");

        //Toast.makeText(StudentEntryActivity.this,String.valueOf(flag),Toast.LENGTH_SHORT).show();
        if(flag == 1){


            finalMarks.putAll(studentMarks);

            id = intent.getStringExtra("ID");
            saveAnswers = (HashMap<String,HashMap<String, String>>) intent.getSerializableExtra("saveAnswers");
            saveFinalAnswers.putAll(saveAnswers);
            selectedStudents.add(id);
        }

        subjectName = testInfo.get("subject");
        examName = testInfo.get("exam name");
        standard = testInfo.get("standard");
        division = testInfo.get("division");
        totalStudents = testInfo.get("students");

        header.setText(subjectName + " " + examName + " " + standard + " " + division);

        for(int i=0; i<Integer.parseInt(totalStudents);i++){
            arrayList.add(String.valueOf(i+1));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StudentEntryAdapter studentEntryAdapter = new StudentEntryAdapter(arrayList,testInfo,correctAnswers,studentAnswers, studentMarks, saveAnswers, evaluateMarks, selectedStudents,this,Integer.parseInt(totalStudents));
        recyclerView.setAdapter(studentEntryAdapter);


        evaluateMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalMarks.keySet().size() == Integer.parseInt(totalStudents)){
                    Intent intent = new Intent(StudentEntryActivity.this,StudentResultEntryActivity.class);
                    intent.putExtra("testInfo",(HashMap<String,String>) testInfo);
                    intent.putExtra("finalMarks",(HashMap<String,String>) finalMarks);
                    intent.putExtra("saveFinalAnswers",(HashMap<String,HashMap<String, String>>) saveFinalAnswers);
                    intent.putExtra("studentFinalAnswers",(HashMap<String, String>) studentFinalAnswers);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(StudentEntryActivity.this,"Finish entering answers of all students",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}