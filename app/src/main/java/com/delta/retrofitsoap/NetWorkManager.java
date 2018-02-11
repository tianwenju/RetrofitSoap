package com.delta.retrofitsoap;

import com.delta.soaplib.network.SoapConfig;
import com.delta.soaplib.network.SoapNetWorkInteceptor;
import com.delta.soaplib.network.ksoap2.serialization.SoapSerializationEnvelope;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2018/2/6 16:18
 */


public class NetWorkManager {




    public static IWeatherApi getAPIService(){

        SoapConfig mSoapConfig = SoapConfig.builder().version(SoapSerializationEnvelope.VER12).build();

        OkHttpClient mOkHttpClient=new OkHttpClient.Builder().addInterceptor(new SoapNetWorkInteceptor(mSoapConfig)).build();
        Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient).baseUrl(API.URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        return retrofit.create(IWeatherApi.class);

    }

}
