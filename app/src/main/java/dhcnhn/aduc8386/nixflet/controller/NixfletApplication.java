package dhcnhn.aduc8386.nixflet.controller;

import android.app.Application;

import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;

public class NixfletApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.init(this);
    }
}
