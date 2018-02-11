package com.delta.retrofitsoap.entity;

import org.simpleframework.xml.Element;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2018/2/9 15:31
 */

public class CityList {

    @Element
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String mString) {
        string = mString;
    }

    @Override
    public String toString() {
        return "CityList{" +
                "string='" + string + '\'' +
                '}';
    }
    //    @ElementList(entry = "string",inline = false,required = false)
//    private List<String> string;
//
//    public List<String> getString() {
//        return string;
//    }
//
//    public void setString(List<String> mString) {
//        string = mString;
//    }
//
//    @Override
//    public String toString() {
//        return "CityList{" +
//                "string=" + string +
//                '}';
//    }
}
