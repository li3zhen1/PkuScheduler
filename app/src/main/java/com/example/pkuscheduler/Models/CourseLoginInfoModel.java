package com.example.pkuscheduler.Models;

import android.content.Context;
import android.content.SharedPreferences;

public final class CourseLoginInfoModel {
    public String studentId;
    public String password;

    public String jSessionId;
    public String sSessionId;
    public String guid;
    public String iaaaToken;
    public String sessionId;

    public CourseLoginInfoModel(String _studentId, String _password){
        studentId=_studentId;
        password=_password;
        jSessionId=null;
        sSessionId=null;
        guid=null;
        iaaaToken=null;
        sessionId=null;
    }

    public static CourseLoginInfoModel getInstanceFromSharedPreference(Context context){
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        CourseLoginInfoModel courseLoginInfoModel = new CourseLoginInfoModel(
                sp.getString("studentId", ""),
                sp.getString("password", "")
        );
        courseLoginInfoModel.guid = sp.getString("guid", "");
        courseLoginInfoModel.sSessionId = sp.getString("sSessionId", "");
        courseLoginInfoModel.jSessionId = sp.getString("jSessionId", "");
        courseLoginInfoModel.sessionId = sp.getString("sessionId", "");
        return courseLoginInfoModel;
    }
}
