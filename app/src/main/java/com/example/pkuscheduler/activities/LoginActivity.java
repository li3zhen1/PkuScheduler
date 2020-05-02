package com.example.pkuscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pkuscheduler.data.LoginInfoRepository;
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

import com.example.pkuscheduler.R;
import com.example.pkuscheduler.utilities.LoginClient;

public class LoginActivity extends AppCompatActivity {

    private TextView LoginStudentIdView;
    private EditText LoginPasswordView;
    private ImageButton LoginSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        setUpActionBar();
        LoginStudentIdView = findViewById(R.id.Login_input_id);
        LoginPasswordView = findViewById(R.id.Login_input_password);
        LoginSubmitButton = findViewById(R.id.login_button);
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

        private final LoginClient loginClient;

        UserLoginTask(String _studentId, String _password) {
            loginClient = new LoginClient(_studentId,_password);
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
                Boolean hasIaaaToken = loginClient.FetchIaaaToken();
                Boolean hasCookies = loginClient.FetchCookies();
                Boolean hasJSessionId = loginClient.FetchJSessionId();
                SharedPreferences sharedPreferences_LoginInfo = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences_LoginInfo.edit();

                if(hasIaaaToken&&hasCookies&&hasJSessionId){
                    LoginInfoRepository loginInfoRepository = loginClient.GetLoginInfo();
                    editor.putString("sSessionId", loginInfoRepository.sSessionId);
                    editor.putString("studentId", loginInfoRepository.studentId);
                    editor.putString("password", loginInfoRepository.password);
                    editor.putString("jSessionId", loginInfoRepository.jSessionId);
                    editor.putString("guid",loginInfoRepository.password);
                    editor.putString("sessionId",loginInfoRepository.sessionId);
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
        userLoginTask.execute((Void) null);

    }

    public void ShowPrivacyPolicyDrawer(View view){
        Drawer drawer = Drawer.newInstance(R.layout.drawer_privacy_policy);
        drawer.show(getSupportFragmentManager(),null);

    }

    public void TransitionToVerificationActivity(){
        Intent intent = new Intent(this, VerificationActivity.class);
        startActivity(intent);
    }

}
