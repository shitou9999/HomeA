package com.home.shitou.homec.Utils;

import android.content.Context;

public class DisplayUtil {
    public static int getScreenWindth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

}