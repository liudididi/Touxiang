package com.liu.asus.dianshang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.Myxqpage;
import MyFragment.frag_xer;
import MyFragment.frag_faxian;
import MyFragment.frag_xyi;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

public class Xiangqing extends AppCompatActivity {

    private Button bt_joinshapcar;
    private SharedPreferences sp;
    private int pid;
    private LinearLayout xq_shapcar;
    private ImageView iv_guanzhu;
    private ImageView xq_fanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqing2);
        initview();
    }
    private void initview() {
        TabLayout tl= (TabLayout) findViewById(R.id.tl);
        ViewPager xq_vp= (ViewPager) findViewById(R.id.xq_vp);
        iv_guanzhu = (ImageView) findViewById(R.id.iv_guanzhu);
        iv_guanzhu.setImageResource(R.drawable.guanzhu);
        xq_fanhui = (ImageView) findViewById(R.id.xq_fanhui);
        xq_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<String> menus=new ArrayList<>();
        menus.add("商品");
        menus.add("详情");
        menus.add("评价");
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new frag_xyi());
        fragments.add(new frag_xer());
        fragments.add(new frag_faxian());
        Myxqpage mypager=new Myxqpage(getSupportFragmentManager(),fragments,menus);
        xq_vp.setAdapter(mypager);
        tl.setupWithViewPager(xq_vp);
        pid = getIntent().getIntExtra("pid", -1);
        initgouwu();
        inittiao();
        
    }

    private void inittiao() {
        xq_shapcar = (LinearLayout) findViewById(R.id.xq_shapcar);
        xq_shapcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Xiangqing.this,HomeActivity.class);
                sp.edit().putBoolean("gouwuche",true).commit();
                startActivity(in);
            }
        });
        LinearLayout  guanzhu= (LinearLayout) findViewById(R.id.guanzhu);
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getBoolean("guanzhu", false)){
                    iv_guanzhu.setImageResource(R.drawable.guanzhu);
                    Toast.makeText(Xiangqing.this, "取消关注", Toast.LENGTH_SHORT).show();
                    sp.edit().putBoolean("guanzhu",false).commit();
                }else {
                    iv_guanzhu.setImageResource(R.drawable.guanzhu2);
                    sp.edit().putBoolean("guanzhu",true).commit();
                    Toast.makeText(Xiangqing.this, "关注成功", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void initgouwu() {
        bt_joinshapcar = (Button) findViewById(R.id.joinshapcar);
        sp = getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        bt_joinshapcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uid = sp.getInt("uid", -1);
                if(uid==-1){
                    Toast.makeText(Xiangqing.this, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                jsonshopcar(uid,pid);
            }
        });
    }
    private void jsonshopcar(int uid, int pid) {
        Okhttputils.requer("http://120.27.23.105/product/addCart?uid="+uid+"&&pid="+pid, new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Xiangqing.this, "网络较慢，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onresponse(Call call, Response response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    final String msg = json.optString("msg");
                    if(msg!=null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Xiangqing.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
