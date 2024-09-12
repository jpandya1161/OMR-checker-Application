package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDOB,
            editTextRegisterMobile, editTextRegisterSchoolName, editTextRegisterPwd, editTextRegisterConfirmPwd;


    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private Button buttonRegister;
    private int Flag;
    private static final String TAG = "RegisteredActivity";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "Register Now", Toast.LENGTH_SHORT).show();

        progressBar = findViewById(R.id.progressbar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDOB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterSchoolName = findViewById(R.id.editText_register_school_name);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);

        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Setting up DatePicker
        editTextRegisterDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setText("Verify Details ");
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDOB = editTextRegisterDOB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textSchoolName = editTextRegisterSchoolName.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;

                //Validate Mobile Number using Matcher and Pattern (Regular Expression)
                String mobileRegex = "[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);

                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this, "Enter Full Name",Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full Name Required");
                    editTextRegisterFullName.requestFocus();
                }
                else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Enter Email",Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email Required");
                    editTextRegisterEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Re-enter EMail",Toast.LENGTH_SHORT).show();
                    editTextRegisterDOB.setError("Proper email Required");
                    editTextRegisterDOB.requestFocus();
                }
                else if(TextUtils.isEmpty(textDOB)){
                    Toast.makeText(RegisterActivity.this, "Enter DOB",Toast.LENGTH_SHORT).show();
                    editTextRegisterDOB.setError("DOB Required");
                    editTextRegisterDOB.requestFocus();
                }
                else if(radioGroupRegisterGender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this, "Please select Gender",Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelected.setError("Gender is Required");
                    radioButtonRegisterGenderSelected.requestFocus();

                }
                else if(TextUtils.isEmpty(textMobile)){
                    Toast.makeText(RegisterActivity.this, "Enter Mobile no.",Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if(textMobile.length() != 10){
                    Toast.makeText(RegisterActivity.this, "Re-enter Mobile no.",Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                }
                else if(!mobileMatcher.find()){
                    Toast.makeText(RegisterActivity.this, "Re-enter Mobile no.",Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. is invalid");
                    editTextRegisterMobile.requestFocus();
                }
                else if(TextUtils.isEmpty(textSchoolName)){
                    Toast.makeText(RegisterActivity.this, "Enter School Name",Toast.LENGTH_SHORT).show();
                    editTextRegisterSchoolName.setError("School Name Required");
                    editTextRegisterSchoolName.requestFocus();
                }
                else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(RegisterActivity.this, "Enter Password",Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password Required");
                    editTextRegisterPwd.requestFocus();
                }
                else if(textPwd.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Re-enter Password",Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password should be at least 6 digits");
                    editTextRegisterPwd.requestFocus();
                }
                else if(TextUtils.isEmpty(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this, "Enter Confirm Password",Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation Required");
                    editTextRegisterConfirmPwd.requestFocus();
                }
                else if(!textPwd.equals(textConfirmPwd)){
                    Toast.makeText(RegisterActivity.this, "Passwords not same",Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Passwords must be same");
                    editTextRegisterConfirmPwd.requestFocus();
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }
                else{
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(RegisterActivity.this,OtpSendActivity.class);
                    ArrayList<String> registerUserInfo = new ArrayList<>();
                    registerUserInfo.add(0,textFullName);
                    registerUserInfo.add(1,textEmail);
                    registerUserInfo.add(2,textDOB);
                    registerUserInfo.add(3,textGender);
                    registerUserInfo.add(4,textMobile);
                    registerUserInfo.add(5,textSchoolName);
                    registerUserInfo.add(6,textPwd);

                    intent.putExtra("UserInfo",registerUserInfo);

                    verifyUser(textEmail,textPwd, textMobile);
                    if(Flag == 1){
                        startActivity(intent);
                    }
                }

            }
        });

    }

    private void verifyUser(String textEmail,String textPwd, String textMobile) {

        auth = FirebaseAuth.getInstance();

        //create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query mobileQuery = ref.child("Registered Users").orderByChild("mobile").equalTo(textMobile);

                    mobileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String ref= null;
                            for (DataSnapshot mobileSnapshot: dataSnapshot.getChildren()) {
                                ref = mobileSnapshot.getRef().toString();
                            }
                            if(ref == null){
                                Flag = 1;
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this,"Details Verified",Toast.LENGTH_SHORT).show();
                                buttonRegister.setText("Register");
                                return;
                            }
                            else{
                                editTextRegisterMobile.setError("User is already registered with this number. Use another number.");
                                editTextRegisterMobile.requestFocus();
                                Flag = 0;
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });


                    auth.getCurrentUser().delete();

                }
                else{
                    Flag = 0;
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        editTextRegisterEmail.setError("User is already registered with this email. Use another email.");
                        editTextRegisterEmail.requestFocus();
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}