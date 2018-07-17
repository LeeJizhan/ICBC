import db.DBOper;
import http.WeatherApi;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Asus- on 2018/7/14.
 */
public class Main {
    //    int count = 0;
    public static void main(String[] args) {
//        Main m = new Main();
//        WeatherApi weatherApi = new WeatherApi();
//        weatherApi.getCityWeatherFromNet("珠海");

        Runnable runnable = new Runnable() {
            public void run() {
                DBOper dbOper = new DBOper();
                try {
                    dbOper.updateWeather();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        //定时任务
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //从执行开始的时间起，24小时后更新，initialDelay表示延迟1s后开始第一次执行
        service.scheduleAtFixedRate(runnable, 1, 3600 * 24, TimeUnit.SECONDS);

//        Map<String, List<String>> cityMap = dbOper.getCityMapListFromDisk();
//        Map<String, List<String>> districtMap = dbOper.getDistrictMapListFromDisk();
//        m.printMap(cityMap);
//        m.printMap(districtMap);

//        List<String> cityList = dbOper.getCityListFromDisk("广东");
//        List<String> districtList = dbOper.getDistrictListFromDisk("珠海");
//        m.printList(cityList);
//        m.printList(districtList);
    }

//    private void printList(List<String> cityList) {
//        for (String s : cityList){
//            System.out.println(s);
//        }
//        System.out.println("---------------------------------------------------");
//        System.out.println("---------------------------------------------------");
//        System.out.println("---------------------------------------------------");
//    }
//
//    private void printMap(Map<String,List<String>> map){
//        for (String s : map.keySet()) {
//            List<String> list = map.get(s);
//            count += list.size();
//            for (String ss : list){
//                System.out.println(ss);
//            }
//        }
//        System.out.println(map.keySet().size());
//        System.out.println(count);
//        System.out.println("---------------------------------------------------");
//        System.out.println("---------------------------------------------------");
//        System.out.println("---------------------------------------------------");
//        count = 0;
//    }
}
