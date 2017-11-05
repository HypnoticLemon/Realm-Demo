package shoppingapp.android.com.demoapp.data;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Vikrant on 22-10-2017.
 */

public class CityListData {

    private int status;
    private List<CountriesBean> countries;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CountriesBean> getCountries() {
        return countries;
    }

    public void setCountries(List<CountriesBean> countries) {
        this.countries = countries;
    }

    public static class CountriesBean {

        private int id;
        private String name;
        private String shortcode;
        private int mobile_code;
        private List<CityBean> city;

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

        public String getShortcode() {
            return shortcode;
        }

        public void setShortcode(String shortcode) {
            this.shortcode = shortcode;
        }

        public int getMobile_code() {
            return mobile_code;
        }

        public void setMobile_code(int mobile_code) {
            this.mobile_code = mobile_code;
        }

        public List<CityBean> getCity() {
            return city;
        }

        public void setCity(List<CityBean> city) {
            this.city = city;
        }


    }
}
