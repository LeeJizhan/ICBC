package db;

import bean.CityBean;
import bean.FutureWeather;
import bean.TodayWeather;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import http.WeatherApi;

import java.sql.*;
import java.util.*;

/**
 * Created by Asus- on 2018/7/14.
 */
public class DBOper {

    private WeatherApi weatherApi;
    private List<CityBean.Result> cityResult;
    private DBCon dbCon;
    private Connection connection;
    private TodayWeather todayWeather = new TodayWeather();
    private List<FutureWeather> futureWeatherList = new ArrayList<FutureWeather>();

    public DBOper() {
        this.weatherApi = new WeatherApi();
        this.dbCon = DBCon.getInstance();
        this.connection = dbCon.getConnection();
    }

    /**
     * 插入今日天气和未来天气数据到数据库
     */
    public void updateWeather() throws SQLException {
        /**
         * 将今日天气和未来天气数据到数据库
         * 1.连接数据库
         * 2.创建PreparedStatement容器
         * 3.查询数据库数据
         * 4.判断是否是今天的天气数据，若是，不更新；若不是，从网络上获取更新，更新数据库
         * 5.更新数据库
         *   1)获取主要城市的列表
         *   2)遍历列表进行数据请求
         *   3)对请求得到的数据进行解析
         *   4)获取到今天天气和未来天气数据
         *   5)将解析得到的数据放到数据库中
         * 6.关闭数据库的连接
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        connection.setAutoCommit(false);
        //2.创建Statement容器
        PreparedStatement preparedStatement = connection.prepareStatement("");
        //3.获取数据库中的日期
        String querySql = "SELECT DISTINCT daytime FROM todayweather";
        ResultSet resultSet = preparedStatement.executeQuery(querySql);
        //获取当前的日期
        Calendar now = Calendar.getInstance();
        String year = now.get(Calendar.YEAR) + "年";
        String month;
        if ((now.get(Calendar.MONTH) + 1) < 10 && (now.get(Calendar.MONTH) + 1) > 0) {
            month = "0" + (now.get(Calendar.MONTH) + 1) + "月";
        } else {
            month = (now.get(Calendar.MONTH) + 1) + "月";
        }
        String day = now.get(Calendar.DAY_OF_MONTH) + "日";
        String today = year + month + day;

        boolean isUpdate = true;
        //判断数据库中是否含有当前天的天气信息，包括今天天气和未来天气数据
        while (resultSet.next()) {
            String daytime = resultSet.getString("daytime");
            if (daytime.contains(today)) {
                isUpdate = false;
                break;
            }
        }
        //没有当前天的数据，需要联网更新
        if (isUpdate) {
            //获取所有城市
            //String getAllCitySql = "SELECT DISTINCT city FROM city";
            //ResultSet cityResultSet = statement.executeQuery(getAllCitySql);
            //获取所有区县
            //String getAllDistrictSql = "SELECT district FROM city";
            //ResultSet districtResultSet = statement.executeQuery(getAllDistrictSql);
            //获取主要城市列表

            //API调用次数不够，暂时只做以下5个城市的数据。
            List<String> cities = new ArrayList<String>();
            cities.add("珠海");
            cities.add("北京");
            cities.add("上海");
            cities.add("广州");
            cities.add("深圳");
            String preSqlToday = "insert into todayweather(city,daytime,mintemp,maxtemp,weather,wind,description) values";
            String preSqlFuture = "insert into futureweather(city,daytime,mintemp,maxtemp,todaytime,wind,weather) values";
            StringBuffer sbToday = new StringBuffer();
            for (String c : cities) {
                String data = weatherApi.getCityWeatherFromNet(c);
                //解析数据
                parsingJson(data);
                //开始插入数据到数据库
                //插入今日天气数据
                //1) 拼接sql语句
                //天气
                String weather = todayWeather.getWeather();
                //城市
                String city = todayWeather.getCity();
                //最低温度
                String minTemperature = todayWeather.getMinTemperature();
                //最高温度
                String maxTemperature = todayWeather.getMaxTemperature();
                //风
                String wind = todayWeather.getWind();
                //当前日期
                String date_y = todayWeather.getDate_y();
                //穿衣建议
                String dressing_advice = todayWeather.getDressing_advice();
                //SQL语句
                String todaySql = "("
                        + "\'" + city + "\'" + ","
                        + "\'" + date_y + "\'" + ","
                        + "\'" + minTemperature + "\'" + ","
                        + "\'" + maxTemperature + "\'" + ","
                        + "\'" + weather + "\'" + ","
                        + "\'" + wind + "\'" + ","
                        + "\'" + dressing_advice + "\'"
                        + "),";
                //2)添加sql语句
                sbToday.append(todaySql);
                //插入未来天气数据
                //遍历七天天气
                StringBuffer sbFuture = new StringBuffer();
                for (FutureWeather futureWeather : futureWeatherList) {
                    //当前日期
                    //天气
                    String fWeather = futureWeather.getWeather();
                    //未来日期
                    String fDay = futureWeather.getDate();
                    //温度
                    String fTemp = futureWeather.getTemperature();
//                    //星期几
//                    String week = futureWeather.getWeek();
                    //风
                    String fWind = futureWeather.getWind();
                    String[] fTemps = fTemp.split("~");
                    //最低气温
                    String fMinTemp = fTemps[0];
                    //最高气温
                    String fMaxTemp = fTemps[1];
                    //1)拼接sql语句
                    String futureSql = "("
                            + "\'" + city + "\'" + ","
                            + "\'" + fDay + "\'" + ","
                            + "\'" + fMinTemp + "\'" + ","
                            + "\'" + fMaxTemp + "\'" + ","
                            + "\'" + date_y + "\'" + ","
                            + "\'" + fWind + "\'" + ","
                            + "\'" + fWeather + "\'"
                            + "),";   //SQL语句
                    //2)添加sql语句
                    sbFuture.append(futureSql);
                }
                String futureSql = preSqlFuture + sbFuture.substring(0, sbFuture.length() - 1);
                preparedStatement.addBatch(futureSql);
                //preparedStatement.executeBatch();
            }
            String todaySql = preSqlToday + sbToday.substring(0, sbToday.length() - 1);
            preparedStatement.addBatch(todaySql);
            preparedStatement.executeBatch();
            connection.commit();
            preparedStatement.close();
            System.out.println("天气信息更新成功!");
        } else {
            System.out.println("已经有当天的天气信息，请放心使用。");
        }
    }

    /**
     * 解析Json数据
     *
     * @param data
     */
    private void parsingJson(String data) {

        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        int errorCode = jsonObject.get("error_code").getAsInt();
        if (errorCode == 0) {
            JsonObject jsonResult = jsonObject.getAsJsonObject("result");
//            //实时天气
//            JsonObject skJson = jsonResult.getAsJsonObject("sk");
//            String temp = skJson.get("temp").getAsString();
//            System.out.println("temp = " + temp);
            //今日天气json
            JsonObject todayJson = jsonResult.getAsJsonObject("today");
            //天气
            String weather = todayJson.get("weather").getAsString();
            //城市
            String city = todayJson.get("city").getAsString();
            //温度
            String temperature = todayJson.get("temperature").getAsString();
            //风
            String wind = todayJson.get("wind").getAsString();
            //日期
            String date_y = todayJson.get("date_y").getAsString();
            //穿衣建议
            String dressing_advice = todayJson.get("dressing_advice").getAsString();
            String[] temps = temperature.split("~");
            //最低气温
            String minTemp = temps[0];
            //最高气温
            String maxTemp = temps[1];
            todayWeather.setWeather(weather);
            todayWeather.setCity(city);
            todayWeather.setDate_y(date_y);
            todayWeather.setMinTemperature(minTemp);
            todayWeather.setMaxTemperature(maxTemp);
            todayWeather.setWind(wind);
            todayWeather.setDressing_advice(dressing_advice);
            //未来天气json
            JsonArray jsonFutureElements = jsonResult.getAsJsonArray("future");
//            System.out.println(jsonFutureElements);
            //如果列表不为空，先清空
            if (!futureWeatherList.isEmpty()) {
                futureWeatherList.clear();
            }
            //循环遍历
            for (JsonElement futureWeatherEle : jsonFutureElements) {
                //通过反射 得到FutureWeather.class
                FutureWeather futureWeather = new Gson().fromJson(futureWeatherEle, new TypeToken<FutureWeather>() {
                }.getType());
                futureWeatherList.add(futureWeather);
            }
        } else {
            System.out.println("查询失败!");
        }
    }

    /**
     * 插入城市列表到数据库
     */
    public void updateCityList() {
        /**
         * 将解析得到的Json数据保存到数据库
         * 1.连接数据库
         * 2.创建PreparedStatement容器
         * 3.查询数据库数据
         * 4.若数据库为空，从网络上请求数据，插入数据；不为空，不操作
         * 5.遍历更新数据库，插入数据
         *  5.1 获取值
         *  5.2 拼接sql语句
         *  5.3 执行sql语句
         * 6.关闭数据库的连接
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        //2.创建Statement容器
        PreparedStatement state = null;   //容器
        try {
            state = connection.prepareStatement("");
            //3.查询数据库数据
            String querySql = "select * from city";
            ResultSet rs = state.executeQuery(querySql);
            String preSqlCity = "insert into city(id,province,city,district) values";
            //4.若数据库为空，从网络上请求数据，插入数据；不为空，不操作
            if (!rs.next()) {
                //请求网络数据
                cityResult = weatherApi.getCityResultFromNet();
                StringBuilder sbCity = new StringBuilder();
                //5.遍历更新数据库，插入数据
                for (CityBean.Result result : cityResult) {
                    //5.1 获取值
                    String id = result.getId();
                    String province = result.getProvince();
                    String city = result.getCity();
                    String district = result.getDistrict();
                    //5.2 拼接sql语句
                    String sql = "("
                            + "\'" + id + "\'" + ","
                            + "\'" + province + "\'" + ","
                            + "\'" + city + "\'" + ","
                            + "\'" + district + "\'"
                            + "),";   //SQL语句
                    //5.3 添加sql语句
                    sbCity.append(sql);
                }
                String citySql = preSqlCity + sbCity.substring(0, sbCity.length() - 1);
                state.addBatch(citySql);
                state.executeBatch();
                connection.commit();
                state.close();
            } else {
                System.out.println("city表中已经存在数据.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
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
         * 5.关闭数据库的连接
         * 6.返回provinceList
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
        try {
            connection.close();
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
         * 5.关闭数据库连接
         * 6.返回cityMap
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        PreparedStatement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.prepareStatement("");
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
        try {
            connection.close();
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
         * 4.关闭数据库连接
         * 5.返回cityList
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        PreparedStatement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.prepareStatement("");
            List<String> provinceList = getProvinceListFromDisk();
            if (province != null && provinceList.contains(province)) {
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
        try {
            connection.close();
        } catch (SQLException e) {
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
         * 4.关闭数据库连接
         * 5.返回districtMap
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        PreparedStatement statement = null;
        try {
            //2.创建Statement容器
            statement = connection.prepareStatement("");
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
        try {
            connection.close();
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
         * 4.关闭数据库连接
         * 5.返回districtList
         *
         */
        //1.连接数据库
//        Connection connection = dbCon.getConnection();
        PreparedStatement statement = null;
        try {
            List<String> provinceList = getProvinceListFromDisk();
            //3.查询数据库数据
            for (String p : provinceList) {
                List<String> cityList = getCityListFromDisk(p);
                for (String c : cityList) {
                    allCityList.add(c);
                }
            }
            //2.创建Statement容器
            statement = connection.prepareStatement("");

            if (city != null && allCityList.contains(city)) {
                //3.查询数据库数据
                String querySql = "select district from city WHERE city = "
                        + "\'" + city + "\'";
                ResultSet resultSet = statement.executeQuery(querySql);
                while (resultSet.next()) {
                    districtList.add(resultSet.getString("district"));
                }
            } else {
                throw new Exception("不存在这个城市!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //4.返回districtList
        return districtList;
    }
}