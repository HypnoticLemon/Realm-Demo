package shoppingapp.android.com.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import io.realm.Realm;
import shoppingapp.android.com.demoapp.RealmController;
import shoppingapp.android.com.demoapp.activity.MainActivity;
import shoppingapp.android.com.demoapp.data.CityBean;
import shoppingapp.android.com.demoapp.data.CityListData;
import shoppingapp.android.com.demoapp.listener.OnRecyclerItemClickListener;
import shoppingapp.android.com.demoapp.R;

/**
 * Created by Vikrant on 22-10-2017.
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {

    private Context context;
    private List<CityBean> cityBeen;
    private ArrayList<CityBean> tempCityBeen;
    private ArrayList<CityBean> temp2CityBeen;
    private int position;
    String TAG = "CityListAdapter";
    private OnRecyclerItemClickListener listener;
    private Realm realm;

    public CityListAdapter(Context context, List<CityBean> cityBeen, int position, OnRecyclerItemClickListener listner) {
        this.context = context;
        this.cityBeen = cityBeen;
        tempCityBeen = new ArrayList<>();
        temp2CityBeen = new ArrayList<>();
        tempCityBeen.addAll(cityBeen);
        temp2CityBeen.addAll(cityBeen);
        this.position = position;
        this.listener = listner;
        realm = Realm.getDefaultInstance();
    }


    @Override
    public CityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_city_list, parent, false);
        CityListViewHolder cityListViewHolder = new CityListViewHolder(view);
        return cityListViewHolder;
    }

    @Override
    public void onBindViewHolder(CityListViewHolder holder, int position) {
        holder.txtCityName.setText(cityBeen.get(position).getId() + ". " + cityBeen.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (null != cityBeen ? cityBeen.size() : 0);
    }

    public void searchString(String newText) {
        newText = newText.toLowerCase(Locale.getDefault());
        cityBeen.clear();
        Log.e(TAG, "searchString: " + newText);
        if (newText.length() == 0) {
            cityBeen.addAll(temp2CityBeen);
        } else {
            for (CityBean model : tempCityBeen) {
                if (model.getName().toLowerCase(Locale.getDefault()).contains(newText)) {
                    cityBeen.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class CityListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCityName;

        public CityListViewHolder(View itemView) {
            super(itemView);
            txtCityName = (TextView) itemView.findViewById(R.id.txtCityName);
        }
    }
}
