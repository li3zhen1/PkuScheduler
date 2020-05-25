package com.engrave.pkuscheduler.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.engrave.pkuscheduler.Utils.PkuCourse.PkuCourseLoginClient;

import java.io.IOException;

public final class CourseLoginInfoModel {
    public String studentId;
    public String password;

    public String jSessionId_Frameset;
    public String jSessionId_Calendar;
    public String jSessionId_Portal;
    public String sSessionId;
    public String guid;
    public String iaaaToken;
    public String sessionId;

    public CourseLoginInfoModel(String _studentId, String _password){
        studentId=_studentId;
        password=_password;
        jSessionId_Frameset =null;
        sSessionId=null;
        guid=null;
        iaaaToken=null;
        sessionId=null;
    }

    public void saveInstanceToSharedPreference(Context context){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor sped = sp.edit();
        sped.putString("studentId",studentId);
        sped.putString("password",password);
    }

    public static CourseLoginInfoModel getInstanceFromSharedPreference(Context context){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        CourseLoginInfoModel courseLoginInfoModel = new CourseLoginInfoModel(
                sp.getString("studentId", null),
                sp.getString("password", null)
        );
        return courseLoginInfoModel;
    }

    public static CourseLoginInfoModel getCookie(Context context){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        PkuCourseLoginClient pkuCourseLoginClient = new PkuCourseLoginClient(
                sp.getString("studentId", null),
                sp.getString("password", null)
        );
        try{
            pkuCourseLoginClient.FetchCourseCookies_Portals();
            pkuCourseLoginClient.FetchIaaaToken();
            pkuCourseLoginClient.OathValidate();
            CourseLoginInfoModel courseLoginInfoModel = pkuCourseLoginClient.GetLoginInfo();
            courseLoginInfoModel.saveInstanceToSharedPreference(context);
            return courseLoginInfoModel;
        } catch (IOException e) {
            Log.e("Error","Fail to login to pkuCourse");
            return null;
        }
    }
}
