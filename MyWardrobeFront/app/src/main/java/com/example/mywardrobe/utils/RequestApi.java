package com.example.mywardrobe.utils;

public class RequestApi {
    /**
     *  Base url
     *  TODO: BASE_URL
     */
    //public static final String BASE_URL = "http://192.168.1.103:8080";
    public static final String BASE_URL = "http://129.211.165.110:8001";

    /**
     * 接口
     */

    //用户管理
    public static final String Login = BASE_URL + "/user/login";
    public static final String Register = BASE_URL + "/user/register";
    public static final String ModifyUserInfo = BASE_URL + "/user/modifyInformation";
    public static final String ModifyUserPwd = BASE_URL + "/user/modifyPassword";
    public static final String GetUserInfo = BASE_URL + "/user/getInformation";
    public static final String GetHistoricalLocation = BASE_URL + "/clothes/locationHistory";

    //衣物管理
    public static final String Upload = BASE_URL + "/clothes/add";
    public static final String Filter = BASE_URL + "/clothes/searchByAttribute";
    public static final String GetAllClothes = BASE_URL + "/clothes/getAllClothes";
    public static final String GetClothesDetail = BASE_URL + "/clothes/getClothesByID";
    public static final String ModifyClothesDetail = BASE_URL + "/clothes/modify";
    public static final String SearchClothes = BASE_URL + "/clothes/searchByKeyword";
    public static final String DeleteClothes = BASE_URL + "/clothes/delete";


    //位置管理
    public static final String GetLocation = BASE_URL + "/location/get";
    public static final String AddLocation = BASE_URL + "/location/add";
    public static final String DeleteLocation = BASE_URL + "/location/delete";

    //穿搭管理
    public static final String AddClothes = BASE_URL +"/outfit/addClothes";
    public static final String DeleteClothesFromOutfit = BASE_URL +"/outfit/deleteClothes";
    public static final String DeleteOutfit = BASE_URL +"/outfit/delete";
    public static final String GetOutfits = BASE_URL + "/outfit/get";
    public static final String AddOutfit = BASE_URL + "/outfit/add";

    //分类管理
    public static final String GetCategory = BASE_URL + "/category/get";
    public static final String AddCategory = BASE_URL + "/category/add";
    public static final String EditCategory = BASE_URL + "/category/edit";
    public static final String DeleteCategory = BASE_URL + "/category/delete";

    //统计管理
    public static final String StatisticByCategory = BASE_URL + "/statistic/category";
    public static final String StatisticByLocation = BASE_URL + "/statistic/location";
    public static final String StatisticBySeason = BASE_URL + "/statistic/season";
    public static final String StatisticByPrice = BASE_URL + "/statistic/priceRange";
}
