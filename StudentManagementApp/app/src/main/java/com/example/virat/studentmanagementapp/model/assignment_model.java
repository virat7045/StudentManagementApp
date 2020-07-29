package com.example.virat.studentmanagementapp.model;


public class assignment_model {
    private String assignment_name, start_date, due_date,name , e_no , marks;



    public assignment_model() {
    }

    public assignment_model(String assignment_name, String start_date, String due_date , String name , String e_no , String marks) {
        this.assignment_name = assignment_name;
        this.start_date = start_date;
        this.due_date = due_date;
        this.name = name;
        this.e_no = e_no;
        this.marks = marks;
    }

    public void setAssignment_name(String assignment_name) {
        this.assignment_name = assignment_name;
    }

    public String getAssignment_name() {
        return assignment_name;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setE_no(String e_no) {
        this.e_no = e_no;
    }

    public String getE_no() {
        return e_no;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getMarks() {
        return marks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
