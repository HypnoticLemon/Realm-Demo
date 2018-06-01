package com.example.vikrant.demoapplicationjava;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private String TAG = "MainActivity";
    private Context context = MainActivity.this;
    private Realm realm;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.realm = RealmController.with(this).getRealm();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        if (isNetworkAvailable()) {
            getDataFromAPI();
        } else {
            displayData("Display Data from Database");
        }

    }

    private void getDataFromAPI() {
        progressBar.setVisibility(View.VISIBLE);

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<DataModel>> call = apiService.getModelData();
        call.enqueue(new Callback<List<DataModel>>() {

            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                Log.e(TAG, "onResponse: " + response);
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<DataModel> dataModelList = new ArrayList<>();
                    dataModelList = response.body();
                    List<RealmDataModel> realmDataModelList = new ArrayList<>();

                    for (DataModel dataModel : dataModelList) {
                        RealmDataModel realmDataModel = new RealmDataModel();
                        realmDataModel.setId(dataModel.getId());
                        realmDataModel.setBody(dataModel.getBody());
                        realmDataModel.setTitle(dataModel.getTitle());

                        realmDataModelList.add(realmDataModel);
                    }

                    try {
                        for (RealmDataModel realmDataModel : realmDataModelList) {
                            Log.e(TAG, "onResponse: " + realmDataModel.getTitle());
                            realm.beginTransaction();
                            realm.insertOrUpdate(realmDataModel);
                            realm.commitTransaction();
                        }
                        displayData("Display Data from API");
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private void displayData(String s) {
        if(progressBar.isShown()){
            progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();

        MainAdapter mainAdapter = new MainAdapter(context, RealmController.with(MainActivity.this).getAllDatas());
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();

    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}