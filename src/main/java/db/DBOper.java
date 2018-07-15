package db;

import bean.CityBean;
import http.WeatherApi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Asus- on 2018/7/14.
 */
public class DBOper {

    private WeatherApi weatherApi;
    private List<CityBean.Result> cityResult;
    private DBCon dbCon;
    private Connection connection;

    public DBOper() {
        this.weatherApi = new WeatherApi();
        this.dbCon = DBCon.getInstance();
        this.connection = dbCon.getConnection();
    }

    /**
     * 插入城市列表到数据库
     */
    public void updateCityList() {
        /**
         * 将解析得到的Json数据保存到数据库
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         * 4.若数据库为空，从网络上请求数据，插入数据；不为空，不操作
         * 5.遍历更新数据库，插入数据
         *  5.1 获取值
         *  5.2 拼接sql语句
         *  5.3 执行sql语句
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        //2.创建Statement容器
        Statement state = null;   //容器
        try {
            state = connection.createStatement();
            //3.查询数据库数据
            String querySql = "select * from city";
            ResultSet rs = state.executeQuery(querySql);
            //4.若数据库为空，从网络上请求数据，插入数据；不为空，不操作
            if (!rs.next()) {
                //请求网络数据
                cityResult = weatherApi.getCityResultFromNet();
                //5.遍历更新数据库，插入数据
                for (CityBean.Result result : cityResult) {
                    //5.1 获取值
                    String id = result.getId();
                    String province = result.getProvince();
                    String city = result.getCity();
                    String district = result.getDistrict();
                    //5.2 拼接sql语句
                    String sql = "insert into city values("
                            + "\'" + id + "\'" + ","
                            + "\'" + province + "\'" + ","
                            + "\'" + city + "\'" + ","
                            + "\'" + district + "\'"
                            + ")";   //SQL语句
                    //5.3 执行sql语句
                    state.executeUpdate(sql);
                }
            } else {
                System.out.println("city表中已经存在数据.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从数据库中获取省列表数据
     */
    public List<String> getProvinceListFromDisk() {
        List<String> provinceList = new ArrayList<String>();
        /**
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         * 4.根据返回的ResultSet保存数据到provinceList中
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        Statement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.createStatement();
            //3.查询数据库数据,distinct去重
            String querySql = "select DISTINCT province from city";
            ResultSet resultSet = statement.executeQuery(querySql);
            //4.根据返回的ResultSet保存数据到List中
            while (resultSet.next()) {
                provinceList.add(resultSet.getString("province"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return provinceList;
    }

    /**
     * 从数据库中获取所有省对应的城市列表数据
     * 为了方便根据省级目录得到市级目录
     *
     * @return 返回省市的键值对
     */
    public Map<String, List<String>> getCityMapListFromDisk() {
        Map<String, List<String>> cityMap = new HashMap<String, List<String>>();
        List<String> provinceList;
        /**
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         *  3.1 根据省的列表拼接sql语句
         *  3.2 执行sql查询语句
         *  3.3 根据返回的ResultSet保存数据到cityList中
         * 4.将cityList中的数据保存到cityMap
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        Statement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.createStatement();
            provinceList = getProvinceListFromDisk();
            //3.查询数据库数据
            for (String p : provinceList) {
                List<String> cityList = new ArrayList<String>();
                String querySql = "select DISTINCT city from city WHERE province = "
                        + "\'" + p + "\'";
                ResultSet resultSet = statement.executeQuery(querySql);
                while (resultSet.next()) {
                    cityList.add(resultSet.getString("city"));
                }
                cityMap.put(p, cityList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityMap;
    }

    /**
     * 从数据库中获取某个省的城市列表数据
     * 为了方便根据省级目录得到市级目录
     *
     * @return 返回对应省的所有城市列表
     */
    public List<String> getCityListFromDisk(String province) {
        List<String> cityList = new ArrayList<String>();
        /**
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         *  3.1 根据省的名称拼接sql语句
         *  3.2 执行sql查询语句
         *  3.3 根据返回的ResultSet保存数据到cityList中
         * 4.返回cityList
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        Statement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.createStatement();
            List<String> provinceList = getProvinceListFromDisk();
            if (province != null && provinceList.contains(province)){
                //3.查询数据库数据
                String querySql = "select DISTINCT city from city WHERE province = "
                        + "\'" + province + "\'";
                ResultSet resultSet = statement.executeQuery(querySql);
                while (resultSet.next()) {
                    cityList.add(resultSet.getString("city"));
                }
            } else {
                throw new Exception("不存在这个省!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //4.返回cityList
        return cityList;
    }

    /**
     * 从数据库中获取所有城市对应区的列表数据
     *
     * @return 返回市和区的键值对
     */
    public Map<String, List<String>> getDistrictMapListFromDisk() {
        Map<String, List<String>> districtMap = new HashMap<String, List<String>>();
        List<String> cityList;
        /**
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         * 4.返回districtMap
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        Statement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.createStatement();
            List<String> provinceList = getProvinceListFromDisk();
            //3.查询数据库数据
            for (String p : provinceList) {
                cityList = getCityListFromDisk(p);
                for (String c : cityList) {
                    List<String> districtList = new ArrayList<String>();
                    String querySql = "select district from city WHERE city = "
                            + "\'" + c + "\'";
                    ResultSet resultSet = statement.executeQuery(querySql);
                    while (resultSet.next()) {
                        districtList.add(resultSet.getString("district"));
                    }
                    districtMap.put(c, districtList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return districtMap;
    }

    /**
     * 从数据库中获取某个市的区县列表数据
     * 为了方便根据市级目录得到区县级目录
     *
     * @return 返回对应城市的所有区县列表
     */
    public List<String> getDistrictListFromDisk(String city) {
        List<String> districtList = new ArrayList<String>();
        List<String> allCityList = new ArrayList<String>();
        /**
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         *  3.1 根据市的名称拼接sql语句
         *  3.2 执行sql查询语句
         *  3.3 根据返回的ResultSet保存数据到districtList中
         * 4.返回districtList
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        Statement statement = null;
        try {
            List<String> provinceList = getProvinceListFromDisk();
            //3.查询数据库数据
            for (String p : provinceList) {
                List<String> cityList = getCityListFromDisk(p);
                for (String c : cityList){
                    allCityList.add(c);
                }
            }
            //2.创建Statement容器
            statement = connection.createStatement();

            if (city != null && allCityList.contains(city)){
                //3.查询数据库数据
                String querySql = "select district from city WHERE city = "
                        + "\'" + city + "\'";
                ResultSet resultSet = statement.executeQuery(querySql);
                while (resultSet.next()) {
                    districtList.add(resultSet.getString("district"));
                }
            }else {
                throw new Exception("不存在这个城市!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //4.返回districtList
        return districtList;
    }
}
