package com.example.pkuscheduler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pkuscheduler.R;
import com.example.pkuscheduler.Utils.UI.CustomTypefaceSpan;
import com.example.pkuscheduler.Utils.PkuHelper.PkuHelperLoginClient;
import com.example.pkuscheduler.Utils.StringUtils;
import com.microsoft.officeuifabric.drawer.Drawer;

import java.io.IOException;

public class VerificationActivity extends AppCompatActivity {


    private TextView CodeInput0,CodeInput1,CodeInput2,CodeInput3,CodeInput4,CodeInput5;
    private EditText CodeCoreText;
    private String studentId;
    private TextView VerificationActivityTitle;
    private String inputVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verification);
        setUpActionBar();


        VerificationActivityTitle = findViewById(R.id.verification_title);
        final SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder("通过 PKU Helper\n获取数据");
        Typeface mCustomFont = Typeface.createFromAsset(getAssets(), "product_sans_bold.ttf");
        mSpannableStringBuilder.setSpan(new CustomTypefaceSpan(VerificationActivityTitle.getText().toString(),mCustomFont), 2, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        VerificationActivityTitle.setText(mSpannableStringBuilder);

        CodeCoreText =findViewById(R.id.codeCoreText);
        CodeInput0 = findViewById(R.id.codeInput1);
        CodeInput1 = findViewById(R.id.codeInput2);
        CodeInput2 = findViewById(R.id.codeInput3);
        CodeInput3 = findViewById(R.id.codeInput4);
        CodeInput4 = findViewById(R.id.codeInput5);
        CodeInput5 = findViewById(R.id.codeInput6);

        //CodeCoreText = findViewById(R.id.codeCoreText);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputVerificationCode = editable.toString();
                String newStr = String.format("%-6s",inputVerificationCode).replace(" ","_");
                CodeInput0.setText(String.valueOf(newStr.charAt(0)));
                CodeInput1.setText(String.valueOf(newStr.charAt(1)));
                CodeInput2.setText(String.valueOf(newStr.charAt(2)));
                CodeInput3.setText(String.valueOf(newStr.charAt(3)));
                CodeInput4.setText(String.valueOf(newStr.charAt(4)));
                CodeInput5.setText(String.valueOf(newStr.charAt(5)));
                if(editable.length()>=6){
                    FetchToken(null);
                }
            }
        };
        CodeCoreText.addTextChangedListener(tw);
        studentId = getSharedPreferences("loginInfo",MODE_PRIVATE).getString("studentId","");
    }


    public void setUpActionBar(){
        getSupportActionBar().hide();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(0x00ffffff);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    public void ShowPrivacyPolicyDrawer(View view){
        Drawer drawer = Drawer.newInstance(R.layout.drawer_privacy_policy);
        drawer.show(getSupportFragmentManager(),null);
    }

    public void SendVerificationCode(View view){

        PkuHelperAskPinTask pkuHelperAskPinTask = new PkuHelperAskPinTask("1800013025");
        pkuHelperAskPinTask.execute();
    }

    public void FetchToken(View view){

        PkuHelperFetchTokenTask pkuHelperFetchTokenTask = new PkuHelperFetchTokenTask();
        pkuHelperFetchTokenTask.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private class PkuHelperAskPinTask extends AsyncTask<Void, Void, String>{
        PkuHelperAskPinTask(String studentId){
        }
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        protected String doInBackground(Void... voids) {
            if(!isNetworkAvailable())
                return getString(R.string.Network_Error);
            try{
                if(!PkuHelperLoginClient.AskForPin(studentId))
                    return getString(R.string.Network_Error);
                return getString(R.string.VerificationActivity_AskPinSuccess);
            } catch (IOException e) {
                return getString(R.string.Api_Error);
            }
        }

        @Override
        protected void onPostExecute(final String str){
            CodeCoreText.setVisibility(View.VISIBLE);
            findViewById(R.id.verification_form).setVisibility(View.VISIBLE);
            CodeCoreText.requestFocus();
            CodeCoreText.requestFocusFromTouch();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(CodeCoreText, 0);
        }

        @Override
        protected void onCancelled(){

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PkuHelperFetchTokenTask extends AsyncTask<Void, Void, String>{
        PkuHelperFetchTokenTask(){
        }
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        protected String doInBackground(Void... voids) {
            if(!isNetworkAvailable())
                return getString(R.string.Network_Error);
            try{
                if(inputVerificationCode==null||inputVerificationCode.length()!=6)
                    return getString(R.string.VerificationActivity_VerificationCodeMissing);
                String jsonResponse = PkuHelperLoginClient.FetchToken(
                        studentId,
                        inputVerificationCode
                );
                SharedPreferences sharedPreferences_pkuHelperLoginInfo = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences_pkuHelperLoginInfo.edit();
                editor.putString("pkuHelperToken", StringUtils.getFieldFromJson(jsonResponse,"user_token"));
                editor.putString("userName", StringUtils.getFieldFromJson(jsonResponse,"name"));
                editor.putString("gender", StringUtils.getFieldFromJson(jsonResponse,"gender"));
                editor.putString("department", StringUtils.getFieldFromJson(jsonResponse,"department"));
                editor.apply();
                return getString(R.string.VerificationActivity_FetchTokenSuccess);
            } catch (IOException e) {
                return getString(R.string.Api_Error);
            }
        }

        @Override
        protected void onPostExecute(final String str){
            TransitionToTodayActivity();
        }

        //TODO：中断行为处理
        @Override
        protected void onCancelled(){

        }
    }

    public void TransitionToTodayActivity(){
        Intent intent = new Intent(this, TodayActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
