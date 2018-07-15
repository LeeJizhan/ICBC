package utils;

import bean.CityBean;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Asus- on 2018/7/13.
 */

/**
 * 封装的Gson解析工具类，提供泛型参数
 */
public class GsonUtil {
    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    // 将Json数组解析成相应的映射对象列表
    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
                                                     Class<T> type) {
        Gson gson = new Gson();
        CityBean cityBean = (CityBean) gson.fromJson(jsonData,type);
        //对象中拿到集合
        List<CityBean.Result> cityLists = cityBean.getResult();
        System.out.println(cityLists);
//        for (CityBean.City c : cityLists){
//            System.out.println(c.getCity());
//        }
        return (List<T>) cityLists;
    }
}
