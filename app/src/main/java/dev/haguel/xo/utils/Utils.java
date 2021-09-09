package dev.haguel.xo.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Utils {

    public static void changeFragment(int container, FragmentTransaction ft, Fragment fragment, String tag, boolean delay){
        if (TextUtils.isEmpty(tag)) {
            ft.replace(container, fragment);
        } else {
            ft.replace(container, fragment, tag);
        }

        if (delay){
            new Handler(Looper.getMainLooper()).postDelayed(ft::commit,1400);
        } else {
            ft.commit();
        }
    }

    public static void changeFragment(int container, FragmentTransaction ft, Fragment fragment, boolean delay){
        changeFragment(container, ft, fragment, null, delay);
    }
}
