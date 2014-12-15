package com.colleagues.hintman;
import android.app.*;
import com.parse.*;

public class App extends Application
{
	private static App INSTANCE = null;

	public App() {
        INSTANCE = this;
    }

	

    public static App getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new App();
        }

        return INSTANCE;
    }
	
	
	public void onCreate() {
		super.onCreate();
		//Parse.enableLocalDatastore(this);
		Parse.initialize(this, "k8qnwj1LoXQdTKvaaDcWXhuh87vxBi0NtQ3amV4E", "E96hssvjI1MY0GuUUzXsqQtL5k4jFW7JnM0JNq3D");
		// Also in this method, specify a default Activity to handle push notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);
		//ParseAnalytics.trackAppOpened(getIntent());
		
		
		}
}
