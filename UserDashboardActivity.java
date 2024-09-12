package com.msquare.omr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private TextView textViewWelcome, sideNavName;
    private ImageView imageView;
    private ImageView sideNavImageView;
    private FirebaseAuth authDashboard;
    private FirebaseUser firebaseUser;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fabAddTest;
    RecyclerView recyclerViewTestInfo;
    ArrayList<testInfoModel> testInfoList;
    FirebaseFirestore testInfoDB;
    testInfoAdapter testInfoAdap;




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        recyclerViewTestInfo = (RecyclerView)findViewById(R.id.testInfoRecycler);
        recyclerViewTestInfo.setLayoutManager(new LinearLayoutManager(this));
        testInfoList = new ArrayList<>();



        testInfoDB = FirebaseFirestore.getInstance();
        testInfoDB.collection("Tests").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot d:list){
                    testInfoModel obj = new testInfoModel();
                    obj.division = d.get("division").toString();
                    obj.standard = d.get("standard").toString();
                    obj.examName = d.get("exam name").toString();
                    obj.subject = d.get("subject").toString();
                    obj.options = d.get("options").toString();
                    obj.totalQuestions = d.get("questions").toString();
                    obj.totalStudents = d.get("students").toString();
                    obj.marksPerQuestion = d.get("marks per question").toString();
                    obj.negativeMarks = d.get("negative marks per question").toString();
                    obj.testCreatorID = d.get("testCreatorID").toString();

                    assert obj != null;
                    if (obj.testCreatorID.equals(firebaseUser.getUid())){
                        testInfoList.add(obj);
                    }
                }

                if(testInfoList.isEmpty()){
                    Toast.makeText(UserDashboardActivity.this,"No tests found",Toast.LENGTH_SHORT).show();
                }
                testInfoAdap.notifyDataSetChanged();

            }

        });

        testInfoAdap = new testInfoAdapter(testInfoList);
        recyclerViewTestInfo.setAdapter(testInfoAdap);

        drawerLayout = findViewById(R.id.dashboard_drawer);
        navigationView = (NavigationView) findViewById(R.id.sideNavigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.menu_Open,R.string.menu_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        fabAddTest = findViewById(R.id.fab_add_test);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        sideNavName = (TextView)headerView.findViewById(R.id.sidenav_name);
        sideNavImageView = (ImageView) headerView.findViewById(R.id.sidenav_profile_dp);

        textViewWelcome = findViewById(R.id.textView_welcome);
        imageView = findViewById(R.id.imageView_dp);

        authDashboard = FirebaseAuth.getInstance();
        firebaseUser = authDashboard.getCurrentUser();

        getSupportActionBar().setTitle(firebaseUser.getDisplayName()+"'s Dashboard");
        //getSupportActionBar().setTitle("Dashboard");

        if(firebaseUser == null){
            Toast.makeText(UserDashboardActivity.this,"Something went wrong. User's details not available at the moment.",Toast.LENGTH_SHORT).show();

        }
        else{
            showUserProfile(firebaseUser);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_TAG","Home clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayout.clearFocus();
                        Log.i("MENU_DRAWER_TAG","Profile clicked");
                        Intent intent = new Intent(UserDashboardActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_share:
                        Log.i("MENU_DRAWER_TAG","Share clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayout.clearFocus();
                        String path = getExternalFilesDir(null).getAbsolutePath()+"/";
                        //Toast.makeText(UserDashboardActivity.this,path,Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(path);
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setDataAndType(uri,"*/*");
                        startActivity(i);
                        break;

                    case R.id.nav_logout:
                        Log.i("MENU_DRAWER_TAG","Logout clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        drawerLayout.clearFocus();
                        Toast.makeText(UserDashboardActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
                        showAlertDialog();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.clearFocus();
                return true;
            }
        });



        fabAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboardActivity.this, AddTestActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }



    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboardActivity.this);
        builder.setTitle("Logout?");
        builder.setMessage("Press okay to logout!");

        //Open email Spp if clicked on continue
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                authDashboard.signOut();
                Intent logoutIntent = new Intent(UserDashboardActivity.this, LoginActivity.class);

                //Clear Stack
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });

        //Create alert dialog box
        AlertDialog alertDialog = builder.create();

        //show alert dialog box
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered Users"
        DatabaseReference referenceDashboard = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceDashboard.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    String name = readUserDetails.fullName;
                    textViewWelcome.setText("Welcome, "+ name + "!");
                    sideNavName.setText(name);

                    //Set User DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageView setImageURI() not used
                    //So using Picasso
                    if(uri != null){
                        Picasso.with(UserDashboardActivity.this).load(uri).into(imageView);
                        Picasso.with(UserDashboardActivity.this).load(uri).into(sideNavImageView);
                    }
                }
                else{
                    Toast.makeText(UserDashboardActivity.this,"Something went wrong.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed() {

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