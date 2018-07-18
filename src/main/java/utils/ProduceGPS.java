package utils;

/**
 * Created by Asus- on 2018/7/17.
 */

import bean.CarBean;
import db.DBCon;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProduceGPS {

    private static int sigDigits = 8;
    //地球半径
    private static double radiusEarth = 6372.796924d;

    private static double circumKm = 20020.732;

    public static void main(String[] args) throws InterruptedException {
        ProduceGPS produceGPS = new ProduceGPS();
        produceGPS.calculate(20, 34.259516, 109.003784, 0.1);
    }

    public static String padZeroRight(double s) {
        String ss = "" + Math.round(s * Math.pow(10, sigDigits)) / Math.pow(10, sigDigits);
        int i = ss.indexOf(".");
        int d = ss.length() - i - 1;
        if (i == -1) {
            return ss + ".00";
        } else if (d == 1) {
            return ss + "0";
        } else {
            return ss;
        }
    }

    public static double deg(double rd) {
        return rd * 180 / Math.PI;
    }

    public static double normalizeLongitude(double lon) {
        double n = Math.PI;
        if (lon > n) {
            lon = lon - 2 * n;
        } else if (lon < -n) {
            lon = lon + 2 * n;
        }
        return lon;
    }

    public static double rad(double dg) {
        return (dg * Math.PI / 180);
    }

    public void calculate(int p, double startlat, double startlon, double maxdist) throws InterruptedException {
        List<CarBean> rtnList = new ArrayList<CarBean>();
        double finalLat;
        double finalLon;
        double[] brg = new double[]{0, 180, 0};
        int j = 0;
        if (startlat == -90) {
            startlat = -89.99999999;
            j = 2;
        }
        startlat = rad(startlat);
        startlon = rad(startlon);
        double mx = circumKm;
        maxdist = maxdist / radiusEarth;
        double sinstartlat = Math.sin(startlat);
        double cosstartlat = Math.cos(startlat);
        double distance;
        double rad360 = 2 * Math.PI;//圆周率
        //车辆数
        for (int i = 0;i<10;i++){
            String cardID = getCarID();
            if (!rtnList.isEmpty()){
                rtnList.clear();
            }
            //点数
            for (int k = 0; k < p; k++) {
                //模拟车辆停止
                int randomSleepTime = (int) Math.round(Math.random() * 1000);
                Thread.sleep(randomSleepTime);
                //生成0-1的随机数
                double rand1 = new Random().nextDouble();
                distance = Math.acos(rand1 * (Math.cos(maxdist) - 1) + 1);//随机数
                brg[0] = rad360 * new Random().nextDouble();
                //最终点的经纬度
                finalLat = Math.asin(sinstartlat * Math.cos(distance) + cosstartlat * Math.sin(distance) * Math.cos(brg[0]));
                finalLon = deg(normalizeLongitude(startlon * 1 + Math.atan2(Math.sin(brg[0])
                        * Math.sin(distance) * cosstartlat, Math.cos(distance) - sinstartlat * Math.sin(distance))));
                finalLat = deg(finalLat);

                distance = (double) Math.round(distance * radiusEarth * 10000) / 10000.0;
                brg[0] = Math.round(deg(brg[0]) * 1000) / 1000;//随机距离
                String time = getTime();
                CarBean carBean = new CarBean();
                carBean.setCarID(cardID);
                carBean.setTime(time);
                carBean.setLongitude(padZeroRight(finalLon));
                carBean.setLatitude(padZeroRight(finalLat));
                carBean.setBearing(brg[j] + "");
                carBean.setDistance(distance + "");
//            tmpMap.put(LATITUDE, padZeroRight(finalLat));//纬度
//            tmpMap.put(LONGITUDE, padZeroRight(finalLon));//经度
//            tmpMap.put(DISTANCE, distance + "");//距离
//            tmpMap.put(BEARING, brg[j] + "");//bearing--方位（0-360度）
//            System.out.println("cardID = " + carBean.getCardID());
//            System.out.println("time = " + carBean.getTime());
//            System.out.println("lon = " + carBean.getLongitude());
//            System.out.println("lat = " + carBean.getLatitude());
//            System.out.println("bearing = " + carBean.getBearing());
//            System.out.println("distance = " + carBean.getDistance());
//            System.out.println("-------------------------------------");
//            System.out.println("-------------------------------------");
                rtnList.add(carBean);
            }
            updateDataToDB(rtnList);
        }
    }

    public static String getTime() {
        Date now = new Date();
        //设置日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(now);
        return time;
    }

    public static String getCarID() {
        String carID = Math.round(Math.random() * 1000) + "";
        return carID;
    }

    /**
     * 写入数据库
     * @param list
     */
    public void updateDataToDB(List<CarBean> list) {
        DBCon dbCon = DBCon.getInstance();
        Connection connection = dbCon.getConnection();
        try {
            Statement statement = connection.createStatement();
            List<CarBean> carList = list;
            for (CarBean carBean : carList) {
                //拼接SQL语句
                String updateSql = "insert into gps values("
                        + "\'" + carBean.getCarID() + "\'" + ","
                        + "\'" + carBean.getTime() + "\'" + ","
                        + "\'" + carBean.getLongitude() + "\'" + ","
                        + "\'" + carBean.getLatitude() + "\'" + ","
                        + "\'" + carBean.getBearing() + "\'" + ","
                        + "\'" + carBean.getDistance() + "\'"
                        + ")";
                statement.executeUpdate(updateSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}