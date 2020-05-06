package com.example.pkuscheduler.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String betweenStrings(String str, String leftStr, String rightStr) {
        int l = str.indexOf(leftStr) + leftStr.length();
        int r = l+str.substring(l,str.length()).indexOf(rightStr);
        if (l < leftStr.length() || r < 0 || l > r)
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


    public static String getUnicodeEscaped(String unescaped){
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unescaped);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            unescaped = unescaped.replace(matcher.group(1), ch+"" );
        }
        return unescaped;
    }
}
