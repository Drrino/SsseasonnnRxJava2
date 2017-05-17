package drrino.com.ssseasonnnrxjava2;

import android.app.Application;

/**
 * Created by Dell1 on 2017/5/16.
 */

public class App extends Application {
    private static App instance;


    public static synchronized App getInstance() {
        return instance;
    }


    @Override public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
