package com.delta.retrofitsoap;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Mr_Sun on 2016/12/1.
 * http://www.jianshu.com/p/6fc8c9081c64
 */

public interface IWeatherApi {

    @FormUrlEncoded
    @POST("WebServices/WeatherWebService.asmx")
    @Headers({
            "SOAP:true",
            "Namespace:http://WebXml.com.cn/",
            "SOAPAction:http://WebXml.com.cn/getSupportCity",
            "MethodName:getSupportCity"
            })
    rx.Observable<ResponseBody> getSupportCity(@Field("byProvinceName") String byProvinceName);

}
