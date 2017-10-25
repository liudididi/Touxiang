package com.liu.asus.dianshang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

public class Xnicheng extends AppCompatActivity {

    private EditText ed_nicheng;
    private SharedPreferences sp;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xnicheng);
        initview();
    }
    private void initview() {
        sp = getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        uid = sp.getInt("uid", -1);
        ImageView img_fanhui= (ImageView) findViewById(R.id.tv_fanhui);
        img_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
         ed_nicheng = (EditText) findViewById(R.id.ed_nicheng);
        String name = sp.getString("name", null);
        ed_nicheng.setText(name);
        Button bt_queding= (Button) findViewById(R.id.bt_queding);
          bt_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  String name= ed_nicheng.getText().toString();
                  Okhttputils.requer("http://120.27.23.105/user/updateNickName?uid="+uid+"&&nickname="+name, new Okhttputils.Backquer() {
                    @Override
                    public void onfailure(Call call, IOException e) {

                    }
                    @Override
                    public void onresponse(Call call, Response response) {
                        try {
                            JSONObject json=new JSONObject(response.body().string());
                            final String msg = json.optString("msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    //把返回数据存入Intent
                                    intent.putExtra("result",ed_nicheng.getText().toString());
                                    intent.putExtra("msg",msg);
                                    //设置返回数据
                                    setResult(1, intent);
                                    //关闭Activity
                                    finish();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
