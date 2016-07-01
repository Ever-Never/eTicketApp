package com.icfcc.example.Util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class AppContext extends Application {
	private static AppContext instance;

	public static Context getInstance() {
		return instance;
	}

	public static Resources getAppResources() {
		return getAppResources();
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
