package com.example.virat.studentmanagementapp.model;


public class practical_model {
    private String practical_name, practical_date, description,name , e_no , marks;



    public practical_model() {
    }

    public practical_model(String practical_name, String practical_date, String description , String name , String e_no , String marks) {
        this.practical_name = practical_name;
        this.practical_date = practical_date;
        this.description = description;
        this.name = name;
        this.e_no = e_no;
        this.marks = marks;
    }

    public void setPractical_name(String practical_name) {
        this.practical_name = practical_name;
    }

    public String getPractical_name() {
        return practical_name;
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

    public void setPractical_date(String practical_date) {
        this.practical_date = practical_date;
    }

    public String getPractical_date() {
        return practical_date;
    }

}
