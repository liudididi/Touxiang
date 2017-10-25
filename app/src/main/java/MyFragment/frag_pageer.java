package MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.asus.dianshang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Fenleibean;
import okhttp3.Call;
import okhttp3.Response;
import utils.Okhttputils;

/**
 * Created by asus on 2017/9/29.
 */

public class frag_pageer extends Fragment {

    private View view;
    private ImageView er_img1,er_img2,er_img3,er_img4,er_img5,er_img6,er_img7,er_img8;
    private TextView  er_tv1,er_tv2,er_tv3,er_tv4,er_tv5,er_tv6,er_tv7,er_tv8;
    private List<ImageView> imglist;
    private List<TextView> tvlist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_pageer, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       er_img1= view.findViewById(R.id.er_img1);
       er_img2= view.findViewById(R.id.er_img2);
       er_img3= view.findViewById(R.id.er_img3);
       er_img4= view.findViewById(R.id.er_img4);
       er_img5= view.findViewById(R.id.er_img5);
       er_img6= view.findViewById(R.id.er_img6);
       er_img7= view.findViewById(R.id.er_img7);
       er_img8= view.findViewById(R.id.er_img8);

        imglist = new ArrayList<>();
        imglist.add(er_img1);
        imglist.add(er_img2);
        imglist.add(er_img3);
        imglist.add(er_img4);
        imglist.add(er_img5);
        imglist.add(er_img6);
        imglist.add(er_img7);
        imglist.add(er_img8);
        er_tv1= view.findViewById(R.id.er_tv1);
        er_tv2= view.findViewById(R.id.er_tv2);
        er_tv3= view.findViewById(R.id.er_tv3);
        er_tv4= view.findViewById(R.id.er_tv4);
        er_tv5= view.findViewById(R.id.er_tv5);
        er_tv6= view.findViewById(R.id.er_tv6);
        er_tv7= view.findViewById(R.id.er_tv7);
        er_tv8= view.findViewById(R.id.er_tv8);

        tvlist = new ArrayList<>();
        tvlist.add(er_tv1);
        tvlist.add(er_tv2);
        tvlist.add(er_tv3);
        tvlist.add(er_tv4);
        tvlist.add(er_tv5);
        tvlist.add(er_tv6);
        tvlist.add(er_tv7);
        tvlist.add(er_tv8);
        initdata();




    }

    private void initdata() {
        Okhttputils.requer("http://120.27.23.105/product/getCatagory", new Okhttputils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onresponse(Call call, Response response) {
                try {
                     final List<Fenleibean> recyclebeanList=new ArrayList<>();
                    JSONObject json=new JSONObject(response.body().string());
                    JSONArray data = json.getJSONArray("data");
                    for (int i = 0; i <data.length() ; i++) {
                        JSONObject l= (JSONObject) data.get(i);
                        Fenleibean fenleibean=new Fenleibean();
                        fenleibean.name=l.optString("name");
                        fenleibean.imgurl=l.optString("icon");
                        recyclebeanList.add(fenleibean);
                    }
                    if(recyclebeanList!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i <8 ; i++) {
                                    tvlist.get(i).setText(recyclebeanList.get(i+11).name);
                                    ImageLoader.getInstance().displayImage(recyclebeanList.get(i+11).imgurl,imglist.get(i));
                                }
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
