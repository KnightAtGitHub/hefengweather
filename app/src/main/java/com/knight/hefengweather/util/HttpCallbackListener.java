package com.knight.hefengweather.util;

/**
 * Created by knight on 17-6-9.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
  //  void onFinish(String kind,String city,String response);
    void onError(Exception e);
}
