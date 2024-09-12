package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
//import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.util.Objects;

public class ViewTestResultPDF extends AppCompatActivity {

    String fileName;
    private AdView adView;
    Button openPDF, openEXCEL;
    EditText standard, division, subject, examName, totalStudents, totalQuestions, marksPerQuestion, negativeMarks;
    RadioGroup optionsPerQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test_result_pdf);

        standard = findViewById(R.id.editText_standard);
        standard.setKeyListener(null);
        division = findViewById(R.id.editText_division);
        division.setKeyListener(null);
        subject = findViewById(R.id.editText_subject);
        subject.setKeyListener(null);
        examName = findViewById(R.id.editText_exam_name);
        examName.setKeyListener(null);
        totalStudents = findViewById(R.id.editText_total_students);
        totalStudents.setKeyListener(null);
        totalQuestions = findViewById(R.id.editText_total_questions);
        totalQuestions.setKeyListener(null);
        marksPerQuestion = findViewById(R.id.editText_marks_per_question);
        marksPerQuestion.setKeyListener(null);
        negativeMarks = findViewById(R.id.editText_negative_marks);
        negativeMarks.setKeyListener(null);
        optionsPerQuestion = findViewById(R.id.radio_group_number_of_options);

        openPDF = findViewById(R.id.button_open_PDF);
        openEXCEL = findViewById(R.id.button_open_EXCEL);

        getSupportActionBar().setTitle("Test Results");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        Intent intent = getIntent();
        fileName = intent.getStringExtra("fileName");

        standard.setText(intent.getStringExtra("standard"));
        division.setText(intent.getStringExtra("division"));
        subject.setText(intent.getStringExtra("subject"));
        examName.setText(intent.getStringExtra("exam name"));
        totalStudents.setText(intent.getStringExtra("students"));
        totalQuestions.setText(intent.getStringExtra("questions"));
        marksPerQuestion.setText(intent.getStringExtra("marks per question"));
        negativeMarks.setText(intent.getStringExtra("negative marks per question"));

        String ID = intent.getStringExtra("options");

        if(Objects.equals(ID, "2")){
            optionsPerQuestion.check(R.id.radio_2);
        }
        else if(Objects.equals(ID, "3")){
            optionsPerQuestion.check(R.id.radio_3);
        }
        else if(Objects.equals(ID, "4")){
            optionsPerQuestion.check(R.id.radio_4);
        }
        else if(Objects.equals(ID, "5")){
            optionsPerQuestion.check(R.id.radio_5);
        }


        String path = getExternalFilesDir(null).getAbsolutePath()+"/";

        openPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(path,fileName+".pdf");
                String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = FileProvider.getUriForFile(ViewTestResultPDF.this,ViewTestResultPDF.this.getApplicationContext().getPackageName() + ".provider", file);

                try{
                    i.setDataAndType(uri, mimeType);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(i, "Choose File"));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("ViewTestResultPDF", e.getMessage());
                }
            }
        });

        openEXCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(path,fileName+".xls");
                String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = FileProvider.getUriForFile(ViewTestResultPDF.this,ViewTestResultPDF.this.getApplicationContext().getPackageName() + ".provider", file);

                try{
                    i.setDataAndType(uri, mimeType);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(i, "Choose File"));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("ViewTestResultPDF", e.getMessage());
                }
            }
        });

    }
}