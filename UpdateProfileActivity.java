package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateName,  editTextUpdateDoB, editTextUpdateMobile, editTextUpdateSchoolName;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private String textFullName,textEmail ,textDob, textGender, textMobile, textSchoolName,testEmail="xyz@gmail.com",testPwd="xyzpqr",userID;
    private FirebaseAuth authUpdateProfile;
    private ProgressBar progressBar;
    private int Flag;
    private static final String TAG = "UpdateProfileActivity";
    private Button buttonUpdateProfile;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Update Profile Details");

        progressBar = findViewById(R.id.progressBar);
        editTextUpdateName = findViewById(R.id.editText_update_profile_name);
        editTextUpdateDoB = findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile = findViewById(R.id.editText_update_profile_mobile);
        editTextUpdateSchoolName =  findViewById(R.id.editText_update_profile_school_name);
        buttonUpdateProfile = findViewById(R.id.button_update_profile);

        radioGroupUpdateGender = findViewById(R.id.radio_group_update_gender);

        authUpdateProfile = FirebaseAuth.getInstance();
        firebaseUser = authUpdateProfile.getCurrentUser();
        userID = firebaseUser.getUid();


        //SHow Profile Data
        showProfile(firebaseUser);

        //Upload Profile Pic
        Button buttonUploadUpdatedProfilePic = findViewById(R.id.button_upload_profile_pic);
        buttonUploadUpdatedProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Update Email
        Button buttonUpdateEmail = findViewById(R.id.button_update_email);
        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Setting up DatePicker
        editTextUpdateDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Extracting saved dd,mm,yyyy into different variables by creating an array
                String textSADoB[] = textDob.split("/");

                int day = Integer.parseInt(textSADoB[0]);
                int month = Integer.parseInt(textSADoB[1])-1;
                int year = Integer.parseInt(textSADoB[2]);

                DatePickerDialog picker;

                //Date Picker
                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextUpdateDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        //Update Profile
        Button buttonUpdateProfile = findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser,textMobile);
            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser, String textMobile) {

        int selectedGenderID = radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected = findViewById(selectedGenderID);

        //Validate Mobile Number using Matcher and Pattern (Regular Expression)
        String mobileRegex = "[6-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(textMobile);

        if(TextUtils.isEmpty(textFullName)){
            Toast.makeText(UpdateProfileActivity.this, "Enter Full Name",Toast.LENGTH_SHORT).show();
            editTextUpdateName.setError("Full Name Required");
            editTextUpdateName.requestFocus();
        }
        else if(TextUtils.isEmpty(textDob)){
            Toast.makeText(UpdateProfileActivity.this, "Enter DOB",Toast.LENGTH_SHORT).show();
            editTextUpdateDoB.setError("DOB Required");
            editTextUpdateDoB.requestFocus();
        }
        else if(TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())){
            Toast.makeText(UpdateProfileActivity.this, "Please select Gender",Toast.LENGTH_SHORT).show();
            radioButtonUpdateGenderSelected.setError("Gender is Required");
            radioButtonUpdateGenderSelected.requestFocus();

        }
        else if(TextUtils.isEmpty(textMobile)){
            Toast.makeText(UpdateProfileActivity.this, "Enter Mobile no.",Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile no. Required");
            editTextUpdateMobile.requestFocus();
        }
        else if(textMobile.length() != 10){
            Toast.makeText(UpdateProfileActivity.this, "Re-enter Mobile no.",Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile no. should be 10 digits");
            editTextUpdateMobile.requestFocus();
        }
        else if(!mobileMatcher.find()){
            Toast.makeText(UpdateProfileActivity.this, "Re-enter Mobile no.",Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile no. is invalid");
            editTextUpdateMobile.requestFocus();
        }
        else if(TextUtils.isEmpty(textSchoolName)){
            Toast.makeText(UpdateProfileActivity.this, "Enter School Name",Toast.LENGTH_SHORT).show();
            editTextUpdateSchoolName.setError("School Name Required");
            editTextUpdateSchoolName.requestFocus();
        }
        else {
            textGender = radioButtonUpdateGenderSelected.getText().toString();
            textFullName = editTextUpdateName.getText().toString();
            textDob = editTextUpdateDoB.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();
            textSchoolName = editTextUpdateSchoolName.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateOtpSendActivity.class);
            ArrayList<String> updateUserInfo = new ArrayList<>();
            updateUserInfo.add(0, textFullName);
            updateUserInfo.add(1, textEmail);
            updateUserInfo.add(2, textDob);
            updateUserInfo.add(3, textGender);
            updateUserInfo.add(4, textMobile);
            updateUserInfo.add(5, textSchoolName);
            updateUserInfo.add(6, userID);
            Toast.makeText(UpdateProfileActivity.this,firebaseUser.getUid(),Toast.LENGTH_SHORT).show();

            intent.putExtra("UserInfo", updateUserInfo);

            verifyUser(testEmail,testPwd, textMobile);
            if(Flag == 1){
                startActivity(intent);
            }
        }

    }

    private void verifyUser(String textEmail,String textPwd, String textMobile) {



        authUpdateProfile = FirebaseAuth.getInstance();

        //create user profile
        authUpdateProfile.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(UpdateProfileActivity.this,"Details Verified",Toast.LENGTH_SHORT).show();
                                buttonUpdateProfile.setText("Update Profile");
                                authUpdateProfile.getCurrentUser().delete();
                                return;
                            }
                            else{
                                editTextUpdateMobile.setError("User is already registered with this number. Use another number.");
                                editTextUpdateMobile.requestFocus();
                                Flag = 0;
                                progressBar.setVisibility(View.GONE);
                                authUpdateProfile.getCurrentUser().delete();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                    authUpdateProfile.getCurrentUser().delete();

                }
                else{
                    Flag = 0;
                    try{
                        throw task.getException();
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(UpdateProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    authUpdateProfile.getCurrentUser().delete();
                }

            }
        });

    }


    private void showProfile(FirebaseUser firebaseUser) {

        String userIDofRegistered = firebaseUser.getUid();

        //Extracting reference from database for "Registered Users"
        DatabaseReference referenceUpdateProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        progressBar.setVisibility(View.VISIBLE);

        referenceUpdateProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    textFullName = readUserDetails.fullName;
                    textEmail = readUserDetails.email;
                    textDob = readUserDetails.doB;
                    textGender = readUserDetails.gender;
                    textMobile = readUserDetails.mobile;
                    textSchoolName = readUserDetails.schoolName;


                    editTextUpdateName.setText(textFullName);
                    editTextUpdateDoB.setText(textDob);
                    editTextUpdateMobile.setText(textMobile);
                    editTextUpdateSchoolName.setText(textSchoolName);

                    //Show Gender through Radio Button
                    if(textGender.equals("Male")){
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_male);
                    }
                    else{
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_female);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);
                }
                else{
                    Toast.makeText(UpdateProfileActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    //Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            //Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }
        else if(id == R.id.menu_update_profile){
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.menu_update_email){
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.menu_change_password){
            Intent intent = new Intent(UpdateProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.menu_delete_profile){
            Intent intent = new Intent(UpdateProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(UpdateProfileActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}