package com.example.vikrant.demoapplicationjava;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("demo.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

}

