package com.liu.asus.dianshang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Adapter.Mygridadpter;
import Adapter.Mylistadpter;
import Adapter.Mylistduodpter;
import bean.Dingdanbean;
import bean.Shangpin;
import model.GridSpacingItemDecoration;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

public class Sousuo extends AppCompatActivity {
    private XRecyclerView recycle;
    private EditText ed_sou;
    private SharedPreferences sp;
    private List<Shangpin> recyclebeanList=new ArrayList<>();
    private GridSpacingItemDecoration gsid;
    private LinearLayout s_resou;
    private LinearLayout s_jieguo;
    private ImageView img_fanhui;
    private Mylistadpter myrecycle;
    private Mygridadpter mygridadpter;
    private Button bt_jiage,bt_xiaoliang,bt_zonghe ;
    private String guanjian=null;
    private  int page=1;
    private Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sousuo);
        initview();
        Intent intent = getIntent();
        guanjian = intent.getStringExtra("guanjian");
        if(guanjian !=null)
        {
            s_jieguo.setVisibility(View.VISIBLE);
            s_resou.setVisibility(View.GONE);
            getdata(guanjian);
        }

    }
    private void initview() {
        sp = getSharedPreferences("moni", MODE_PRIVATE);
        s_resou = (LinearLayout) findViewById(R.id.s_resou);
        s_jieguo = (LinearLayout) findViewById(R.id.s_jieguo);
        ed_sou = (EditText) findViewById(R.id.ed_sou);
        img_fanhui = (ImageView) findViewById(R.id.img_fanhui);
        img_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        inittextonlick();
        initjiage();
        TextView tv_sou= (TextView) findViewById(R.id.tv_sou);
        tv_sou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ed_sou.getText().toString())){
                    Toast.makeText(Sousuo.this, "关键词不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                guanjian=ed_sou.getText().toString();
                getdata(guanjian);
            }
        });
        final ImageView img_qie = (ImageView) findViewById(R.id.img_qie);
        int spanCount = 2;
        int spacing = 10 ;
        boolean includeEdge = false;
        gsid = new GridSpacingItemDecoration(spanCount, spacing, includeEdge);
        img_qie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getBoolean("list",false)==true){
                    img_qie.setImageResource(R.drawable.imggrid);
                    myrecycle = new Mylistadpter(Sousuo.this,recyclebeanList);
                    initonlistclik(myrecycle);
                    myrecycle.notifyDataSetChanged();
                    recycle.setLayoutManager(new LinearLayoutManager(Sousuo.this));
                    sp.edit().putBoolean("list",false).commit();
                    recycle.setAdapter(myrecycle);
                }else {
                    myrecycle=null;
                    img_qie.setImageResource(R.drawable.imglist);
                    mygridadpter = new Mygridadpter(Sousuo.this,recyclebeanList);
                    initongridclik(mygridadpter);
                    mygridadpter.notifyDataSetChanged();
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(Sousuo.this,2);
                    recycle.setLayoutManager(gridLayoutManager);
                    sp.edit().putBoolean("list",true).commit();
                    recycle.setAdapter(mygridadpter);
                }
            }
        });
        recycle = (XRecyclerView) findViewById(R.id.recycle);
        recycle.setRefreshProgressStyle(13);
        recycle.setLaodingMoreProgressStyle(18);
        recycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                            recyclebeanList = new ArrayList<Shangpin>();
                            page=1;
                            myrecycle=null;
                            getdata(guanjian);
                            recycle.refreshComplete();


                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                            page++;
                            getdata(guanjian);
                            recycle.loadMoreComplete();

                    }
                }, 2000);
            }
        });
      /*  int spanCount = 2;
        int spacing = 5;
        boolean includeEdge = false;
        recycle.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/
    }

    private void initonlistclik(Mylistadpter myrecycle) {
        myrecycle.setOnrecyclik(new Mylistadpter.Onrecyclik() {
            @Override
            public void MyreclOnk(View img, int postion) {
                Intent intent=new Intent(Sousuo.this, Xiangqing.class);
                intent.putExtra("url",recyclebeanList.get(postion).url);
                intent.putExtra("pid",recyclebeanList.get(postion).pid);
                startActivity(intent);
            }
        });
    }
    private void initongridclik(Mygridadpter myrecycle) {
        myrecycle.setOngridclik(new Mygridadpter.Ongridclik() {
            @Override
            public void MyreclOnk(View img, int postion) {
                Intent intent=new Intent(Sousuo.this, Xiangqing.class);
                intent.putExtra("url",recyclebeanList.get(postion).url);
                intent.putExtra("pid",recyclebeanList.get(postion).pid);
                startActivity(intent);
            }
        });
    }

    private void initjiage() {
        bt_jiage = (Button) findViewById(R.id.bt_jiage);
        bt_xiaoliang = (Button) findViewById(R.id.bt_xiaoliang);
        bt_zonghe = (Button) findViewById(R.id.bt_zonghe);
        bt_zonghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    bt_zonghe.setTextColor(Color.RED);
                    bt_xiaoliang.setTextColor(Color.BLACK);
                    bt_jiage.setTextColor(Color.BLACK);
                    getdata(guanjian);
            }
        });
        bt_jiage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_zonghe.setTextColor(Color.BLACK);
                bt_xiaoliang.setTextColor(Color.BLACK);
                bt_jiage.setTextColor(Color.RED);
                if(sp.getBoolean("jiang",false)){
                    Collections.sort(recyclebeanList, new Comparator<Shangpin>() {
                        @Override
                        public int compare(Shangpin house2, Shangpin t1) {
                            int i = house2.price - t1.price;
                            if(i>0){
                                return -1;
                            }
                            return 0;
                        }
                    });
                    if(sp.getBoolean("list",false)){
                        mygridadpter = new Mygridadpter(Sousuo.this,recyclebeanList);
                        initongridclik(mygridadpter);
                        mygridadpter.notifyDataSetChanged();
                        recycle.setAdapter(mygridadpter);
                    }else {
                        myrecycle = new Mylistadpter(Sousuo.this,recyclebeanList);
                        initonlistclik(myrecycle);
                        myrecycle.notifyDataSetChanged();
                        recycle.setAdapter(myrecycle);
                    }
               sp.edit().putBoolean("jiang",false).commit();
                }else {
                    Collections.sort(recyclebeanList, new Comparator<Shangpin>() {
                        @Override
                        public int compare(Shangpin house2, Shangpin t1) {
                            int i = house2.price - t1.price;
                            if(i<0){
                                return -1;
                               }
                            return 0;
                        }
                    });
                    if(sp.getBoolean("list",false)){
                        mygridadpter = new Mygridadpter(Sousuo.this,recyclebeanList);
                        mygridadpter.notifyDataSetChanged();
                        initongridclik(mygridadpter);
                        recycle.setAdapter(mygridadpter );
                    }else {
                        myrecycle = new Mylistadpter(Sousuo.this,recyclebeanList);
                        myrecycle.notifyDataSetChanged();
                        initonlistclik(myrecycle);
                        recycle.setAdapter(myrecycle);
                    }
                    sp.edit().putBoolean("jiang",true).commit();
                }
            }
        });
        bt_xiaoliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_xiaoliang.setTextColor(Color.RED);
                bt_jiage.setTextColor(Color.BLACK);
                bt_zonghe.setTextColor(Color.BLACK);
                if(sp.getBoolean("jiang",false)){
                    Collections.sort(recyclebeanList, new Comparator<Shangpin>() {
                        @Override
                        public int compare(Shangpin house2, Shangpin t1) {
                            int i = house2.salenum - t1.salenum;
                            if(i>0){
                                return -1;
                            }
                            return 0;
                        }
                    });
                    if(sp.getBoolean("list",false)){
                        mygridadpter = new Mygridadpter(Sousuo.this,recyclebeanList);
                        initongridclik(mygridadpter);
                        recycle.setAdapter(mygridadpter);
                    }else {
                        myrecycle = new Mylistadpter(Sousuo.this,recyclebeanList);
                        initonlistclik(myrecycle);
                        recycle.setAdapter(myrecycle);
                    }
                    sp.edit().putBoolean("jiang",false).commit();
                }else {
                    Collections.sort(recyclebeanList, new Comparator<Shangpin>() {
                        @Override
                        public int compare(Shangpin house2, Shangpin t1) {
                            int i = house2.salenum - t1.salenum;
                            if(i<0){
                                return -1;
                            }
                            return 0;
                        }
                    });
                    if(sp.getBoolean("list",false)){
                        mygridadpter = new Mygridadpter(Sousuo.this,recyclebeanList);
                        initongridclik(mygridadpter);
                        mygridadpter.notifyDataSetChanged();
                        recycle.setPullRefreshEnabled(false);
                        recycle.setAdapter(mygridadpter );
                    }else {
                        mygridadpter=null;
                        myrecycle = new Mylistadpter(Sousuo.this,recyclebeanList);
                        myrecycle.notifyDataSetChanged();
                        initonlistclik(myrecycle);
                        recycle.setAdapter(myrecycle);
                        recycle.setPullRefreshEnabled(true);
                    }
                    sp.edit().putBoolean("jiang",true).commit();
                }
            }
        });
    }
    private void inittextonlick() {
       final TextView tv_sj= (TextView) findViewById(R.id.tv_sj);
        tv_sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guanjian=tv_sj.getText().toString();
                getdata(guanjian);
            }
        });
       final TextView tv_bjb= (TextView) findViewById(R.id.tv_bjb);
        tv_bjb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guanjian=tv_bjb.getText().toString();
                getdata(guanjian);
            }
        });
       final TextView tv_ls= (TextView) findViewById(R.id.tv_ls);
        tv_ls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guanjian=tv_ls.getText().toString();
                getdata(guanjian);
            }
        });
    }
    private void getdata(String key) {
        Okhttputils.requer("http://120.27.23.105/product/searchProducts?keywords=" + key + "&&page=1", new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Sousuo.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                    recyclebeanList = new ArrayList<>();
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray list = json.getJSONArray("data");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject l = (JSONObject) list.get(i);
                        Shangpin recyclebean = new Shangpin();
                        recyclebean.imgurl = l.optString("images");
                        recyclebean.zi = l.optString("title");
                        recyclebean.price = l.optInt("price");
                        recyclebean.pid = l.optInt("pid");
                        recyclebean.pscid = l.optInt("pscid");
                        recyclebean.salenum= l.optInt("salenum");
                        recyclebean.url = l.optString("detailUrl");
                        recyclebeanList.add(recyclebean);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            s_jieguo.setVisibility(View.VISIBLE);
                            s_resou.setVisibility(View.GONE);
                            Toast.makeText(Sousuo.this, "搜索成功", Toast.LENGTH_SHORT).show();
                           if(sp.getBoolean("list",false)){
                               recycle.setLayoutManager(new GridLayoutManager(Sousuo.this,2));
                               mygridadpter = new Mygridadpter(Sousuo.this,recyclebeanList);
                               recycle.setAdapter(mygridadpter);
                                recycle.addItemDecoration(gsid);
                                 initongridclik(mygridadpter);
                                gsid = new GridSpacingItemDecoration(2, 0, false);
                            }else {
                               recycle.setLayoutManager(new LinearLayoutManager(Sousuo.this));
                               if(myrecycle==null){
                                   myrecycle = new Mylistadpter(Sousuo.this, recyclebeanList);
                                   recycle.setAdapter(myrecycle);
                               }else {
                                   myrecycle.notifyDataSetChanged();
                               }
                               initonlistclik(myrecycle);
                               recycle.addItemDecoration(gsid);
                               gsid = new GridSpacingItemDecoration(2, 0, false);
                             }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
