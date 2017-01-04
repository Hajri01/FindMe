package com.esprit.findme.utils;

/**
 * Created by TIBH on 07/11/2016.
 */

public class AppConfig {

    public static   String IP ="findme2017.000webhostapp.com" ;


    // user
    public static String URL_LOGIN = "https://"+IP+"/findme/login.php";
    public static String URL_REGISTER = "https://"+IP+"/findme/register.php";
    public static String URL_LOAD_PHOTO = "https://"+IP+"/findme/updateUserImage.php";
    public static String URL_GET_USERS = "https://"+IP+"/findme/getUserByCircle.php";
    public static String URL_GET_USER = "https://"+IP+"/findme/getUser.php";
    public static String URL_UPDATE_POSITION = "https://"+IP+"/findme/updateUserPosition.php";
    public static String URL_UPDATE_PHOTO = "https://"+IP+"/findme/updateUserPhoto.php";
    public static String URL_EDIT_USER = "https://"+IP+"/findme/users/editprofile.php";
    public static String URL_EDIT_PWD="https://"+IP+"/findme/users/editpass.php";




    // circles
    public static String URL_AddCircle = "https://"+IP+"/findme/circle.php";
    public static String URL_JOIN_CIRCLE = "https://"+IP+"/findme/inviter.php";

    //NEWS


    public static String URL_ADD_NEWS = "https://"+IP+"/findme/news/addNews.php";

    public static String URL_GET_NEWS = "https://"+IP+"/findme/news/getAllCircleNews.php";

    public static String URL_GET_CIRCLES = "https://"+IP+"/findme/getCirclesByUser.php";




}
