package com.example.pkuscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pkuscheduler.Models.CourseLoginInfoModel;
import com.microsoft.officeuifabric.drawer.Drawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.example.pkuscheduler.R;
import com.example.pkuscheduler.Utils.CourseLoginClient;

public class LoginActivity extends AppCompatActivity {

    private TextView LoginStudentIdView;
    private EditText LoginPasswordView;
    private ImageButton LoginSubmitButton;
    private LottieAnimationView LottieButton;

    private LottieAnimationView LottieButtonLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        setUpActionBar();
        LoginStudentIdView = findViewById(R.id.Login_input_id);
        LoginPasswordView = findViewById(R.id.Login_input_password);
        LoginSubmitButton = findViewById(R.id.login_button);
        LottieButton = findViewById(R.id.login_button_lottie);
        LottieButtonLoading = findViewById(R.id.login_button_loading);
    }

    public void setUpActionBar(){
        getSupportActionBar().hide();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(0x00ffffff);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    private class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final CourseLoginClient courseLoginClient;

        UserLoginTask(String _studentId, String _password) {
            courseLoginClient = new CourseLoginClient(_studentId,_password);
        }


        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (!isNetworkAvailable()) {
                return -1;
            }
            String bgWorkResult;
            try {
                Boolean hasIaaaToken = courseLoginClient.FetchIaaaToken();
                Boolean hasCookies = courseLoginClient.FetchCookies();
                Boolean hasJSessionId = courseLoginClient.FetchJSessionId();
                SharedPreferences sharedPreferences_LoginInfo = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences_LoginInfo.edit();

                if(hasIaaaToken&&hasCookies&&hasJSessionId){
                    CourseLoginInfoModel courseLoginInfoModel = courseLoginClient.GetLoginInfo();
                    editor.putString("sSessionId", courseLoginInfoModel.sSessionId);
                    editor.putString("studentId", courseLoginInfoModel.studentId);
                    editor.putString("password", courseLoginInfoModel.password);
                    editor.putString("jSessionId", courseLoginInfoModel.jSessionId);
                    editor.putString("guid", courseLoginInfoModel.password);
                    editor.putString("sessionId", courseLoginInfoModel.sessionId);
                    editor.putBoolean("isLogged",true);
                    editor.apply();
                    return 0;
                }
                else{
                    editor.putBoolean("isLogged",false);
                    if(!hasIaaaToken)return 1;
                    if(!hasCookies)return 2;
                    if(!hasJSessionId)return 3;
                }
                return 4;
/*                if (false) {
                    Boolean isCookiesFetched = loginClient.FetchCookies();
                    if(isCookiesFetched){
                        loginClient.FetchJSessionId();
                    }else return "CookiesFetchFailed";


                    // get Basic info
                   */
/* request = "https://course.pku.edu.cn/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_3_1";
                    url = new URL(request);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestProperty("Cookie", "JSESSIONID=" + course_j_session_id +"; session_id=" + session_id
                            +"; s_session_id=" + s_session_id+"; web_client_cache_guid=" + web_client_cache_guid);
                    //conn.setRequestMethod("GET");

                    System.out.println( conn.getResponseCode());
                    in = conn.getInputStream();
                    str = convertStreamToString(in);

                    //System.out.println("\nBasicInfo "+str);

                    String infos = betweenStrings(str, "&url=\" target=\"_top\">", "</a>\n</li>");
                    System.out.println(infos);
                    conn.disconnect();



                    request = "https://course.pku.edu.cn/webapps/calendar/calendarData/selectedCalendarEvents?start=1285411200000&end=1589040000000&course_id=&mode=personal";
                    url = new URL(request);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestProperty("Cookie", "JSESSIONID=" + course_j_session_id +"; session_id=" + session_id
                            +"; s_session_id=" + s_session_id+"; web_client_cache_guid=" + web_client_cache_guid);
                    //conn.setRequestMethod("GET");
                    in = conn.getInputStream();
                    str = convertStreamToString(in);

                    System.out.println("\nCalendar "+str);*//*
                *//*
                // 存储进SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("session_id", session_id);
                editor.putString("student_id", mstudentid);
                editor.putString("password", mPassword);
                editor.putString("name", infos.split(" ")[1]);
                editor.putString("school", infos.split(" ")[0]);
                editor.apply();*//*
                    return "hello";
                } else {
                    return "iaaa connect failed";
                }*/
            } catch (Exception e) {
//                e.printStackTrace();
                return 4;
            }
        }

        @Override
        protected void onPostExecute(final Integer returnStatus) {

            switch (returnStatus){
                case 0:
                    TransitionToVerificationActivity();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Something went wrong...",Toast.LENGTH_LONG).show();
                    ResetLottieButton();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_LONG);
        }
    }

    public void AttemptLogin(View view){
        UserLoginTask userLoginTask = new UserLoginTask(
                LoginStudentIdView.getText().toString(),
                LoginPasswordView.getText().toString()
        );
        StartLottieButton();
        userLoginTask.execute((Void) null);

    }

    public void ResetLottieButton(){
        LottieButton.pauseAnimation();
        LottieButton.setFrame(0);
        LottieButtonLoading.pauseAnimation();
        LottieButtonLoading.setFrame(0);
    }
    public void StartLottieButton(){
        LottieButton.playAnimation();
        LottieButtonLoading.playAnimation();
    }
    public void ShowPrivacyPolicyDrawer(View view){
        Drawer drawer = Drawer.newInstance(R.layout.drawer_privacy_policy);
        drawer.show(getSupportFragmentManager(),null);

    }

    public void TransitionToVerificationActivity(){
        Intent intent = new Intent(this, VerificationActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
