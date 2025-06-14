package com.example.schoolproj.classes;

public class Mark {
    private int studentId;
    private int subjectId;
    private int markId;
    private CharSequence markValue;
    private String markType;

    // Constructor
    public Mark(int studentId, int subjectId, int markId, CharSequence markValue, String markType) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.markId = markId;
        this.markValue = markValue;
        this.markType = markType;
    }


    public Mark() {}

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public CharSequence getMarkValue() {
        return markValue;
    }

    public void setMarkValue(CharSequence markValue) {
        this.markValue = markValue;
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }
}
