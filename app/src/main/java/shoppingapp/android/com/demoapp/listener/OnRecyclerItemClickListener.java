package shoppingapp.android.com.demoapp.listener;

import android.view.View;

/**
 * Created by Vikrant on 22-10-2017.
 */

public interface OnRecyclerItemClickListener {
    void ItemClick(int position, String data, View view, int id);
}
