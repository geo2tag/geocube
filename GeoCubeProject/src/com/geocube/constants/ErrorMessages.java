package com.geocube.constants;

import java.util.HashMap;

public class ErrorMessages extends HashMap<Integer, String>{
    private static ErrorMessages instance;

    private ErrorMessages (){
        put(ErrorList.SUCCESS, "Success");
        put(ErrorList.WRONG_TOKEN_ERROR, "Wrong token error");
        put(ErrorList.USER_ALREADY_EXIST_ERROR, "This user already exists");
        put(ErrorList.USER_DOES_NOT_EXIST_ERROR, "This user doesn't exist");
        put(ErrorList.CHANNEL_ALREADY_EXIST_ERROR, "Channel already exists");
        put(ErrorList.CHANNEL_DOES_NOT_EXIST_ERROR, "Channel doesn't exist");
        put(ErrorList.SUBSCRIPTION_ALREADY_EXIST, "Subscription already exists");
        put(ErrorList.INTERNAL_DB_ERROR, "Internal database error");
        put(ErrorList.INCORRECT_QUERY_NAME_ERROR, "Incorrect query name");
        put(ErrorList.INCORRECT_JSON_ERROR, "Incorrect JSON");
        put(ErrorList.INCORRECT_CREDENTIALS_ERROR, "Incorrect credentails");
        put(ErrorList.CHANNEL_NOT_SUBCRIBED_ERROR, "Channel not subscribed");
        put(ErrorList.CHANNEL_ALREADY_SUBSCRIBED_ERROR, "Channel already subscribed");
        put(ErrorList.TAG_DOES_NOT_EXIST_ERROR, "Tag doesn't exist");
        put(ErrorList.TAG_ALREADY_EXIST_ERROR, "Tag already exists");
        put(ErrorList.NULL_TIMESLOT_ERROR, "Null timeslot");
        put(ErrorList.UNKNOWN_ERROR, "Unkniwn error");
        put(ErrorList.TMP_USER_ALREADY_EXIST_ERROR, "Tmp user already exists");
        put(ErrorList.NETWORK_ERROR, "Network error");
        put(ErrorList.EMAIL_ALREADY_EXIST_ERROR, "Email already exist");
        put(ErrorList.WEAK_PASSWORD_ERROR, "Weak password");
    }

    public static ErrorMessages getInstance(){
        if (instance == null){
            instance = new ErrorMessages();
        }
        return instance;
    }

}
