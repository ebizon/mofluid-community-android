package com.mofluid.magento2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.mofluid.magento2.database.MyDataBaseAdapter;

import java.util.ArrayList;

/**
 * Created by ebizon on 17/12/15.
 */
public class UserSession {


    private static final String TAG="UserSession";

    public static void setSession(Activity act)
    {
        UserProfileItem user=getActiveUser(act);

         SharedPreferences mySharedPreference;
         SharedPreferences.Editor preference_editor_session;

        mySharedPreference=act.getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        preference_editor_session =mySharedPreference.edit();
        if(user!=null) {
            Log.d(TAG, "setSession() called with: " + "USER_INFO_USER_ID"+user.getId());
            Log.d(TAG, "setSession() called with: " + "user's session set successfully= user name  [" +  user.getUsername() + "], status  = [" +  user.getLogin_status() + "]");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_FNAME, user.getFirstname());
            preference_editor_session.putString(ConstantDataMember.USER_INFO_LNAME, user.getLastname());
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_ID, user.getId());
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_NAME, user.getUsername());
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_PASSWORD, user.getPassword());
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_LOGIN_STATUS, user.getLogin_status());
        }
        else {

            Log.d(TAG, "setSession() called with: " + "user's session destroy set successfully");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_FNAME, "");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_LNAME,  "");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_ID, "");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_NAME,  "");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_PASSWORD, "");
            preference_editor_session.putString(ConstantDataMember.USER_INFO_USER_LOGIN_STATUS,  "");
        }

        preference_editor_session.commit();

    }

    public static UserProfileItem getActiveUser(Activity act)
    {
        UserProfileItem user=null;
        ArrayList<UserProfileItem> userList=new ArrayList<UserProfileItem>();
        MyDataBaseAdapter dbAdapter=new MyDataBaseAdapter(act);

        userList=dbAdapter.getUserProfile();

        if(userList!=null)
        {
            for(UserProfileItem users:userList)
            {
                if(users.getLogin_status().equalsIgnoreCase("1"))
                {
                    user=users;
                    break;
                }
                else
                {
                    user=null;
                }
            }
        }

        UserProfileItem userProfileItem = UserManager.getInstance().getUser();
        if(userProfileItem == null){
            UserManager.getInstance().setUser(user);
        }

        return  user;
    }

}
