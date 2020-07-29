package com.example.virat.studentmanagementapp.other;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;


    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "faculty_Login";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.e(TAG, "User login session: Session Manager ");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
