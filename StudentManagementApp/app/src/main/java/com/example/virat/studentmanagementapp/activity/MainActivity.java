package com.example.virat.studentmanagementapp.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.*;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.virat.studentmanagementapp.R;
import com.example.virat.studentmanagementapp.fragment.AssignmentFragment;
import com.example.virat.studentmanagementapp.fragment.AttendanceFragment;
import com.example.virat.studentmanagementapp.fragment.LabFragment;
import com.example.virat.studentmanagementapp.fragment.QuizFragment;
import com.example.virat.studentmanagementapp.other.AppConfig;
import com.example.virat.studentmanagementapp.other.AppController;
import com.example.virat.studentmanagementapp.other.CircleTransform;
import com.example.virat.studentmanagementapp.other.SQLiteHandler;
import com.example.virat.studentmanagementapp.other.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.virat.studentmanagementapp.other.AppConfig.URL_IMAGE;

public class MainActivity extends AppCompatActivity implements AttendanceFragment.OnFragmentInteractionListener, AssignmentFragment.OnFragmentInteractionListener, LabFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtName;
    private Toolbar toolbar;
    /*private FloatingActionButton fab;
*/
    public static int navItemIndex = 0;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private static final String Attendance_TAG = "Attendance";
    private static final String Assignment_TAG = "Assignment_Details";
    private static final String Lab_TAG = "LAb";
    private static final String Quiz_TAG = "Quiz_Details";
    private static final String TAG = " ";
    public static String CURRENT_TAG = Attendance_TAG;
    private SessionManager session;
    private SQLiteHandler db;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
  /*      fab = (FloatingActionButton) findViewById(R.id.fab);
*/
        //Navigation View header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
  /*      txtWebsite = (TextView) navHeader.findViewById(R.id.website);
  */
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        // session manager
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // SqLite database handler

        HashMap<String, String> user = db.getUserDetails();

        String f_id = user.get("faculty_id");
        String nm = user.get("username");
        String name = user.get("name");
        String profile = user.get("profile");
        Log.e(TAG, profile);
        txtName.setText(name);
        if (!profile.toString().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(URL_IMAGE);
            sb.append(profile);
            Glide.with(this).load(sb.toString())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
 //           imgProfile.setImageUrl(sb.toString(), imageLoader);

        }
        else
        {
            Glide.with(this).load(R.mipmap.ic_launcher)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
            //
        }



        /*Toast.makeText(getApplicationContext(),f_id+" "+nm,Toast.LENGTH_LONG ).show();*/

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //loadNavHeader();
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            //CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


    }


    /*private void loadNavHeader() {
        // name, website
        txtName.setText("Ravi Tamada");
        txtWebsite.setText("www.androidhive.info");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to actionbar_attendance label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }*/

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

  /*          // show or hide the fab button
            toggleFab();
  */
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

     /*   // show or hide the fab button
        toggleFab();
*/
        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {

            case 0:
                // Attendance
                AttendanceFragment attendanceFragment = new AttendanceFragment();
                return attendanceFragment;
            case 1:
                // Assignment_Details
                AssignmentFragment assignmentFragment = new AssignmentFragment();
                return assignmentFragment;
            case 2:
                // Practical_Details
                LabFragment labFragment = new LabFragment();
                return labFragment;
            case 3:
                // Quiz_Details
                QuizFragment quizFragment = new QuizFragment();
                return quizFragment;
            default:
                return new AttendanceFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_attendance:
                        navItemIndex = 0;
                        CURRENT_TAG = Attendance_TAG;
                        break;
                    case R.id.nav_assignment:
                        navItemIndex = 1;
                        CURRENT_TAG = Assignment_TAG;
                        break;
                    case R.id.nav_lab:
                        navItemIndex = 2;
                        CURRENT_TAG = Lab_TAG;
                        break;
                    case R.id.nav_quiz:
                        navItemIndex = 3;
                        CURRENT_TAG = Quiz_TAG;
                        break;
                    case R.id.nav_logout:
                        logoutUser();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = Attendance_TAG;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is actionbar_attendance, load the menu created for actionbar_attendance
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
            Snackbar.make(getWindow().getDecorView().getRootView(), "Logged Out", Snackbar.LENGTH_LONG).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
*/

    private void logoutUser() {

        session.setLogin(false);

        // Launching the login activity
        db.deleteUsers();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

   /* // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.sho w();
        else
            fab.hide();
    }*/


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

