package com.example.pkuscheduler.Utils.PkuHelper;

import java.util.Date;

public class ApiRepository {
    public static int getApiVerTimeToken() {
        return (int) (2 * Math.floor(new Date().getTime() / 72e5));
    }

    public static String getUrl(String studentId) {
        return "https://pkuhelper.pku.edu.cn/api_xmcp/login/send_code?user=" + studentId
                + "&code_type=sms&PKUHelperAPI=3.0&jsapiver=200326204124-" + getApiVerTimeToken();
    }

    public static String getTokenUrl(String studentId, String validCode) {
        return "https://pkuhelper.pku.edu.cn/api_xmcp/login/login?user=" + studentId
                + "&valid_code=" + validCode
                + "&PKUHelperAPI=3.0&jsapiver=200326204124-" + getApiVerTimeToken();
    }

    public static String getPKUHoleUrl(String helperToken) {
        return "https://pkuhelper.pku.edu.cn/services/pkuhole/api.php?action=getlist&p=1&PKUHelperAPI=3.0&jsapiver=200326204124-" + getApiVerTimeToken() +
                "&user_token=" + helperToken;
    }

    public static String getPKUHelperScheduleUrl(String helperToken) {
        return "http://pkuhelper.pku.edu.cn/services/login/courses.php?user_token=" + helperToken;
    }
}
