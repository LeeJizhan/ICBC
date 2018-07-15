# ICBC天气API测试版
## 数据来源
> 使用聚合数据的api接口请求数据
## 数据保存
> 暂时保存到本地的mysql数据库中,目前已经保存到服务器上的mysql数据库。
## 接口介绍
1. getProvinceListFromDisk()
> 获取所有省，返回List
2. getCityMapListFromDisk()
> 获取所有省市的键值对，返回Map
3. getCityListFromDisk(String province)()
> 获取某个省对应的所有城市，返回List
4. getDistrictMapListFromDisk()
> 获取所有市和区县的键值对，返回Map
5. getDistrictListFromDisk(String city)
> 获取某个市对应的所有区县，返回List
## 使用到的框架
1. 网络请求okhttp3
2. Json解析框架Gson
