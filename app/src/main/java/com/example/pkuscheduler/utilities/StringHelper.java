package com.example.pkuscheduler.utilities;

import android.util.Log;

public class StringHelper {
    public static String betweenStrings(String str, String leftStr, String rightStr) {
        int l = str.indexOf(leftStr) + leftStr.length();
        int r = l+str.substring(l,str.length()).indexOf(rightStr);        if (l < leftStr.length() || r < 0 || l > r)
            return null;
        return str.substring(l, r);
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String getFieldFromJson(String str, String field){
        return betweenStrings(str,"\""+field+"\":\"","\",\"");
    }

}
