#### 简介
RetrofitSoap是适用于Android端，基于Retroif实现soap协议的网络组件。
#### 特点
* 同时支持Soap协议和Http协议
* 不改变原有网络框架轻松解耦
* 使用简便
#### 使用方法
1. 添加gradle依赖
    暂时支持本地依赖，后期会放到Jcenter仓库
    compile project(":soaplib")
2. 使用方法
    * 配置SoapConifg
        ```
        SoapConfig mSoapConfig = SoapConfig.builder().version(SoapSerializationEnvelope.VER11).contentType().namesapce().build();
        ```
    这里设置的是全局的soapConfig，其中version必须要填写。
    * 添加拦截器
    ```
     OkHttpClient mOkHttpClient=new OkHttpClient.Builder().addInterceptor(new SoapNetWorkInteceptor(mSoapConfig)).build();
    ```
    * 在ApiService上配置Soap协议
    ```
        @FormUrlEncoded
    @POST("WebServices/WeatherWebService.asmx")
    @Headers({
            "SOAP:true",
            "Namespace:http://WebXml.com.cn/",
            "SOAPAction:http://WebXml.com.cn/getSupportCity",
            "MethodName:getSupportCity"
            })
    rx.Observable<ResponseBody> getSupportCity(@Field("byProvinceName") String byProvinceName);
    ```
    * 如果返回的接口需要xml解析请用SimpleXml
    ```
     compile ('com.squareup.retrofit2:converter-simplexml:2.1.0'){
        exclude group: 'xpp3', module: 'xpp3'
        exclude group: 'stax', module: 'stax-api'
        exclude group: 'stax', module: 'stax'
    }
    ```
    ```
    Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient).baseUrl(API.URL)
              .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    ```