package com.example.virat.studentmanagementapp.model;


public class quiz_model {
    private String quiz_name, quiz_date, description,name , e_no , marks;



    public quiz_model() {
    }

    public quiz_model(String quiz_name, String quiz_date, String description , String name , String e_no , String marks) {
        this.quiz_name = quiz_name;
        this.quiz_date = quiz_date;
        this.description = description;
        this.name = name;
        this.e_no = e_no;
        this.marks = marks;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setQuiz_date(String quiz_date) {
        this.quiz_date = quiz_date;
    }

    public String getQuiz_date() {
        return quiz_date;
    }

}
