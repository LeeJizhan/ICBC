package bean;

/**
 * Created by Asus- on 2018/7/16.
 */
public class FutureWeather {
    //温度
    private String temperature;
    //天气情况
    private String weather;

    //天气图片id
    private WeatherID weather_id;
    //风情况
    private String wind;
    //星期几
    private String week;
    //日期
    private String date;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public WeatherID getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(WeatherID weather_id) {
        this.weather_id = weather_id;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private class WeatherID {
        private String fa;
        private String fb;

        public String getFa() {
            return fa;
        }

        public void setFa(String fa) {
            this.fa = fa;
        }

        public String getFb() {
            return fb;
        }

        public void setFb(String fb) {
            this.fb = fb;
        }
    }
}