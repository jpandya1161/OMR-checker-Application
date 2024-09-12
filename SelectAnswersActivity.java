package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectAnswersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String textNumberOfQuestions, textNumberOfOptions;
    SelectAnswersAdapter adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> selectedStudents = new ArrayList<>();
    private Button buttonStudentEntry;
    private HashMap<String, String> testInfo;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_answers);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        recyclerView = findViewById(R.id.select_answer_recycler);
        buttonStudentEntry = findViewById(R.id.button_student_entry);

        Intent intent = getIntent();
        textNumberOfOptions = intent.getStringExtra("options");
        textNumberOfQuestions = intent.getStringExtra("questions");
        testInfo = (HashMap<String,String>) intent.getSerializableExtra("TestInfo");


        for(int i=0; i<Integer.parseInt(textNumberOfQuestions);i++){
            arrayList.add(String.valueOf(i+1));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SelectAnswersAdapter selectAnswersAdapter = new SelectAnswersAdapter(arrayList,testInfo,buttonStudentEntry, selectedStudents,this,Integer.parseInt(textNumberOfQuestions),Integer.parseInt(textNumberOfOptions));
        recyclerView.setAdapter(selectAnswersAdapter);

    }
}