package com.delta.soaplib.network;

import android.util.Log;

import com.delta.soaplib.network.ksoap2.SoapEnvelope;
import com.delta.soaplib.network.ksoap2.serialization.SoapSerializationEnvelope;
import com.delta.soaplib.network.ksoap2.transport.SoapHelper;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.R.attr.version;
import static com.delta.soaplib.network.ksoap2.transport.Transport.CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8;
import static com.delta.soaplib.network.ksoap2.transport.Transport.CONTENT_TYPE_XML_CHARSET_UTF_8;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2018/2/6 16:22
 */


public class SoapNetWorkInteceptor implements Interceptor {


    private static final String SOAP = "SOAP";
    private static final String NAMESAPCE = "Namespace";
    private static final String SOAPACTION = "SOAPAction";
    private static final String methodName = "MethodName";
    private  String currentMethodName=null;
    private SoapConfig mSoapConfig;
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/xml; charset=UTF-8");

    public SoapNetWorkInteceptor(SoapConfig mSoapConfig) {
        this.mSoapConfig = mSoapConfig;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request mOrignalRequest = chain.request();
        Log.e("request", "intercept: " + mOrignalRequest);
        if (obtainSoapFromHeaders(mOrignalRequest, SOAP)) {

            return soapProceed(chain, mOrignalRequest);
        }
        return chain.proceed(mOrignalRequest);

    }

    private Response soapProceed(Chain mChain, Request mOrignalRequest) throws IOException {


        Request mSoapRequest = buildSoapRequest(mOrignalRequest);

        Response mOrignalResponse = mChain.proceed(mSoapRequest);
        if (mOrignalResponse.code() == 200) {
            // mSoapResponse.peekBody();
            Headers mHeaders = mOrignalResponse.headers();
            ResponseBody mBody = mOrignalResponse.body();
            MediaType mMediaType = mBody.contentType();
            if (mOrignalResponse != null) {
               // SoapSerializationEnvelope soapEnvelope = SoapHelper.getInstance().getSoapEnvelope();
//                BufferedSource mSource = mOrignalResponse.body().source();
     // SoapHelper.getInstance().parseResponse(soapEnvelope, mSource.inputStream());
//                //todo  怎么处理null的情况
//
//                String body = soapEnvelope.bodyIn.toString();
//                System.out.println("quest"+body);
                ResponseBody mNewResponseBody = null;
                if (mMediaType != null) {
                    mNewResponseBody=parseResponseBody(mMediaType,currentMethodName,mBody);

                } else {
                    mNewResponseBody =parseResponseBody(MEDIA_TYPE,currentMethodName,mBody);
                }

                Response.Builder mBuilder = mOrignalResponse.newBuilder();
                mBuilder.protocol(mOrignalResponse.protocol())
                        .code(mOrignalResponse.code())
                        .message(mOrignalResponse.message())
                        .handshake(mOrignalResponse.handshake())
                        .headers(mHeaders)
                        .body(mNewResponseBody)
                        .cacheResponse(mOrignalResponse.cacheResponse())
                        .networkResponse(mOrignalResponse.networkResponse())
                        .sentRequestAtMillis(mOrignalResponse.sentRequestAtMillis())
                        .priorResponse(mOrignalResponse.priorResponse());
                Response mNewResponse = mBuilder.build();
//            SoapObject resultsRequestSOAP = (SoapObject) soapEnvelope.bodyIn;
//            Object obj = (Object) resultsRequestSOAP.getProperty(0);
                return mNewResponse;
            }
        } else {
            return mOrignalResponse;
        }

        return mOrignalResponse;
    }

    private Request buildSoapRequest(Request mOrignalRequest) {

        String namespace = null;
        if (obtainSoapFromHeaders(mOrignalRequest, NAMESAPCE)) {
            List<String> mNameSpaces = mOrignalRequest.headers(NAMESAPCE);
            namespace = mNameSpaces.get(0);
        } else {
            if (mSoapConfig.Namespace() != null) {
                namespace = mSoapConfig.Namespace();
            } else {
                throw new IllegalArgumentException("please  specify namespace ");
            }
        }
        String soapAction = null;

        if (mSoapConfig.Version() != SoapEnvelope.VER12) {
            if (obtainSoapFromHeaders(mOrignalRequest, SOAPACTION)) {
                List<String> soapActions = mOrignalRequest.headers(SOAPACTION);
                soapAction = soapActions.get(0);
            }else {
                throw new IllegalArgumentException("please  specify SOAPAction ");
            }
        } else {
            soapAction = null;
        }

        if (obtainSoapFromHeaders(mOrignalRequest, methodName)) {

            List<String> mMethodNames = mOrignalRequest.headers(methodName);

             currentMethodName = mMethodNames.get(0);
            RequestBody mBody = mOrignalRequest.body();

           MediaType mMediaType = null ;
            if (version == SoapSerializationEnvelope.VER12) {
                //mheaderMap.put("Content-Type", CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8);
                mMediaType=MediaType.parse(CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8);

            } else {
                mMediaType=MediaType.parse(CONTENT_TYPE_XML_CHARSET_UTF_8);

            }
            byte[] mRequestData = null;
            HashMap<String, Object> mMap = new HashMap<>();
            if (mBody instanceof FormBody) {
                FormBody mFormBody = (FormBody) mBody;
                for (int mI = 0; mI < mFormBody.size(); mI++) {
                    String key = mFormBody.encodedName(mI);
                    String value = URLDecoder.decode(mFormBody.encodedValue(mI)).replace("&lt;", "<").replace("&gt;", ">").replace("%24", "$").replace("&quot;","\"");
                    //String mValue = mFormBody.encodedValue(mI);
                    mMap.put(key, value);
                }
                //todo 添加namespace默認值
                mRequestData = SoapHelper.getInstance().createRequestContent(currentMethodName, namespace, mSoapConfig.Version(), mMap);
            } else {
                mRequestData = SoapHelper.getInstance().createRequestContent(currentMethodName, namespace, mSoapConfig.Version(), null);

            }

            if (mRequestData != null && mRequestData.length > 0) {
                Log.e("mRequestData", new String(mRequestData));
                RequestBody mNewRequestBody = RequestBody.create(mMediaType, mRequestData);
                Map<String, String> mHeader = SoapHelper.getInstance().createHeader(mSoapConfig.Version(), soapAction,mRequestData);
                Request.Builder mBuilder = mOrignalRequest.newBuilder();
                for (Map.Entry<String, String> mHeaders : mHeader.entrySet()) {
                    mBuilder.addHeader(mHeaders.getKey(), mHeaders.getValue());
                }
                mBuilder.post(mNewRequestBody);
                Request mSoapRequest = mBuilder.build();
                return mSoapRequest;
            }

        } else {
            throw new IllegalArgumentException("Soap must contain Namespace MethodName ");
        }
        return mOrignalRequest;
    }

    private boolean obtainSoapFromHeaders(Request mOrignalRequest, String header) {
        List<String> mHeaders = mOrignalRequest.headers(header);

        if (mHeaders == null || mHeaders.size() == 0) {
            return false;
        }
        if (mHeaders.size() > 1) {
            throw new IllegalArgumentException("only one " + header + " in the headers");
        }

        return true;

    }

    /**
     * 解析Response
     * @param methodName
     * @param responseBody
     * @return
     */
    private ResponseBody parseResponseBody(MediaType mediaType, String methodName, ResponseBody responseBody) {
        try {


            String res = responseBody.string().replace("&lt;", "<").replace("&gt;", ">").replace("%24", "$");
            if (res != null && !res.equals("")) {
                // 字符转义
                String ostar = "<" + methodName + "Result>";
                String oend = "</" + methodName + "Result>";
                if (res.contains(ostar) && res.contains(oend)) {
                    int startIndex = res.indexOf(ostar) + ostar.length();
                    int endIndex = res.lastIndexOf(oend);
                    String ores = res.substring(startIndex, endIndex);
                    Log.d("response", "parseResponseBody: "+ ores);
                    return ResponseBody.create(mediaType, ores);
                }
            }else{
                return ResponseBody.create(mediaType, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
