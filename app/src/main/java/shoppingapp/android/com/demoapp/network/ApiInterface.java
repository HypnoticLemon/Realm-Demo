package shoppingapp.android.com.demoapp.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import shoppingapp.android.com.demoapp.data.CityListData;

/**
 * Created by Vikrant on 22-10-2017.
 */

public interface ApiInterface {


    @POST("city_name_list.json")
    Call<CityListData> getCityList();
}
