package bean;

import java.util.List;

/**
 * Created by Asus- on 2018/7/16.
 */
public class WeatherBean {
    private SK sk;

    private TodayWeather todayWeather;

    private List<FutureWeather> futureWeatherList;

    public class TodayWeather {
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

        public String getWeather_id() {
            return weather_id;
        }

        public void setWeather_id(String weather_id) {
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

        public String getDressing_index() {
            return dressing_index;
        }

        public void setDressing_index(String dressing_index) {
            this.dressing_index = dressing_index;
        }

        public String getDressing_advice() {
            return dressing_advice;
        }

        public void setDressing_advice(String dressing_advice) {
            this.dressing_advice = dressing_advice;
        }

        public String getUv_index() {
            return uv_index;
        }

        public void setUv_index(String uv_index) {
            this.uv_index = uv_index;
        }

        public String getComfort_index() {
            return comfort_index;
        }

        public void setComfort_index(String comfort_index) {
            this.comfort_index = comfort_index;
        }

        public String getWash_index() {
            return wash_index;
        }

        public void setWash_index(String wash_index) {
            this.wash_index = wash_index;
        }

        public String getTravel_index() {
            return travel_index;
        }

        public void setTravel_index(String travel_index) {
            this.travel_index = travel_index;
        }

        public String getExercise_index() {
            return exercise_index;
        }

        public void setExercise_index(String exercise_index) {
            this.exercise_index = exercise_index;
        }

        public String getDrying_index() {
            return drying_index;
        }

        public void setDrying_index(String drying_index) {
            this.drying_index = drying_index;
        }

        //温度min ~ max，示例：23℃~25℃
        private String temperature;
        //天气情况，示例：暴雨
        private String weather;
        //天气图片ID
        private String weather_id;
        //风情况，包括风向和风力，示例：东风3~4级
        private String wind;
        //星期几
        private String week;
        //城市
        private String city;
        //日期
        private String date_y;
        //穿衣指数
        private String dressing_index;
        //穿衣建议
        private String dressing_advice;
        //紫外线强度
        private String uv_index;
        //舒适度
        private String comfort_index;
        //洗车指数
        private String wash_index;
        //旅游指数
        private String travel_index;
        //运动指数
        private String exercise_index;
        //干燥指数
        private String drying_index;
    }

    public class SK {
        //温度数值，注意：没带单位
        private String temp;
        //风向
        private String wind_direction;

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getWind_direction() {
            return wind_direction;
        }

        public void setWind_direction(String wind_direction) {
            this.wind_direction = wind_direction;
        }

        public String getWind_strength() {
            return wind_strength;
        }

        public void setWind_strength(String wind_strength) {
            this.wind_strength = wind_strength;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        //风力
        private String wind_strength;
        //湿度
        private String humidity;
        //更新时间
        private String time;
    }

}
