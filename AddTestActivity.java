package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTestActivity extends AppCompatActivity {

    private EditText editTextStandard, editTextDivision, editTextSubject, editTextExamName, editTextTotalStudents, editTextTotalQuestions, editTextMarksPerQuestion, editTextNegativeMarksPerQuestion;
    private RadioGroup radioGroupOptionsPerQuestion;
    private RadioButton radioButtonOptionsPerQuestionSelected;
    private Button buttonSelectAnswers;
    private ProgressBar progressBar;
    private FirebaseFirestore testInfoDB;
    private FirebaseAuth authAddTest;
    private FirebaseUser userAddTest;
    private AdView adView;
    private InterstitialAd mInterstitialAd = null;
    public static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        getSupportActionBar().setTitle("Test Details");

        editTextStandard = findViewById(R.id.editText_standard);
        editTextDivision = findViewById(R.id.editText_division);
        editTextSubject = findViewById(R.id.editText_subject);
        editTextExamName = findViewById(R.id.editText_exam_name);
        editTextTotalStudents = findViewById(R.id.editText_total_students);
        editTextTotalQuestions = findViewById(R.id.editText_total_questions);
        editTextMarksPerQuestion = findViewById(R.id.editText_marks_per_question);
        editTextNegativeMarksPerQuestion = findViewById(R.id.editText_negative_marks);
        progressBar = findViewById(R.id.progressBar);

        radioGroupOptionsPerQuestion = findViewById(R.id.radio_group_number_of_options);
        radioGroupOptionsPerQuestion.clearCheck();

        testInfoDB = FirebaseFirestore.getInstance();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        loadInterstitialAds();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        buttonSelectAnswers = findViewById(R.id.button_select_answers);
        buttonSelectAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedNumberOfOptions = radioGroupOptionsPerQuestion.getCheckedRadioButtonId();
                radioButtonOptionsPerQuestionSelected = findViewById(selectedNumberOfOptions);

                String textStandard = editTextStandard.getText().toString();
                String textDivision = editTextDivision.getText().toString();
                String textSubject = editTextSubject.getText().toString();
                String textExamName = editTextExamName.getText().toString();
                String textTotalStudents = editTextTotalStudents.getText().toString();
                String textTotalQuestions = editTextTotalQuestions.getText().toString();
                String textMarksPerQuestion = editTextMarksPerQuestion.getText().toString();
                String textNegativeMarksPerQuestion = editTextNegativeMarksPerQuestion.getText().toString();
                String textOptionsPerQuestion;

                if(TextUtils.isEmpty(textStandard)){
                    Toast.makeText(AddTestActivity.this, "Enter Standard",Toast.LENGTH_SHORT).show();
                    editTextStandard.setError("Standard Required");
                    editTextStandard.requestFocus();
                }
                else if(TextUtils.isEmpty(textDivision)){
                    Toast.makeText(AddTestActivity.this, "Enter Division",Toast.LENGTH_SHORT).show();
                    editTextDivision.setError("Division Required");
                    editTextDivision.requestFocus();
                }
                else if(TextUtils.isEmpty(textSubject)){
                    Toast.makeText(AddTestActivity.this, "Enter Subject",Toast.LENGTH_SHORT).show();
                    editTextSubject.setError("Subject Required");
                    editTextSubject.requestFocus();
                }
                else if(TextUtils.isEmpty(textExamName)){
                    Toast.makeText(AddTestActivity.this, "Enter Exam Name",Toast.LENGTH_SHORT).show();
                    editTextExamName.setError("Exam Name Required");
                    editTextExamName.requestFocus();
                }
                else if(TextUtils.isEmpty(textTotalStudents)){
                    Toast.makeText(AddTestActivity.this, "Enter Total Students",Toast.LENGTH_SHORT).show();
                    editTextTotalStudents.setError("Total Students Required");
                    editTextTotalStudents.requestFocus();
                }
                else if(TextUtils.isEmpty(textTotalQuestions)){
                    Toast.makeText(AddTestActivity.this, "Enter Total Questions",Toast.LENGTH_SHORT).show();
                    editTextTotalQuestions.setError("Total Questions Required");
                    editTextTotalQuestions.requestFocus();
                }
                else if(TextUtils.isEmpty(textMarksPerQuestion)){
                    Toast.makeText(AddTestActivity.this, "Enter Marks per question",Toast.LENGTH_SHORT).show();
                    editTextMarksPerQuestion.setError("Marks per question Required");
                    editTextMarksPerQuestion.requestFocus();
                }
                else if(TextUtils.isEmpty(textNegativeMarksPerQuestion)){
                    textNegativeMarksPerQuestion = "0";
                }
                else if(Integer.valueOf(textNegativeMarksPerQuestion) < 0 || (Integer.valueOf(textNegativeMarksPerQuestion)>Integer.valueOf(textMarksPerQuestion))){
                    Toast.makeText(AddTestActivity.this, "Enter valid Negative marks",Toast.LENGTH_SHORT).show();
                    editTextNegativeMarksPerQuestion.setError("Valid Negative marks Required");
                    editTextNegativeMarksPerQuestion.requestFocus();
                }

                else if(radioGroupOptionsPerQuestion.getCheckedRadioButtonId() == -1){
                    Toast.makeText(AddTestActivity.this, "Please select Number of Options",Toast.LENGTH_SHORT).show();
                    radioButtonOptionsPerQuestionSelected.setError("Options Per Question Required");
                    radioButtonOptionsPerQuestionSelected.requestFocus();
                }
                else{
                    textOptionsPerQuestion = radioButtonOptionsPerQuestionSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    insertTestData(textStandard,textDivision,textSubject,textExamName,textTotalStudents,textTotalQuestions,textMarksPerQuestion,textNegativeMarksPerQuestion,textOptionsPerQuestion);
                }

            }
        });

    }

    private void loadInterstitialAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                if(mInterstitialAd != null){
                    mInterstitialAd.show(AddTestActivity.this);
                }
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mInterstitialAd = null;
                        //loadInterstitialAds();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Toast.makeText(AddTestActivity.this,"Ad not Loaded",Toast.LENGTH_SHORT).show();
                        mInterstitialAd.show(AddTestActivity.this);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });
            }
        });

    }

    private void insertTestData(String textStandard, String textDivision, String textSubject, String textExamName, String textTotalStudents, String textTotalQuestions, String textMarksPerQuestion, String textNegativeMarksPerQuestion, String textOptionsPerQuestion) {

        authAddTest = FirebaseAuth.getInstance();
        userAddTest = authAddTest.getCurrentUser();

        Map<String,String> testInfo = new HashMap<>();

        testInfo.put("testCreatorID",userAddTest.getUid());
        testInfo.put("standard",textStandard);
        testInfo.put("division",textDivision);
        testInfo.put("subject",textSubject);
        testInfo.put("exam name",textExamName);
        testInfo.put("students",textTotalStudents);
        testInfo.put("questions",textTotalQuestions);
        testInfo.put("marks per question",textMarksPerQuestion);
        testInfo.put("negative marks per question",textNegativeMarksPerQuestion);
        testInfo.put("options",textOptionsPerQuestion);

        testInfoDB.collection("Tests").add(testInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddTestActivity.this,"Test Details Added Successfully",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddTestActivity.this,SelectAnswersActivity.class);
                intent.putExtra("questions",textTotalQuestions);
                intent.putExtra("options",textOptionsPerQuestion);
                intent.putExtra("TestInfo", (HashMap<String,String>) testInfo);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

}