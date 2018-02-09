package com.delta.soaplib.network;


import com.delta.soaplib.network.ksoap2.SoapEnvelope;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2018/2/7 9:58
 */


public class SoapConfig {
    private int version;
    private String namespace;
    private String contentType;

    public SoapConfig(Builder mBuilder) {
        this.version =mBuilder.version;
        this.namespace =mBuilder.namespace;
        this.contentType=mBuilder.contentType;
    }

    public int Version() {
        return version;
    }

    public String Namespace() {
        return namespace;
    }

    public String ContentType() {
        return contentType;
    }

    public static Builder builder() {
        return  new Builder();
    }


    public static  final class Builder{
        private int version;
        private String namespace;
        private String contentType;

        public Builder version(int version){
            this.version =version;
            return this;
        }

        public Builder namesapce(String mNamespace) {
            this.namespace = mNamespace;
            return this;
        }
        public Builder contentType(String contentType){
            this.contentType = contentType;
            return this;
        }

        public SoapConfig build(){

            if (version == SoapEnvelope.VER11 || version == SoapEnvelope.VER10 || version == SoapEnvelope.VER12) {
                return new SoapConfig(this);
            }else {
                throw new IllegalArgumentException("Please specify the version number of Soap(SoapEnvelope.VER10,VER11,VER12 )");

            }

        }

    }

}
