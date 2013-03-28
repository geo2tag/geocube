package com.geocube.constants;


public interface ErrorList {
    public static int SUCCESS = 0;
    public static int WRONG_TOKEN_ERROR = 1;
    public static int USER_ALREADY_EXIST_ERROR = 2;
    public static int USER_DOES_NOT_EXIST_ERROR = 3;
    public static int CHANNEL_ALREADY_EXIST_ERROR = 4;
    public static int CHANNEL_DOES_NOT_EXIST_ERROR = 5;
    public static int SUBSCRIPTION_ALREADY_EXIST = 6;
    public static int INTERNAL_DB_ERROR = 7;
    public static int INCORRECT_QUERY_NAME_ERROR = 8;
    public static int INCORRECT_JSON_ERROR = 9;
    public static int INCORRECT_CREDENTIALS_ERROR = 10;
    public static int CHANNEL_NOT_SUBCRIBED_ERROR = 11;
    public static int CHANNEL_ALREADY_SUBSCRIBED_ERROR = 12;
    public static int TAG_DOES_NOT_EXIST_ERROR = 13;
    public static int TAG_ALREADY_EXIST_ERROR = 14;
    public static int NULL_TIMESLOT_ERROR = 15;
    public static int UNKNOWN_ERROR = 16;
    public static int TMP_USER_ALREADY_EXIST_ERROR = 17;
    public static int NETWORK_ERROR = 18;
    public static int EMAIL_ALREADY_EXIST_ERROR = 19;
    public static int WEAK_PASSWORD_ERROR = 20;
}
