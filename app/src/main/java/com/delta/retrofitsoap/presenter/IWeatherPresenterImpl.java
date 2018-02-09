package com.delta.retrofitsoap.presenter;

import android.util.Log;

import com.delta.retrofitsoap.NetWorkManager;
import com.delta.retrofitsoap.view.IWeatherView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Mr_Sun on 2016/12/1.
 * http://www.jianshu.com/p/6fc8c9081c64
 */

public class IWeatherPresenterImpl implements IWeatherPresenter {


    private IWeatherView iLoginView;



    public IWeatherPresenterImpl(IWeatherView view) {
        this.iLoginView = view;

    }

    @Override
    public void getSupportCity(String city) {

        NetWorkManager.getAPIService().getSupportCity(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Object>() {
            @Override
            public void call(Object mO) {

                Log.e(TAG, "call: "+mO);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable mThrowable) {
                Log.e(TAG, "call: "+mThrowable.toString());
            }
        });
    }
}
