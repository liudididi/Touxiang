package com.liu.asus.dianshang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meg7.widget.CustomShapeImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import presenter.Changepresent;
import utils.Okhttputils;

public class MemessageActivity extends AppCompatActivity {

    private ImageView tv_fanhui;
    private Changepresent changepresent;
    private CustomShapeImageView img_icon;
    private SharedPreferences sp;
    private TextView tv_yhm;
    private int uid;
    private Button bt_tuichu;
    private TextView tv_nicheng;
    private  int nicheng=1;
    private Button bt_tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memessage);
        initview();
        uid = getIntent().getIntExtra("uid",-1);
        Okhttputils.requer("http://120.27.23.105/user/getUserInfo?uid="+uid, new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MemessageActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                    JSONObject json=new JSONObject(response.body().string());
                    JSONObject data = json.getJSONObject("data");
                    final String name=data.optString("username");
                     if(name!=null){
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               tv_yhm.setText(name);
                           }
                       });
                     }
                    final String icon = data.optString("icon");
                    if(icon!=null&&icon.length()>3){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sp.edit().putString("icon",icon).commit();
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            ImageLoader.getInstance().displayImage(icon,img_icon);
                        }
                    });
                    }
                    final String nickname = data.optString("nickname");
                       if(nickname!=null){
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   tv_nicheng.setText(nickname);
                               }
                           });
                       }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tv_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MemessageActivity.this,HomeActivity.class);
                sp.edit().putBoolean("wode",true).commit();
                startActivity(in);
            }
        });
        img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepresent.showChoosePicDialog(MemessageActivity.this);
            }
        });
        bt_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MemessageActivity.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
    private void initview() {
        tv_fanhui = (ImageView) findViewById(R.id.tv_fanhui);
        changepresent = new Changepresent();
        img_icon = (CustomShapeImageView) findViewById(R.id.img_icon);
        sp = getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        tv_yhm = (TextView) findViewById(R.id.tv_yhm);
        bt_tuichu = (Button) findViewById(R.id.bt_tuichu);
        tv_nicheng = (TextView) findViewById(R.id.tv_nicheng);
        tv_nicheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MemessageActivity.this,Xnicheng.class);
                startActivityForResult(in,nicheng);
            }
        });
        bt_tc = (Button) findViewById(R.id.bt_tc);
        bt_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                sp.edit().putString("name","登录/注册").commit();
                Intent in=new Intent(MemessageActivity.this,HomeActivity.class);
                sp.edit().putBoolean("wode",true).commit();
                startActivity(in);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        changepresent.back(requestCode,resultCode,data,MemessageActivity.this,img_icon,uid);
        if(resultCode==nicheng){
            String result = data.getExtras().getString("result");
            String msg = data.getExtras().getString("msg");
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            sp.edit().putString("name",result).commit();
            tv_nicheng.setText(result);
        }
    }
}
