package com.gift.sawatariyuki.amclient.ServerNetwork;

public class ServerApi {
    private static final String URL = "http://yuki.pandaomeng.com/";

    public static String login = URL + "user/loginPage";
    public static String register = URL + "user/register";
    public static String userInfo = URL + "user/userInfo";
    public static String userDetail = URL + "user/detailPage";

    public static String getEvent = URL + "event/getEvent";
    public static String getEventType = URL + "event/getType";
    public static String addEvent = URL + "event/addEvent";
    public static String deleteEvent = URL + "event/deleteEvent";
    public static String cancelEvent = URL + "event/cancelEvent";
    public static String addOrUpdateEventType = URL + "event/addOrUpdateEventType";
    public static String deleteEventType = URL + "event/deleteEventType";
    public static String arrange = URL + "event/arrange";

    public static String getLog = URL + "user/log";

    public static String getAll = URL + "getAll";
    public static String activate_GET = URL + "activate";
    public static String activate_POST = URL + "activatePage";
}
