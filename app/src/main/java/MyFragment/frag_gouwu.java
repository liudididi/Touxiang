package MyFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.Dingdan;
import com.liu.asus.dianshang.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Adapter.Mygwrecycle;
import bean.Fenleibean;
import bean.Shangpin;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/28.
 */

public class frag_gouwu extends Fragment {

    private View view;
    private RecyclerView gw_recycle;
    private SharedPreferences sp;
    private int uid;
    private  boolean first;
    private   static LinearLayout gw_jiesuan;
    private   static TextView tv_tishi;
    private static CheckBox ck_quanxuan;
    private static TextView tv_zong;
    private static double zongjia;
    private Button bt_jiesuan;
    private LinearLayout linea_dingdan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_gouwu, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        sp = getActivity().getSharedPreferences("jdsp", Context.MODE_PRIVATE);
        gw_jiesuan.setVisibility(View.GONE);
        tv_tishi.setVisibility(View.VISIBLE);
        uid = sp.getInt("uid", -1);
        if(uid!=-1){
            getdata();
        }else {
            Toast.makeText(getActivity(), "去商城逛逛再来吧", Toast.LENGTH_SHORT).show();
        }
    }

    private void getdata() {
        Okhttputils.requer("http://120.27.23.105/product/getCarts?uid="+uid, new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
              getActivity().runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Toast.makeText(getActivity(), "网络较差，稍后重试", Toast.LENGTH_SHORT).show();
                  }
              });
            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                    String string=response.body().string();
                    JSONObject  json = new JSONObject(string);
                    JSONArray data = json.getJSONArray("data");
                    final List<JSONObject> objects=new ArrayList<JSONObject>();
                    for (int i = 0; i <data.length() ; i++) {
                        JSONObject l= (JSONObject) data.get(i);
                        objects.add(l);
                    }
                    if(objects!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<CheckBox> checkBoxes=new ArrayList<CheckBox>();
                                gw_jiesuan.setVisibility(View.VISIBLE);
                                tv_tishi.setVisibility(View.GONE);
                                final Mygwrecycle mygwrecycle=new Mygwrecycle(getActivity(),objects,checkBoxes);
                                gw_recycle.setAdapter(mygwrecycle);
                                bt_jiesuan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(zongjia==0.0){
                                            Toast.makeText(getActivity(), "总价零你好意思?", Toast.LENGTH_SHORT).show();
                                            return;
                                        }


                                        Okhttputils.requer("http://120.27.23.105/product/createOrder?uid="+uid+"&price="+zongjia, new Okhttputils.Backquer() {
                                            @Override
                                            public void onfailure(Call call, IOException e) {



                                            }

                                            @Override
                                            public void onresponse(Call call, Response response) {
                                                try {

                                                    String string = response.body().string();
                                                    JSONObject json=new JSONObject(string );
                                                    final String msg = json.getString("msg");
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }  catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });


                                    }
                                });
                                ck_quanxuan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(ck_quanxuan.isChecked()){
                                            List<Boolean> getlistflag = mygwrecycle.getlistflag();
                                            for (int i = 0; i <getlistflag.size() ; i++) {
                                                getlistflag.set(i,true);
                                            }
                                            mygwrecycle.notifyDataSetChanged();
                                        }else {
                                            List<Boolean> getlistflag = mygwrecycle.getlistflag();
                                            for (int i = 0; i <getlistflag.size() ; i++) {
                                                getlistflag.set(i,false);
                                            }
                                            mygwrecycle.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




    }

    private void initview() {
        gw_recycle = view.findViewById(R.id.gw_recycle);
        gw_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        gw_jiesuan = view.findViewById(R.id.gw_jiesuan);
        ck_quanxuan = view.findViewById(R.id.ck_quanxuan);
        bt_jiesuan = view.findViewById(R.id.bt_jiesuan);
        tv_zong = view.findViewById(R.id.tv_zong);
        tv_tishi= view.findViewById(R.id.tv_tishi);
        linea_dingdan = view.findViewById(R.id.linea_dingdan);
        linea_dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Dingdan.class);
                startActivity(intent);
            }
        });
    }

    public static  void  setzongjia(double prices){
        zongjia=prices;
        DecimalFormat   df = new DecimalFormat("#0.000");
        df.format(zongjia);
        tv_zong.setText("￥"+zongjia);
    }
    public static  void  setckquanxuan(boolean  b){
        ck_quanxuan.setChecked(b);
    }
    public static  void  setjiesuanhind(){
        gw_jiesuan.setVisibility(View.GONE);
        tv_tishi.setVisibility(View.VISIBLE);
    }


}
