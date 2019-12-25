package com.quantuminventions.speechtotext.app;

import android.content.Context;

public class AppConstant {

    public static String INTENT_KEY_SPEECH_INPUT_RESULT = "KEY_SPEECH_INPUT_RESULT";
    public static final String ENDPOINT_DICTIONARY_API = "interview/dictionary-v2.json";
    public static final int REQ_SPEECH_INPUT_RESULT = 1070;
    public static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String BASE_URL = "http://a.galactio.com/";

    public static String getDictionaryUrl(Context context) {
        return getBaseUrl(context) + ENDPOINT_DICTIONARY_API;
    }
    public static String getBaseUrl(Context context) {
        return BASE_URL;
    }
}
