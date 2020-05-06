package com.example.pkuscheduler.Models;

public class CourseLoginInfoModel {
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
}
