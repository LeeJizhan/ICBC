package http;

import bean.CityBean;
import bean.FutureWeather;
import bean.WeatherBean;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
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
    public void getCityWeatherFromNet(String cityName) {
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
        Response response = null;
        try {
            response = client.newCall(request).execute();
            //得到json数据
            String data = response.body().string();
            //解析json
            parsingJson(data);
        } catch (IOException e) {
            e.printStackTrace();
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
        System.out.println("errorCode = " + errorCode);
        if (errorCode == 0) {
            JsonObject jsonResult = jsonObject.getAsJsonObject("result");
            //实时天气
            JsonObject skJson = jsonResult.getAsJsonObject("sk");
            String temp = skJson.get("temp").getAsString();
            System.out.println("temp = " + temp);
            //今日天气
            JsonObject todayJson = jsonResult.getAsJsonObject("today");
            String weather = todayJson.get("weather").getAsString();
            System.out.println("weather = " + weather);
            //未来天气
            JsonArray jsonFutureElements = jsonResult.getAsJsonArray("future");
//            System.out.println(jsonFutureElements);
            //循环遍历
            for (JsonElement futureWeatherEle : jsonFutureElements) {
                //通过反射 得到UserBean.class
                FutureWeather futureWeather = new Gson().fromJson(futureWeatherEle, new TypeToken<FutureWeather>() {
                }.getType());
                System.out.println("futureWeather weather = " + futureWeather.getWeather());
            }
        } else {
            System.out.println("查询失败!");
        }

    }

}

