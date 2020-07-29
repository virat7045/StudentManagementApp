package com.example.virat.studentmanagementapp.other;

import android.util.Log;

import com.example.virat.studentmanagementapp.R;

public class AppConfig {
    //Genymotion (10.0.3.2) //Android AVD (10.0.2.2)
    // Server user login url
    /*public static String Base_Url = "192.168.42.14";
*/
    /*public static String URL_LOGIN = "http://192.168.0.4/app/login.php";
    public static String URL_LACTURE_DETAILS = "http://192.168.0.4/app/attendance/lecture_details.php";
    public static String URL_STUDENT_LECTURE_DETAILS = "http://192.168.0.4/app/attendance/student_lecture_details.php";
    public static String URL_STUDENT_LAB_DETAILS = "http://192.168.0.4/app/attendance/student_lab_details.php";
    public static String URL_SAVE_LECTURE_ATTENDANCE = "http://192.168.0.4/app/attendance/save_student_lecture_attendance.php";
    public static String URL_SAVE_LAB_ATTENDANCE = "http://192.168.0.4/app/attendance/save_student_lab_attendance.php";
    public static String URL_UPDATE_ATTENDANCE_DETAILS = "http://192.168.0.4/app/attendance/update_lecture_attendance_details.php";
    public static String URL_UPDATE_LECTURE_ATTENDANCE = "http://192.168.0.4/app/attendance/update_lecture_attendance.php";
    public static String URL_UPDATE_LAB_ATTENDANCE_DETAILS = "http://192.168.0.4/app/attendance/update_lab_attendance_details.php";
    public static String URL_UPDATE_LAB_ATTENDANCE = "http://192.168.0.4/app/attendance/update_lab_attendance.php";
*/  //public static String Base_Url = "10.0.2.2";
    public static String Base_Url = "10.0.2.2";
    public static String URL_LOGIN = "http://"+Base_Url+"/app/login.php";
    public static String URL_LACTURE_DETAILS = "http://"+Base_Url+"/app/attendance/lecture_details.php";
    public static String URL_STUDENT_LECTURE_DETAILS = "http://"+Base_Url+"/app/attendance/student_lecture_details.php";
    public static String URL_STUDENT_LAB_DETAILS = "http://"+Base_Url+"/app/attendance/student_lab_details.php";

    public static String URL_IMAGE = "http://"+Base_Url+"/admin/Gentelella/img/";
//Attendance
    public static String URL_SAVE_LECTURE_ATTENDANCE = "http://"+Base_Url+"/app/attendance/save_student_lecture_attendance.php";
    public static String URL_SAVE_LAB_ATTENDANCE = "http://"+Base_Url+"/app/attendance/save_student_lab_attendance.php";
    public static String URL_UPDATE_ATTENDANCE_DETAILS = "http://"+Base_Url+"/app/attendance/update_lecture_attendance_details.php";
    public static String URL_UPDATE_LECTURE_ATTENDANCE = "http://"+Base_Url+"/app/attendance/update_lecture_attendance.php";
    public static String URL_UPDATE_LAB_ATTENDANCE_DETAILS = "http://"+Base_Url+"/app/attendance/update_lab_attendance_details.php";
    public static String URL_UPDATE_LAB_ATTENDANCE = "http://"+Base_Url+"/app/attendance/update_lab_attendance.php";
    public static String URL_GENERATE_FETCH_LECTURE_MARKS="http://"+Base_Url+"/app/attendance/generate_fetch_lecture_marks.php";
    public static String URL_GENERATE_FETCH_LAB_MARKS="http://"+Base_Url+"/app/attendance/generate_fetch_lab_marks.php";
//Assignment
    public static String URL_ASSIGNMENT_DETAILS= "http://"+Base_Url+"/app/assignment/assignment_details.php";
    public static String URL_ASSIGNMENT_ADD="http://"+Base_Url+"/app/assignment/assignment_add.php";
    public static String URL_ASSIGNMENT_FETCH_MARKS="http://"+Base_Url+"/app/assignment/assignment_fetch_marks.php";
    public static String URL_ASSIGNMENT_UPDATE_MARKS="http://"+Base_Url+"/app/assignment/assignment_update_marks.php";
    public static String URL_GENERATE_FETCH_ASSIGNMENT_MARKS="http://"+Base_Url+"/app/assignment/generate_fetch_assignment_marks.php";

//Quiz
    public static String URL_QUIZ_DETAILS= "http://"+Base_Url+"/app/quiz/quiz_details.php";
    public static String URL_QUIZ_ADD="http://"+Base_Url+"/app/quiz/quiz_add.php";
    public static String URL_QUIZ_FETCH_MARKS="http://"+Base_Url+"/app/quiz/quiz_fetch_marks.php";
    public static String URL_QUIZ_UPDATE_MARKS="http://"+Base_Url+"/app/quiz/quiz_update_marks.php";
    public static String URL_GENERATE_FETCH_QUIZ_MARKS="http://"+Base_Url+"/app/quiz/generate_fetch_quiz_marks.php";

//Practical
    public static String URL_PRACTICAL_DETAILS= "http://"+Base_Url+"/app/practical/practical_details.php";
    public static String URL_PRACTICAL_ADD="http://"+Base_Url+"/app/practical/practical_add.php";
    public static String URL_PRACTICAL_FETCH_MARKS="http://"+Base_Url+"/app/practical/practical_fetch_marks.php";
    public static String URL_PRACTICAL_UPDATE_MARKS="http://"+Base_Url+"/app/practical/practical_update_marks.php";
    public static String URL_GENERATE_FETCH_PRACTICAL_MARKS="http://"+Base_Url+"/app/practical/generate_fetch_practical_marks.php";
    //Device
    /*public static String URL_LOGIN = "http://192.168.42.69/app/login.php";
    public static String URL_LACTURE_DETAILS = "http://192.168.42.69/app/lacture_details.php";
*/
    /*public static String URL_LOGIN = "http://192.168.42.14/app/login.php";
    public static String URL_LACTURE_DETAILS = "http://192.168.42.14/app/lacture_details.php";
*/
    //Home
    // public static String URL_LOGIN = "http://192.168.0.4/app/login.php";
    // public static String URL_LACTURE_DETAILS = "http://192.168.0.4/app/lacture_details.php";

   /* public static String URL_LOGIN =                    "http://10.0.2.2/app/login.php";
    public static String URL_LACTURE_DETAILS =          "http://10.0.2.2/app/attendance/lecture_details.php";
    public static String URL_STUDENT_LECTURE_DETAILS =  "http://10.0.2.2/app/attendance/student_lecture_details.php";
    public static String URL_STUDENT_LAB_DETAILS =      "http://10.0.2.2/app/attendance/student_lab_details.php";
    public static String URL_SAVE_LECTURE_ATTENDANCE =  "http://10.0.2.2/app/attendance/save_student_lecture_attendance.php";
    public static String URL_SAVE_LAB_ATTENDANCE =      "http://10.0.2.2/app/attendance/save_student_lab_attendance.php";
*/
}