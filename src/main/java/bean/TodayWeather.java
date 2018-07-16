package bean;

/**
 * Created by Asus- on 2018/7/16.
 */
public class TodayWeather {
    //最低气温
    private String minTemperature;
    //高气温
    private String maxTemperature;
    //天气情况，示例：暴雨
    private String weather;
    //风情况，包括风向和风力，示例：东风3~4级
    private String wind;
    //城市
    private String city;
    //日期
    private String date_y;
    //穿衣建议
    private String dressing_advice;

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
    }

    public String getDressing_advice() {
        return dressing_advice;
    }

    public void setDressing_advice(String dressing_advice) {
        this.dressing_advice = dressing_advice;
    }
}
