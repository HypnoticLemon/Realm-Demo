package com.example.vikrant.demoapplicationjava;


import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        if(!realm.isAutoRefresh())
        realm.setAutoRefresh(true);

    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        //realm.clear(RealmDataModel.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<RealmDataModel> getAllDatas() {

        return realm.where(RealmDataModel.class).findAll();
    }

    //query a single item with the given id
    public RealmDataModel getDatas(int id) {

        return realm.where(RealmDataModel.class).equalTo("id", id).findFirst();
    }

    //check if Data is empty
    /*public boolean hasData() {

      //  return !realm.allObjects(RealmDataModel.class).isEmpty();
    }*/

    //query example
   /* public RealmResults<DataModel> queryedBooks() {

        return realm.where(DataModel.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }*/
}
