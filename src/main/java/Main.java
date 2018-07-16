import db.DBOper;
import http.WeatherApi;

import java.util.List;
import java.util.Map;

/**
 * Created by Asus- on 2018/7/14.
 */
public class Main {
    int count = 0;
    public static void main(String[] args) {
        Main m = new Main();
        WeatherApi weatherApi = new WeatherApi();
        weatherApi.getCityWeatherFromNet("珠海");
        //DBOper dbOper = new DBOper();

//        Map<String, List<String>> cityMap = dbOper.getCityMapListFromDisk();
//        Map<String, List<String>> districtMap = dbOper.getDistrictMapListFromDisk();
//        m.printMap(cityMap);
//        m.printMap(districtMap);

//        List<String> cityList = dbOper.getCityListFromDisk("广东");
//        List<String> districtList = dbOper.getDistrictListFromDisk("珠海");
//        m.printList(cityList);
//        m.printList(districtList);
    }

    private void printList(List<String> cityList) {
        for (String s : cityList){
            System.out.println(s);
        }
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
    }

    private void printMap(Map<String,List<String>> map){
        for (String s : map.keySet()) {
            List<String> list = map.get(s);
            count += list.size();
            for (String ss : list){
                System.out.println(ss);
            }
        }
        System.out.println(map.keySet().size());
        System.out.println(count);
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
        count = 0;
    }
}
