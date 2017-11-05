package shoppingapp.android.com.demoapp.data;

import io.realm.RealmObject;

/**
 * Created by Vikrant on 25-10-2017.
 */

public class CityBean extends RealmObject {
    /**
     * id : 1
     * name : Ahmedabad
     */

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
