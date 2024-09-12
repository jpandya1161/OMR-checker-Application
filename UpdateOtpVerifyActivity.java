package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.msquare.omr.databinding.ActivityOtpVerifyBinding;
import com.msquare.omr.databinding.ActivityUpdateOtpSendBinding;
import com.msquare.omr.databinding.ActivityUpdateOtpVerifyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UpdateOtpVerifyActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private ActivityUpdateOtpVerifyBinding binding;
    private String verificationId;
    private static final String TAG = "OtpVerifyActivity";
    private String textFullName, textEmail, textDOB,textGender,textMobile, textSchoolName, userID;
    private ArrayList<String> registerUserInfo = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateOtpVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Intent i = getIntent();
        registerUserInfo = i.getStringArrayListExtra("UserInfo");
        textFullName = registerUserInfo.get(0);
        textEmail = registerUserInfo.get(1);
        textDOB = registerUserInfo.get(2);
        textGender = registerUserInfo.get(3);
        textMobile = registerUserInfo.get(4);
        textSchoolName = registerUserInfo.get(5);
        userID = registerUserInfo.get(6);



        editTextInput();

        binding.tvMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("phone")
        ));

        verificationId = getIntent().getStringExtra("verificationId");

        binding.tvResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateOtpVerifyActivity.this, "OTP Send Successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBarVerify.setVisibility(View.VISIBLE);
                binding.btnVerify.setVisibility(View.INVISIBLE);
                if (binding.etC1.getText().toString().trim().isEmpty() ||
                        binding.etC2.getText().toString().trim().isEmpty() ||
                        binding.etC3.getText().toString().trim().isEmpty() ||
                        binding.etC4.getText().toString().trim().isEmpty() ||
                        binding.etC5.getText().toString().trim().isEmpty() ||
                        binding.etC6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(UpdateOtpVerifyActivity.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (verificationId != null) {
                        String code = binding.etC1.getText().toString().trim() +
                                binding.etC2.getText().toString().trim() +
                                binding.etC3.getText().toString().trim() +
                                binding.etC4.getText().toString().trim() +
                                binding.etC5.getText().toString().trim() +
                                binding.etC6.getText().toString().trim();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        FirebaseAuth
                                .getInstance()
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            binding.progressBarVerify.setVisibility(View.VISIBLE);
                                            binding.btnVerify.setVisibility(View.INVISIBLE);
                                            Toast.makeText(UpdateOtpVerifyActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textEmail, textDOB, textGender, textMobile, textSchoolName);
                                            DatabaseReference referenceUpdatedProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                                            binding.progressBarVerify.setVisibility(View.GONE);
                                            referenceUpdatedProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        //UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                                                        //firebaseUser.updateProfile(profileUpdates);
                                                        Toast.makeText(UpdateOtpVerifyActivity.this,"Update Successful",Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(UpdateOtpVerifyActivity.this, LoginActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else{
                                                        try{
                                                            throw task.getException();
                                                        }
                                                        catch (Exception e){
                                                            Toast.makeText(UpdateOtpVerifyActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    binding.progressBarVerify.setVisibility(View.GONE);
                                                }
                                            });
                                        } else {
                                            binding.progressBarVerify.setVisibility(View.GONE);
                                            binding.btnVerify.setVisibility(View.VISIBLE);
                                            Toast.makeText(UpdateOtpVerifyActivity.this, "OTP is not Valid!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void editTextInput() {
        binding.etC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etC5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etC6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


//    private void updateUser(String textFullName, String textEmail, String textDOB, String textGender,String textMobile, String textSchoolName, String textPwd) {
//
//        auth = FirebaseAuth.getInstance();
//
//        //create user profile
//        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(UpdateOtpVerifyActivity.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(UpdateOtpVerifyActivity.this,"User Registration", Toast.LENGTH_SHORT).show();
//                    firebaseUser = auth.getCurrentUser();
//
//                    //Update Display Name of User
//                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
//                    firebaseUser.updateProfile(profileChangeRequest);
//
//                    //Enter user data into firebase realtime database
//                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textEmail, textDOB, textGender, textMobile, textSchoolName);
//
//                    //Extracting user reference from database for "Registered Users"
//                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
//
//
//
//                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if(task.isSuccessful()){
//                                //Send verification email
//                                firebaseUser.sendEmailVerification();
//                                Toast.makeText(UpdateOtpVerifyActivity.this,"User Registration Successful. Please Login.", Toast.LENGTH_SHORT).show();
//
//                                //open user profile after successful registration
//                                Intent intent = new Intent(UpdateOtpVerifyActivity.this, LoginActivity.class);
//
//                                //prevents user from returning back to register activity
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                //close Register Activity
//                                finish();
//                            }
//                            else{
//                                Toast.makeText(UpdateOtpVerifyActivity.this,"User Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
//                            }
//                            binding.progressBarVerify.setVisibility(View.GONE);
//                        }
//                    });
//
//
//                }
//                else{
//                    //referenceProfileNumber.child(firebaseUser.getUid()).removeValue();
//                    auth.getCurrentUser().delete();
//                    try{
//                        throw task.getException();
//                    }
//                    catch (Exception e){
//                        Log.e(TAG, e.getMessage());
//                        Toast.makeText(UpdateOtpVerifyActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                    binding.progressBarVerify.setVisibility(View.GONE);
//                }
//
//            }
//        });
//    }


    @Override
    public void onBackPressed() {

        if(auth.getCurrentUser() != null){
            auth.getCurrentUser().delete();
        }


        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to Exit", Toast.LENGTH_SHORT);
            backToast.show();
        }


        backPressedTime = System.currentTimeMillis();
    }
}