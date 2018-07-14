package db;

import bean.CityBean;
import http.WeatherApi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Asus- on 2018/7/14.
 */
public class DBOper {

    private WeatherApi weatherApi;
    private List<CityBean.Result> cityResult;
    private DBCon dbCon;

    public DBOper(){
        this.weatherApi = new WeatherApi();
        this.cityResult = weatherApi.getCityResult();
        //1.连接数据库
        this.dbCon = DBCon.getInstance();
    }

    /**
     * 插入城市列表到数据库
     */
    public void updateCityList(){
        /**
         * 将解析得到的Json数据保存到数据库
         * 1.连接数据库
         * 2.创建Statement容器
         * 3.查询数据库数据
         * 4.若数据库为空，插入数据；不为空，不操作
         * 5.遍历更新数据库，插入数据
         *  5.1 获取值
         *  5.2 拼接sql语句
         *  5.3 执行sql语句
         */
        //1.连接数据库
        Connection connection = dbCon.getConnection();
        //2.创建Statement容器
        Statement state= null;   //容器
        try {
            state = connection.createStatement();
            //3.查询数据库数据
            String querySql = "select * from city";
            ResultSet rs = state.executeQuery(querySql);
            //4.若数据库为空，插入数据；不为空，不操作
            if (!rs.next()){
                //5.遍历更新数据库，插入数据
                for (CityBean.Result result : cityResult){
                    //5.1 获取值
                    String id = result.getId();
                    String province = result.getProvince();
                    String city = result.getCity();
                    String district = result.getDistrict();
                    //5.2 拼接sql语句
                    String sql = "insert into city values("
                            + "\'" + id + "\'" + ","
                            + "\'" + province + "\'"+ ","
                            + "\'" + city + "\'" + ","
                            + "\'" + district + "\'"
                            + ")";   //SQL语句
                    //5.3 执行sql语句
                    state.executeUpdate(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
