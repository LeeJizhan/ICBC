package http;

import bean.CityBean;
import com.google.gson.Gson;
import db.DBCon;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Asus- on 2018/7/13.
 */
public class WeatherApi {

    //请求地址
    private static final String requestUrl = "http://v.juhe.cn/weather/citys";
    //请求key
    private static final String key = "d568773f518b3c835a2f487f9cf2181a";

    private static List<CityBean.Result> mList;

    /**
     * 通过网络请求得到城市列表数据
     *
     * @return
     */
    public List<CityBean.Result> getCityResultFromNet() {
        //拼接url
        String realUrl = requestUrl + "?key=" + key;
        //创建okhttp客户端
        OkHttpClient client = new OkHttpClient();
        //创建get请求
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        Response response = null;
        try {
            //请求数据
            response = client.newCall(request).execute();
            String data = response.body().string();
            //解析json
            Gson gson = new Gson();
            CityBean cityBean = gson.fromJson(data, CityBean.class);
            mList = cityBean.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * 通过网络请求获取城市的天气信息
     */
    //public
}

