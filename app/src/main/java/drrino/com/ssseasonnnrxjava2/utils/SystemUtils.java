package drrino.com.ssseasonnnrxjava2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import drrino.com.ssseasonnnrxjava2.App;

public class SystemUtils {
    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance()
            .getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

}
