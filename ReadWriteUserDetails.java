package com.msquare.omr;

public class ReadWriteUserDetails {

    public  String fullName,email, doB, gender,mobile,schoolName;

    //Constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textFullName, String textEmail, String textDOB, String textGender, String textMobile, String textSchoolName){
        this.fullName = textFullName;
        this.email = textEmail;
        this.doB = textDOB;
        this.gender = textGender;
        this.mobile = textMobile;
        this.schoolName = textSchoolName;
    }


}
