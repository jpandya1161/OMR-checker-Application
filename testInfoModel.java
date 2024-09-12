package com.msquare.omr;

public class testInfoModel {

    String standard, division, subject, examName, totalStudents, totalQuestions, marksPerQuestion, negativeMarks, options, testCreatorID;

    public testInfoModel() {
    }

    public testInfoModel(String standard, String division, String subject, String examName, String totalStudents, String totalQuestions, String marksPerQuestion, String negativeMarks, String options, String testCreatorID) {
        this.standard = standard;
        this.division = division;
        this.subject = subject;
        this.examName = examName;
        this.totalStudents = totalStudents;
        this.totalQuestions = totalQuestions;
        this.marksPerQuestion = marksPerQuestion;
        this.negativeMarks = negativeMarks;
        this.options = options;
        this.testCreatorID = testCreatorID;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(String totalStudents) {
        this.totalStudents = totalStudents;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getMarksPerQuestion() {
        return marksPerQuestion;
    }

    public void setMarksPerQuestion(String marksPerQuestion) { this.marksPerQuestion = marksPerQuestion; }

    public String getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(String negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getTestCreatorID() {
        return testCreatorID;
    }

    public void setTestCreatorID(String testCreatorID) {
        this.testCreatorID = testCreatorID;
    }
}
