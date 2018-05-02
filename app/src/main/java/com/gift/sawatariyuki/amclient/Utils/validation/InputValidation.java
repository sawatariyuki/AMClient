package com.gift.sawatariyuki.amclient.Utils.validation;

import android.text.TextUtils;

public class InputValidation {
    public static Boolean isEmail(String email){
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        return !TextUtils.isEmpty(strPattern) && email.matches(strPattern);
    }
}
