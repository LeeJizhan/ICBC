package http;

import bean.CityBean;
import bean.FutureWeather;
import bean.TodayWeather;
import bean.WeatherBean;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus- on 2018/7/13.
 */
public class WeatherApi {

    //请求城市url地址
    private static final String requestCityUrl = "http://v.juhe.cn/weather/citys";

    //请求天气url地址
    private static final String requestWeatherUrl = "http://v.juhe.cn/weather/index";

    //请求key
    private static final String key = "d568773f518b3c835a2f487f9cf2181a";

    //请求返回的json格式
    private static final String format = "2";

    private static List<CityBean.Result> mList;

    /**
     * 通过网络请求得到城市列表数据
     *
     * @return
     */
    public List<CityBean.Result> getCityResultFromNet() {
        //拼接url
        String realUrl = requestCityUrl + "?key=" + key;
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
    public String getCityWeatherFromNet(String cityName) {
        String data = null;
        //拼接url
        String realUrl = requestWeatherUrl
                + "?cityname=" + cityName
                + "&format=" + format
                + "&key=" + key;
        //创建okhttp客户端
        OkHttpClient client = new OkHttpClient();
        //创建get请求
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            //得到json数据
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

