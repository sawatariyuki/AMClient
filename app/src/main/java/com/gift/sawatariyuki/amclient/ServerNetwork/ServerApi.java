package com.gift.sawatariyuki.amclient.ServerNetwork;

public class ServerApi {
    private static final String URL = "http://yuki.pandaomeng.com:90/";

    public static String login = URL + "user/loginPage";
    public static String register = URL + "user/register";
    public static String getEvent = URL + "event/getEvent";
    public static String getEventType = URL + "event/getType";
    public static String addEvent = URL + "event/addEvent";


    public static String getAll = URL + "getAll";
    public static String activate_GET = URL + "activate";
    public static String activate_POST = URL + "activatePage";
}
