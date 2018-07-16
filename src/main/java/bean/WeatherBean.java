package bean;

import java.util.List;

/**
 * Created by Asus- on 2018/7/16.
 */
public class WeatherBean {
    private SK sk;

    private TodayWeather todayWeather;

    private List<FutureWeather> futureWeatherList;

    public SK getSk() {
        return sk;
    }

    public void setSk(SK sk) {
        this.sk = sk;
    }

    public TodayWeather getTodayWeather() {
        return todayWeather;
    }

    public void setTodayWeather(TodayWeather todayWeather) {
        this.todayWeather = todayWeather;
    }

    public List<FutureWeather> getFutureWeatherList() {
        return futureWeatherList;
    }

    public void setFutureWeatherList(List<FutureWeather> futureWeatherList) {
        this.futureWeatherList = futureWeatherList;
    }

}
