package com.delta.retrofitsoap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.delta.retrofitsoap.presenter.IWeatherPresenterImpl;
import com.delta.retrofitsoap.view.IWeatherView;

public class MainActivity extends AppCompatActivity implements IWeatherView, View.OnClickListener {
    EditText city;
    IWeatherPresenterImpl mIWeatherPresenter;
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mIWeatherPresenter = new IWeatherPresenterImpl(this);
        city = (EditText) findViewById(R.id.et);
        mTextView = (TextView) findViewById(R.id.tv_city);

        findViewById(R.id.bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View mView) {
        if (TextUtils.isEmpty(city.getText().toString())) {
            return;
        }
        mIWeatherPresenter.getSupportCity(city.getText().toString());

    }

    @Override
    public void showText(String mString) {
        mTextView.setText(mString);
    }
}
