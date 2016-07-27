package com.at.solcoal.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.model.UserInfo;

/**
 * Created by anjan on 29-03-2016.
 */
public class SharedPreferenceUtility {
    public static UserInfo getUserInfo(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        UserInfo userInfo = null;
        if(sharedpreferences!= null && (sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null) != null)) {
            userInfo = new UserInfo();
            userInfo.setId(sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null));
            userInfo.setName(sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_NAME, null));
            userInfo.setEmail(sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_EMAIL, null));
            userInfo.setExtId(sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_EXT_ID, null));
            userInfo.setGender(sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_GENDER, null));
            userInfo.setExtSource(sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_SOURCE, null));

        }
        return userInfo;
    }
}
