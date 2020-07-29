package com.example.virat.studentmanagementapp.model;


public class attendance_model {
    private String sem, classname, subject, batch, labname, name, e_no;
    private boolean checkbox_value = false;

    public attendance_model() {
    }

    public attendance_model(String subject, String sem, String classname, String batch, String labname, String name, String e_no, boolean checkbox_value) {
        this.subject = subject;
        this.sem = sem;
        this.classname = classname;
        this.batch = batch;
        this.labname = labname;
        this.name = name;
        this.e_no = e_no;
        this.checkbox_value = checkbox_value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCheckbox_value(boolean checkbox_value) {
        this.checkbox_value = checkbox_value;
    }

    public boolean getCheckbox_value() {
        return checkbox_value;
    }

    public boolean isCheckbox_value() {
        return checkbox_value;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }

    public String getLabname() {
        return labname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setE_no(String e_no) {
        this.e_no = e_no;
    }

    public String getE_no() {
        return e_no;
    }
}
