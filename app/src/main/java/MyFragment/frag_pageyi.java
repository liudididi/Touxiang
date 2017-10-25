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

public class frag_pageyi extends Fragment {

    private View view;
    private ImageView yi_img1,yi_img2,yi_img3,yi_img4,yi_img5,yi_img6,yi_img7,yi_img8,yi_img9,yi_img10;
    private TextView  yi_tv1,yi_tv2,yi_tv3,yi_tv4,yi_tv5,yi_tv6,yi_tv7,yi_tv8,yi_tv9,yi_tv10;
    private List<ImageView> imglist;
    private List<TextView> tvlist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.frag_pageyi, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       yi_img1= view.findViewById(R.id.yi_img1);
       yi_img2= view.findViewById(R.id.yi_img2);
       yi_img3= view.findViewById(R.id.yi_img3);
       yi_img4= view.findViewById(R.id.yi_img4);
       yi_img5= view.findViewById(R.id.yi_img5);
       yi_img6= view.findViewById(R.id.yi_img6);
       yi_img7= view.findViewById(R.id.yi_img7);
       yi_img8= view.findViewById(R.id.yi_img8);
       yi_img9= view.findViewById(R.id.yi_img9);
       yi_img10= view.findViewById(R.id.yi_img10);
        imglist = new ArrayList<>();
        imglist.add(yi_img1);
        imglist.add(yi_img2);
        imglist.add(yi_img3);
        imglist.add(yi_img4);
        imglist.add(yi_img5);
        imglist.add(yi_img6);
        imglist.add(yi_img7);
        imglist.add(yi_img8);
        imglist.add(yi_img9);
        imglist.add(yi_img10);
        yi_tv1= view.findViewById(R.id.yi_tv1);
        yi_tv2= view.findViewById(R.id.yi_tv2);
        yi_tv3= view.findViewById(R.id.yi_tv3);
        yi_tv4= view.findViewById(R.id.yi_tv4);
        yi_tv5= view.findViewById(R.id.yi_tv5);
        yi_tv6= view.findViewById(R.id.yi_tv6);
        yi_tv7= view.findViewById(R.id.yi_tv7);
        yi_tv8= view.findViewById(R.id.yi_tv8);
        yi_tv9= view.findViewById(R.id.yi_tv9);
        yi_tv10= view.findViewById(R.id.yi_tv10);
        tvlist = new ArrayList<>();
        tvlist.add(yi_tv1);
        tvlist.add(yi_tv2);
        tvlist.add(yi_tv3);
        tvlist.add(yi_tv4);
        tvlist.add(yi_tv5);
        tvlist.add(yi_tv6);
        tvlist.add(yi_tv7);
        tvlist.add(yi_tv8);
        tvlist.add(yi_tv9);
        tvlist.add(yi_tv10);
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
                    if(recyclebeanList!=null&&getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i <10 ; i++) {
                                    tvlist.get(i).setText(recyclebeanList.get(i).name);
                                    ImageLoader.getInstance().displayImage(recyclebeanList.get(i).imgurl,imglist.get(i));
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
