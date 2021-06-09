package com.nirwal.electricaltools;

import android.util.Log;

import androidx.navigation.NavController;

import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.ui.model.FragmentAddress;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";
    public static final int getNavigationActionIdFromClassName(String className){
        for(FragmentAddress address : Constants.getFragmentAddressBook()){

            if(address.name.equals(className)){
                Log.d(TAG, "getNavigationActionIdFromClassName: "+address.name);
                return address.route;
            }

        }
        Log.d(TAG, "getNavigationActionIdFromClassName: "+0);
        return 0;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
