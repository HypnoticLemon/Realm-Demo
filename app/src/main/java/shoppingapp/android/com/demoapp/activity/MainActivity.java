package shoppingapp.android.com.demoapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shoppingapp.android.com.demoapp.AppController;
import shoppingapp.android.com.demoapp.RealmController;
import shoppingapp.android.com.demoapp.data.CityBean;
import shoppingapp.android.com.demoapp.listener.OnRecyclerItemClickListener;
import shoppingapp.android.com.demoapp.R;
import shoppingapp.android.com.demoapp.adapter.CityListAdapter;
import shoppingapp.android.com.demoapp.data.CityListData;
import shoppingapp.android.com.demoapp.network.ApiClient;
import shoppingapp.android.com.demoapp.network.ApiInterface;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView viewCityList;
    private ProgressBar progressBar;
    private CityListAdapter cityListAdapter;
    private List<CityListData> cityListData;
    private String TAG = "MainActivity";
    private List<CityListData.CountriesBean> countriesBeen;
    private List<CityBean> cityBeen;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.realm = RealmController.with(this).getRealm();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        viewCityList = (RecyclerView) findViewById(R.id.viewCityList);
        viewCityList.setHasFixedSize(true);
        viewCityList.setLayoutManager(new LinearLayoutManager(this));
        cityBeen = new ArrayList<>();

        cityListAdapter = new CityListAdapter(this, cityBeen, 0, listner);
        viewCityList.setAdapter(cityListAdapter);

        // getCityList();

        volleyCall();

        generateNoteOnSD(this, "TestFile", "just test file");
    }

    private void volleyCall() {
        String url = "http://vikrantshah.5gbfree.com/city_name_list.json";
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            Log.e(TAG, "onCreate: from cache");
            try {
                String response = new String(entry.data, "UTF-8");

                try {
                    countriesBeen = new ArrayList<CityListData.CountriesBean>();
                    JSONObject responseObj = new JSONObject(response);

                    if (responseObj.getInt("status") == 1) {
                        JSONArray jsonArray = responseObj.getJSONArray("countries");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONArray cityArray = jsonObject.getJSONArray("city");
                            for (int j = 0; j < cityArray.length(); j++) {
                                JSONObject cityObject = cityArray.getJSONObject(j);
                                CityBean cityBean = new CityBean();
                                cityBean.setName(cityObject.getString("name"));
                                cityBean.setId(cityObject.getInt("id"));
                                cityBeen.add(cityBean);
                            }
                        }
                    }

                    cityListAdapter = new CityListAdapter(MainActivity.this, cityBeen, 0, listner);
                    viewCityList.setAdapter(cityListAdapter);
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "onCreate: Network call");
            if (isInternetAvailabel(this)) {
                getCityListFromVolley(url);
            } else {
                Log.e(TAG, "volleyCall: getting from realm");
                RealmController.with(this).refresh();
                RealmResults<CityBean> cityBeanRealmResults = realm.where(CityBean.class).findAll();
                for (int i = 0; i < cityBeanRealmResults.size(); i++) {
                    CityBean bean = new CityBean();
                    bean.setId(cityBeanRealmResults.get(i).getId());
                    bean.setName(cityBeanRealmResults.get(i).getName());
                    cityBeen.add(bean);
                }
                cityListAdapter = new CityListAdapter(MainActivity.this, cityBeen, 0, listner);
                viewCityList.setAdapter(cityListAdapter);
            }
        }
    }

    private void getCityListFromVolley(String url) {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    countriesBeen = new ArrayList<CityListData.CountriesBean>();
                    JSONObject responseObj = new JSONObject(response);

                    if (responseObj.getInt("status") == 1) {
                        JSONArray jsonArray = responseObj.getJSONArray("countries");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONArray cityArray = jsonObject.getJSONArray("city");
                            for (int j = 0; j < cityArray.length(); j++) {
                                JSONObject cityObject = cityArray.getJSONObject(j);
                                CityBean cityBean = new CityBean();
                                cityBean.setName(cityObject.getString("name"));
                                cityBean.setId(cityObject.getInt("id"));
                                realm.beginTransaction();
                                realm.copyToRealm(cityBean);
                                realm.commitTransaction();
                                cityBeen.add(cityBean);
                            }
                        }
                    }

                    cityListAdapter = new CityListAdapter(MainActivity.this, cityBeen, 0, listner);
                    viewCityList.setAdapter(cityListAdapter);
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onErrorResponse: " + volleyError.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request, "asdf");
    }


    private void getCityList() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<CityListData> call = apiService.getCityList();

        call.enqueue(new Callback<CityListData>() {
            @Override
            public void onResponse(Call<CityListData> call, Response<CityListData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        if (response.body().getCountries().size() > 0) {
                            countriesBeen = new ArrayList<CityListData.CountriesBean>();
                            cityBeen = new ArrayList<CityBean>();
                            countriesBeen = response.body().getCountries();
                            for (int i = 0; i < countriesBeen.size(); i++) {
                                cityBeen.addAll(countriesBeen.get(i).getCity());
                            }

                            cityListAdapter = new CityListAdapter(MainActivity.this, cityBeen, 0, listner);
                            viewCityList.setAdapter(cityListAdapter);

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CityListData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private OnRecyclerItemClickListener listner = new OnRecyclerItemClickListener() {
        @Override
        public void ItemClick(int position, String data, View view, int id) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (cityBeen.size() > 0) {
            cityListAdapter.searchString(newText);
        }
        RealmQuery<CityBean> cityBeanRealmQuery = realm.where(CityBean.class).equalTo("id", newText);
        return true;
    }

    public static boolean isInternetAvailabel(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        //File file =

        String Local_File_Path = Environment.getExternalStorageDirectory() + "/mycc/" + "SampleDOCFile_100kb";
        File mFile = new File(Local_File_Path);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Vikrant");
            jsonObject.put("number", "9033444709");
            jsonObject.put("file", mFile);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e(TAG, "generateNoteOnSD: " + jsonObject.toString());
    }
}
